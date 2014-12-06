package org.pet.launchpet2.util;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

public class FBUtil {
	
	public static final String FB_APP_ID = "926046527413033";
	
	public static final String FB_NAMESPACE = "launchpet";

	public static final Permission[] FB_PERMISSION = new Permission[] {
			Permission.USER_PHOTOS, Permission.EMAIL, Permission.PUBLISH_ACTION };

	public static final SimpleFacebookConfiguration FB_CONFIGURATION = new SimpleFacebookConfiguration.Builder()
			.setAppId(FB_APP_ID).setNamespace(FB_NAMESPACE)
			.setPermissions(FB_PERMISSION).build();

}
