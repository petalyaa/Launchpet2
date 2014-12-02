package org.pet.launchpet2.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;

public class HomeNewsItem extends HashMap<String, String> implements Comparable<HomeNewsItem> {
	
	private static final long serialVersionUID = -1786892733088931857L;

	public static enum NewsType {
		IMAGE, TEXT, DZONE;
	}
	
	private NewsType type;
	
	private Bitmap icon;
	
	private String iconUrl;
	
	private String imageUrl;
	
	private List<String> imageUrlList;
	
	private String title;
	
	private String content;
	
	private Bitmap image;
	
	private Date date;
	
	private List<FooterAction> footerActionList;

	public NewsType getType() {
		return type;
	}

	public void setType(NewsType type) {
		this.type = type;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<FooterAction> getFooterActionList() {
		return footerActionList;
	}

	public void setFooterActionList(List<FooterAction> footerActionList) {
		this.footerActionList = footerActionList;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int compareTo(HomeNewsItem another) {
		return another.getDate().compareTo(getDate());
	}

	public List<String> getImageUrlList() {
		return imageUrlList;
	}

	public void setImageUrlList(List<String> imageUrlList) {
		this.imageUrlList = imageUrlList;
	}

}
