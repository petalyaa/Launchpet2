package org.pet.launchpet2.model;

public class AppsDrawerSettingItem {
	
	private int iconResId;
	
	private String label;
	
	private String description;
	
	private Class<?> activityClass;
	
	public AppsDrawerSettingItem(int iconResId, String label, String description, Class<?> activityClass) {
		setIconResId(iconResId);
		setLabel(label);
		setDescription(description);
		setActivityClass(activityClass);
	}

	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Class<?> getActivityClass() {
		return activityClass;
	}

	public void setActivityClass(Class<?> activityClass) {
		this.activityClass = activityClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
