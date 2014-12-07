package org.pet.launchpet2.model;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.Locale;

public class HiddenApplicationItem implements Serializable, Comparable<HiddenApplicationItem> {
	
	private static final long serialVersionUID = 3094506692642296514L;

	private String name;
	
	private String packageName;
	
	private boolean hidden;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int compareTo(HiddenApplicationItem another) {
		return getName().toLowerCase(Locale.getDefault()).compareTo(another.getName().toLowerCase());
	}

}
