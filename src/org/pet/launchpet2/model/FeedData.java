package org.pet.launchpet2.model;

import java.util.HashMap;

import org.pet.launchpet2.util.StringUtil;

public class FeedData extends HashMap<String, String> {
	
	public static enum FeedSource {
		DZONE;
	}
	
	private static final long serialVersionUID = 935998833452575643L;

	public static final String TITLE_KEY = "title";
	
	public static final String LINK_KEY = "link";
	
	public static final String DESCRIPTION_KEY = "description";
	
	public static final String CATEGORY_KEY = "category";
	
	public static final String PUB_DATE_KEY = "pubDate";
	
	public static final String PUB_DATE_9GAG_KEY = "a10:updated";
	
	private FeedSource source;
	
	public String getTitle() {
		return get(TITLE_KEY);
	}

	public void setTitle(String title) {
		put(TITLE_KEY, title);
	}

	public String getLink() {
		return get(LINK_KEY);
	}

	public void setLink(String link) {
		put(LINK_KEY, link);
	}

	public String getDescription() {
		return get(DESCRIPTION_KEY);
	}

	public void setDescription(String description) {
		put(DESCRIPTION_KEY, description);
	}

	public String getCategory() {
		return get(CATEGORY_KEY);
	}

	public void setCategory(String category) {
		put(CATEGORY_KEY, category);
	}

	public String getPubDate() {
		String pubDate = get(PUB_DATE_KEY);
		if(StringUtil.isNullEmptyString(pubDate))
			pubDate = get(PUB_DATE_9GAG_KEY);
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		put(PUB_DATE_KEY, pubDate);
	}

	public FeedSource getSource() {
		return source;
	}

	public void setSource(FeedSource source) {
		this.source = source;
	}

}
