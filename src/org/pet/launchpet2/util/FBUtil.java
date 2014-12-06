package org.pet.launchpet2.util;

import android.util.Log;

import com.facebook.Session;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.listeners.OnLoginListener;

public class FBUtil {
	
	public static final String FB_APP_ID = "926046527413033";
	
	public static final String FB_NAMESPACE = "launchpet";

	public static interface FBCallback {
		
		public void onConnect();
		
	}
	
	public static final Permission[] FB_PERMISSION = new Permission[] {
			Permission.USER_PHOTOS, Permission.EMAIL, Permission.PUBLISH_ACTION };

	public static final SimpleFacebookConfiguration FB_CONFIGURATION = new SimpleFacebookConfiguration.Builder()
			.setAppId(FB_APP_ID).setNamespace(FB_NAMESPACE)
			.setPermissions(FB_PERMISSION).build();
	
	public static final boolean isFacebookEnabled() {
		return true;
	}
	
	public static final Session getFacebookSession(SimpleFacebook mSimpleFacebook, final FBCallback callback) {
		Session session = Session.getActiveSession();
		OnLoginListener onLoginListener = new OnLoginListener() {
		    @Override
		    public void onLogin() {
		    	if(callback != null)
		    		callback.onConnect();
		    }

		    @Override
		    public void onNotAcceptingPermissions(Permission.Type type) {
		        Log.w("Launchpet2", String.format("You didn't accept %s permissions", type.name()));
		    }

			@Override
			public void onThinking() {
				
			}

			@Override
			public void onException(Throwable arg0) {
				
			}

			@Override
			public void onFail(String arg0) {
				
			}

		};
		mSimpleFacebook.login(onLoginListener);
		return session;
	}

}
