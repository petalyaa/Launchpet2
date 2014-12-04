package org.pet.launchpet2.populator;

import org.pet.launchpet2.model.HomeNewsItem;

import android.view.LayoutInflater;
import android.view.View;

public abstract class Populator {
	
	protected HomeNewsItem item;
	
	private int titleBackgroundColor;
	
	private int contentBackgroundColor;
	
	public Populator(HomeNewsItem item) {
		this.item = item;
	}
	
	public abstract View populateView(LayoutInflater inflater);

	public int getTitleBackgroundColor() {
		return titleBackgroundColor;
	}

	public void setTitleBackgroundColor(int titleBackgroundColor) {
		this.titleBackgroundColor = titleBackgroundColor;
	}

	public int getContentBackgroundColor() {
		return contentBackgroundColor;
	}

	public void setContentBackgroundColor(int contentBackgroundColor) {
		this.contentBackgroundColor = contentBackgroundColor;
	}

}
