package org.pet.launchpet2.populator;

import org.pet.launchpet2.model.HomeNewsItem;

import android.view.LayoutInflater;
import android.view.View;

public abstract class Populator {
	
	protected HomeNewsItem item;
	
	public Populator(HomeNewsItem item) {
		this.item = item;
	}
	
	public abstract View populateView(LayoutInflater inflater);

}
