package org.pet.launchpet2.model;

import java.util.List;

import org.pet.launchpet2.adapter.ImageStackViewAdapter;

import android.graphics.Bitmap;

public class GroupApps {
	
	private Bitmap image;
	
	private String name;
	
	private List<String> packageList;
	
	private List<Bitmap> bitmapList;
	
	private List<LauncherApplication> appList;
	
	private ImageStackViewAdapter stackAdapter;
	
	private Integer[] resIdArray;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LauncherApplication> getAppList() {
		return appList;
	}

	public void setAppList(List<LauncherApplication> appList) {
		this.appList = appList;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public ImageStackViewAdapter getStackAdapter() {
		return stackAdapter;
	}

	public void setStackAdapter(ImageStackViewAdapter stackAdapter) {
		this.stackAdapter = stackAdapter;
	}

	public Integer[] getResIdArray() {
		return resIdArray;
	}

	public void setResIdArray(Integer[] resIdArray) {
		this.resIdArray = resIdArray;
	}

	public List<Bitmap> getBitmapList() {
		return bitmapList;
	}

	public void setBitmapList(List<Bitmap> bitmapList) {
		this.bitmapList = bitmapList;
	}

	public List<String> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<String> packageList) {
		this.packageList = packageList;
	}

}
