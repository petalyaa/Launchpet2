package org.pet.launchpet2.model;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class LauncherApplication implements Comparable<LauncherApplication> {
	
	public static enum Type {
		APPLICATION, FOLDER;
	}

	private Intent launchActivity;
	
	private String packageName;
	
	private ApplicationInfo applicationInfo;
	
	private String name;
	
	private Drawable icon;
	
	private Type type;
	
	private int iconResId;
	
	private boolean isInTheGroup;
	
	private boolean startGroup;
	
	private List<LauncherApplication> groupAppList;

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

	@SuppressLint("DefaultLocale")
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

	public boolean isStartGroup() {
		return startGroup;
	}

	public void setStartGroup(boolean startGroup) {
		this.startGroup = startGroup;
	}

	public boolean isInTheGroup() {
		return isInTheGroup;
	}

	public void setInTheGroup(boolean isInTheGroup) {
		this.isInTheGroup = isInTheGroup;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<LauncherApplication> getGroupAppList() {
		return groupAppList;
	}

	public void setGroupAppList(List<LauncherApplication> groupAppList) {
		this.groupAppList = groupAppList;
	}
	
}
