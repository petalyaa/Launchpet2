package org.pet.launchpet2.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pet.launchpet2.R;
import org.pet.launchpet2.listener.BrowserLinkOpenListener;
import org.pet.launchpet2.listener.OnCardTouchListener;
import org.pet.launchpet2.model.FeedData;
import org.pet.launchpet2.model.HomeNewsItem;
import org.pet.launchpet2.model.RSSFeedSource;
import org.pet.launchpet2.model.FeedData.FeedSource;
import org.pet.launchpet2.model.HomeNewsItem.NewsType;
import org.pet.launchpet2.populator.DzoneCardPopulator;
import org.pet.launchpet2.populator.ImageCardPopulator;
import org.pet.launchpet2.populator.Populator;
import org.pet.launchpet2.populator.TextCardPopulator;
import org.pet.launchpet2.thread.FetchFaviconAsync;
import org.pet.launchpet2.util.CommonUtil;
import org.pet.launchpet2.util.StringUtil;
import org.pet.launchpet2.util.XMLParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LauncherNewsFragment extends LauncherHomeFragment {
	
	private static final String RSS_FEED_JSON_KEY = "rss";
	
	private LinearLayout mLinearLayout;
	
	private LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		mLinearLayout = (LinearLayout) rootView.findViewById(R.id.home_card_main_list);
		populateHomeCard();
		return rootView;
	}
	
	private String getExistingSourceFromSharedPreference() {
		String sourceJson = null;
		SharedPreferences pref = getActivity().getSharedPreferences("feed_settings", Context.MODE_PRIVATE);
		sourceJson = pref.getString("feed_source", "");
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("feed_source", sourceJson);
		editor.putBoolean("feed_update", false);
		editor.commit();
		return sourceJson;
	}
	
	@SuppressLint("InflateParams")
	private void addNewsToView(HomeNewsItem item) {
		Log.v("Launchpet2", "Adding new item : " + item.getTitle());
		CardView cardView = (CardView) inflater.inflate(R.layout.home_card_text, mLinearLayout, false);
		ImageView cardIcon = (ImageView) cardView.findViewById(R.id.card_view_title_icon);
		TextView cardTitle = (TextView) cardView.findViewById(R.id.card_view_title_label);
		cardTitle.setText(Html.fromHtml(item.getTitle()));
		new FetchFaviconAsync(cardIcon).execute(item.get("parentLink"));
		mLinearLayout.addView(cardView);
	}
	
	private List<RSSFeedSource> getExistingFeedSource() {
		List<RSSFeedSource> list = new ArrayList<RSSFeedSource>();
		String jsonStr = getExistingSourceFromSharedPreference();
		if(!StringUtil.isNullEmptyString(jsonStr)) {
			try {
				JSONArray jsonArray = new JSONArray(jsonStr);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject favJsonObj = jsonArray.getJSONObject(i);
					String name = favJsonObj.getString("name");
					String feedUrl = favJsonObj.getString("feedUrl");
					String webUrl = favJsonObj.getString("webUrl");
					boolean isFavorite = favJsonObj.getBoolean("isFavorite");
					long dateAdded = favJsonObj.getLong("dateAdded");
					RSSFeedSource source = new RSSFeedSource();
					source.setName(name);
					source.setUrl(webUrl);
					source.setFeedUrl(feedUrl);
					source.setFavorite(isFavorite);
					source.setDateAdded(dateAdded);
					list.add(source);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(list);
		return list;
	}
	
	@SuppressLint("InflateParams")
	private void populateHomeCard() {
		JSONArray rssList = new JSONArray();
		List<RSSFeedSource> sourceList = getExistingFeedSource();
		if(sourceList != null && sourceList.size() > 0) {
			for(RSSFeedSource source : sourceList) {
				rssList.put(source.getFeedUrl());
			}
		}
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put(RSS_FEED_JSON_KEY, rssList);
			FetchNewsTask task = new FetchNewsTask();
			task.execute(jsonObj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private class FetchNewsTask extends AsyncTask<String, Void, List<HomeNewsItem>> {

		@SuppressLint("InflateParams")
		@Override
		protected List<HomeNewsItem> doInBackground(String... arg0) {
			JSONArray rssFeedUrlArray = null;
			try {
				JSONObject jsonObj = new JSONObject(arg0[0]);
				rssFeedUrlArray = jsonObj.getJSONArray(RSS_FEED_JSON_KEY);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			List<FeedData> feedDataList = new ArrayList<FeedData>();
			if(rssFeedUrlArray != null) {
				for(int i = 0; i < rssFeedUrlArray.length(); i++) {
					InputStream stream = null;
					try {
						String feedUrl = rssFeedUrlArray.getString(i);
						URL url = new URL(feedUrl);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(10000 /* milliseconds */);
						conn.setConnectTimeout(15000 /* milliseconds */);
						conn.setRequestMethod("GET");
						conn.setDoInput(true);
						conn.connect();
						stream = conn.getInputStream();
						XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
						XmlPullParser myparser = xmlFactoryObject.newPullParser();
						myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
						myparser.setInput(stream, null);
						XMLParser parser = new XMLParser();
						feedDataList.addAll(parser.parseXMLAndStoreIt(myparser));
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if(stream != null)
								stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			List<HomeNewsItem> homeNewsItemList = new ArrayList<HomeNewsItem>(feedDataList.size());
			// Feed data
			SimpleDateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
			for(FeedData rssFeedData : feedDataList) {
				NewsType type = NewsType.TEXT;
				HomeNewsItem item = new HomeNewsItem();
				item.setContent(rssFeedData.getDescription());
				try {
					item.setDate(rssDateFormat.parse(rssFeedData.getPubDate()));
				} catch (ParseException e) {
					item.setDate(new Date());
				}

				String iconUrl = null;
				String thumbnailUrl = null;

				// Hardcoded icon for dzone
				FeedSource source = rssFeedData.getSource();
				if(FeedSource.DZONE == source) {
					iconUrl = rssFeedData.get("dz:userimage");
					thumbnailUrl = rssFeedData.get("dz:thumbnail");
					type = NewsType.IMAGE;
				} else {
					String content = rssFeedData.getDescription();
					Document doc = Jsoup.parse(content);
					Elements imgs = doc.getElementsByTag("img");
					List<String> imageSrcUrlList = new ArrayList<String>();
					for (Element el : imgs) {
						String src = el.absUrl("src");
						if(thumbnailUrl == null)
							thumbnailUrl = src;
						imageSrcUrlList.add(src);
						type = NewsType.IMAGE;
						item.setImageUrlList(imageSrcUrlList);
					}

				}
				item.setIconUrl(iconUrl);
				item.setImageUrl(thumbnailUrl);
				item.setTitle(rssFeedData.getTitle());
				item.setType(type);
				item.putAll(rssFeedData);
				homeNewsItemList.add(item);
			}

			Collections.sort(homeNewsItemList);
			CommonUtil.serializeHomeNewsObjectList(homeNewsItemList);
			return homeNewsItemList;
		}

		@Override
		protected void onPreExecute() {
			List<HomeNewsItem> homeNewsItemList = CommonUtil.deserializeHomeNewsObject();
			if(homeNewsItemList != null && homeNewsItemList.size() > 0) {
				mLinearLayout.removeAllViews();
				for(HomeNewsItem item : homeNewsItemList) {
					addNewsToView(item);
				}
			}
		}

		@SuppressLint("InflateParams")
		@Override
		protected void onPostExecute(final List<HomeNewsItem> homeNewsItemList) {
			if(homeNewsItemList != null && homeNewsItemList.size() > 0) {
				mLinearLayout.removeAllViews();
				for(HomeNewsItem item : homeNewsItemList) {
					addNewsToView(item);
				}
			}
		}
	}

}
