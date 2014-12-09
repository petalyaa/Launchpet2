package org.pet.launchpet2.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationUtil {

	public static final boolean isPackageExisted(Context context, String targetPackage) {
		PackageManager pm = context.getPackageManager();
		boolean isInstalled = false;
		try {
			pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
			isInstalled = true;
		} catch (NameNotFoundException e) {
		}
		return isInstalled;
	}

}
