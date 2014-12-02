package org.pet.launchpet2.model;

public class LauncherSettingItem {
	
	private int iconResId;

	private String label;
	
	private SettingAction action;
	
	public LauncherSettingItem(int iconResId, String label, SettingAction action) {
		this.iconResId = iconResId;
		this.label = label;
		this.action = action;
	}

	public String getLabel() {
		return label;
	}

	public SettingAction getAction() {
		return action;
	}
	
	public int getIconResId() {
		return iconResId;
	}

}
