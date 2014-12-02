package org.pet.launchpet2.model;

public class RSSFeedSource implements Comparable<RSSFeedSource> {
	
	private String name;
	
	private String feedUrl;
	
	private boolean favorite;
	
	private String url;
	
	private long dateAdded;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public long getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(long dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	@Override
	public int compareTo(RSSFeedSource another) {
		return Long.compare(another.getDateAdded(), getDateAdded());
	}

}
