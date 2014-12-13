package org.pet.launchpet2.util;

import android.annotation.SuppressLint;

public class StringUtil {

	public static final String shortened(String s, int length) {
		if (s.length() > length) {
			s = s.substring(0, length);
			s = s + "…";
		}
		return s;
	}

	public static final boolean isNullEmptyString(String str) {
		return str == null || str.equals("");
	}

	public static final String toCamelCase(String s) {
		String[] parts = s.split(" ");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + " " + toProperCase(part);
		}
		return camelCaseString;
	}

	@SuppressLint("DefaultLocale")
	public static final String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

}
