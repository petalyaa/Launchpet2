package org.pet.launchpet2.model;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class LauncherApplication implements Comparable<LauncherApplication> {

	private Intent launchActivity;
	
	private String packageName;
	
	private ApplicationInfo applicationInfo;
	
	private String name;
	
	private Drawable icon;
	
	private int iconResId;

	public Intent getLaunchActivity() {
		return launchActivity;
	}

	public void setLaunchActivity(Intent launchActivity) {
		this.launchActivity = launchActivity;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}

	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public int compareTo(LauncherApplication another) {
		return getName().toLowerCase().compareTo(another.getName().toLowerCase());
	}

	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}
	
}
