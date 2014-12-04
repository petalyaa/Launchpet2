package org.pet.launchpet2.util;

public class StringUtil {

	public static final String shortened(String s, int length) {
		if(s.length() > length) {
			s = s.substring(0, length);
			s = s + "…";
		}
		return s;
	}
	
	public static final boolean isNullEmptyString(String str) {
		return str == null || str.equals("");
	}
	
}
