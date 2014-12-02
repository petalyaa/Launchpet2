package org.pet.launchpet2.util;

import java.util.ArrayList;
import java.util.List;

import org.pet.launchpet2.model.FeedData;
import org.pet.launchpet2.model.RSSFeedSource;
import org.xmlpull.v1.XmlPullParser;

public class XMLParser {

	public volatile boolean parsingComplete = true;

	public RSSFeedSource parseFeedSource(String feedUrl, XmlPullParser pullParser) {
		RSSFeedSource source = null;
		if(!StringUtil.isNullEmptyString(feedUrl)) {
			int event;
			String text=null;
			String parentLink = null;
			String feedName = null;
			try {
				event = pullParser.getEventType();
				boolean isListenToParentLink = false;
				boolean isListenToTitle = false;
				while (event != XmlPullParser.END_DOCUMENT) {
					String name = pullParser.getName();
					switch (event){
					case XmlPullParser.START_TAG:
						if (parentLink == null && name.equals(ConfigurationUtil.RSS_LINK_TAG)) {
							isListenToParentLink = true;
						} else if (feedName == null && name.equals(ConfigurationUtil.RSS_TITLE_TAG))
							isListenToTitle = true;
						break;
					case XmlPullParser.TEXT:
						text = pullParser.getText();
						break;
					case XmlPullParser.END_TAG:
						if(name.equals("link")){ 	
							if(isListenToParentLink) {
								parentLink = text;
								isListenToParentLink = false;
							}
						} else if (name.equals(ConfigurationUtil.RSS_TITLE_TAG)) {
							if(isListenToTitle) {
								feedName = text;
								isListenToTitle = false;
							}
						}
						break;
					}		 
					event = pullParser.next(); 
				}
				source = new RSSFeedSource();
				source.setFavorite(false);
				source.setFeedUrl(feedUrl);
				source.setUrl(parentLink);
				source.setName(feedName);
				parsingComplete = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return source;
	}

	public List<FeedData> parseXMLAndStoreIt(XmlPullParser pullParser) {
		int event;
		String text=null;
		List<FeedData> feedDataList = new ArrayList<FeedData>();
		try {
			event = pullParser.getEventType();
			FeedData.FeedSource source = null;
			boolean isBegin = false;
			FeedData data = new FeedData();
			String parentTitle = null;
			String parentLink = null;
			boolean isListenToParentLink = false;
			boolean isListenToParentTitle = false;
			while (event != XmlPullParser.END_DOCUMENT) {
				if(isBegin) {
					data = new FeedData();
					isBegin = false;
				}
				String name = pullParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:
					if(name.equals("item"))
						isBegin = true;
					if(name.equals(ConfigurationUtil.DZONE_SELFLINK_KEY)) {
						source = FeedData.FeedSource.DZONE;
					} else if (parentLink == null && name.equals(ConfigurationUtil.RSS_LINK_TAG)) {
						isListenToParentLink = true;
					} else if (parentTitle == null && name.equals("title")) {
						isListenToParentTitle = true;
					}
					break;
				case XmlPullParser.TEXT:
					text = pullParser.getText();
					break;
				case XmlPullParser.END_TAG:
					if(name.equals("item")) {
						data.setSource(source);
						data.put("parentTitle", parentTitle);
						data.put("parentLink", parentLink);
						isBegin = false;
						feedDataList.add(data);
					} else if(name.equals("title")){
						data.setTitle(text);
						if(isListenToParentTitle) {
							parentTitle = text;
							isListenToParentTitle = false;
						}
					} else if(name.equals("link")){ 	
						data.setLink(text);
						if(isListenToParentLink) {
							parentLink = text;
							isListenToParentLink = false;
						}
					} else if(name.equals("description")){
						data.setDescription(text);
					} else {
						data.put(name, text);
					}
					break;
				}		 
				event = pullParser.next(); 
			}
			parsingComplete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedDataList;
	}

}
