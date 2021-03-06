package org.pet.launchpet2.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigurationUtil {
	
	public static final SimpleDateFormat NEWS_FEED_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
	
	public static final String FAVORITE_FEED_SOURCE_JSON = "favorite_feed_source.json";
	
	public static final String SHARED_PREFERENCE_KEY_APPLICATION_SETTING = "org.pet.launchpet2.MainActivity.application_settings";
	
	public static final String SHARED_PREFERENCE_KEY_FAVORITE = "favorite_app_list";
	
	public static final int IMAGE_MIN_HEIGHT = 10;
	
	public static final int IMAGE_MIN_WIDTH = 10;
	
	public static final String DZONE_KEY = "dzone";
	
	public static final String DZONE_URL = "http://feeds.dzone.com/dzone/frontpage?format=xml";

	public static final String DZONE_FEEDBURNER_INFO = "dzone/frontpage";
	
	public static final String DZONE_SELFLINK_KEY = "dz:selfLink";
	
	public static final String RSS_LINK_TAG = "link";
	
	public static final String RSS_TITLE_TAG = "title";
	
	public static final int SIGNAL_OPEN_LINK_BROWSER = 100;
	
	public static final String APPLICATION_SD_DIRECTORY = "Launchpet2";
	
	public static final String FILENAME_PROFILE_IMAGE = "profile.jpeg";
	
	public static final String FILENAME_BANNER_IMAGE = "banner.jpeg";
	
	public static final String FILENAME_WALLPAPER_IMAGE = "wallpaper.png";
	
	public static final String FILENAME_NEWS_CACHE = "news";
	
	public static final String FILENAME_APPS_CACHE = "apps";
	
	public static final String SUBDIRECTORY_CACHE = "cache";
	
	public static final String SUBDIRECTORY_MEDIA = "media";
	
	public static final String SUBDIRECTORY_IMAGES = "images";
	
	public static final String SUBDIRECTORY_APP_ICONS = "app_icons";
	
	public static final String SUBDIRECTORY_FAVICON = "favicon";
	
	public static final String SUBDIRECTORY_NEWS = "news";
	
	public static final String SUBDIRECTORY_APPS = "apps";
	
	public static final int DIMENSION_X_PROFILE_IMAGE = 200;
	
	public static final int DIMENSION_Y_PROFILE_IMAGE = 200;

	public static final int DIMENSION_X_BANNER_IMAGE = 1280;
	
	public static final int DIMENSION_Y_BANNER_IMAGE = 800;
	
	public static final List<String> RSS_URL_TO_LOAD = new ArrayList<String>();
	
	public static final long CACHE_SERVICE_STARTUP_DELAY = 1000;
	
	public static final long CACHE_SERVICE_CHECKING_INTERVAL = 1000 * 60 * 60 * 12;
	
	public static final int CACHE_MAX_DAYS = 1;
	
	public static final int MAX_FAVORITE = 6;
	
	public static final float MAX_TOOLBAR_TRANSPARENCY = 1f;
	
	public static final float MIN_TOOLBAR_TRANSPARENCY = .6f;
	
	public static final String GOOGLE_NOW_PACKAGE = "com.google.android.googlequicksearchbox";
	
	public static final String PLAY_STORE_MOBILE_URI_STR = "market://details?id=";
	
	public static final String PLAY_STORE_WEB_URI_STR = "http://play.google.com/store/apps/details?id=";
	
//	public static final long WEATHER_UPDATE_FREQUENCY = 1000 * 60 * 60;
	
	public static final long WEATHER_UPDATE_FREQUENCY = 1000 * 5;
	
	public static final String SHARED_PREFERENCE_GENERAL_SETTINGS = "general_settings";
	
	public static final String SHARED_PREFERENCE_ADVANCED_SETTINGS = "advanced_settings";
	
	public static final String SHARED_PREFERENCE_FEED_SETTINGS = "feed_settings";
	
	public static final String SHARED_PREFERENCE_FEED_SOURCE = "feed_source";
	
	public static final String SHARED_PREFERENCE_FEED_UPDATE = "feed_update";
	
	public static final String SHARED_PREFERENCE_KEY_REQUIRE_RELOAD = "require_reload";
	
	public static final String SHARED_PREFERENCE_KEY_REQUIRE_RESTART = "require_restart";
	
	public static final String SHARED_PREFERENCE_HIDDEN_APPS_SETTINGS = "hidden_apps_settings";
	
	public static final String SHARED_PREFERENCE_EXISTING_HIDDEN_APPS_KEY = "existing_hidden_apps";
	
	public static final String SHARED_PREFERENCE_APPS_GROUP_SETTINGS = "group_apps_settings";
	
	public static final String SHARED_PREFERENCE_APPS_GROUP_SETTINGS_LIST_KEY = "group_apps_list";

	public static final int FOLDER_ICON_STACK_LIMIT = 4;
	
	public static final int NATIVE_DRAWER_ROW_COUNT = 5;
	
	public static final int NATIVE_DRAWER_COL_COUNT = 4;
	
	public static final int RESULT_RELOAD_FAVORITE = 100;
	
	static {
		RSS_URL_TO_LOAD.add("http://feeds.dzone.com/dzone/frontpage?format=xml");
		RSS_URL_TO_LOAD.add("http://feeds.feedburner.com/androidcentral?format=xml");
		RSS_URL_TO_LOAD.add("http://feeds.feedburner.com/xda-developers/ShsH?format=xml");
	}
	
	public static final boolean isRequireReload(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_GENERAL_SETTINGS, Context.MODE_PRIVATE);
		return pref.getBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RELOAD, false);
	}
	
	public static final boolean isRequireRestart(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_ADVANCED_SETTINGS, Context.MODE_PRIVATE);
		return pref.getBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RESTART, false);
	}
	
	public static final void setRequireRestart(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_ADVANCED_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RESTART, true);
		editor.commit();
	}
	
	public static final void unsetRequireRestart(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_ADVANCED_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RESTART, false);
		editor.commit();
	}
	
	public static final void setRequireReload(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_GENERAL_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RELOAD, true);
		editor.commit();
	}
	
	public static final void unsetRequireReload(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_GENERAL_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RELOAD, false);
		editor.commit();
	}
	
	public static final boolean isRequireFeedReload(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_FEED_SETTINGS, Context.MODE_PRIVATE);
		return pref.getBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RELOAD, false);
	}
	
	public static final void setRequireFeedReload(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_FEED_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RELOAD, true);
		editor.commit();
	}
	
	public static final void unsetRequireFeedReload(Context context) {
		SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_FEED_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(SHARED_PREFERENCE_KEY_REQUIRE_RELOAD, false);
		editor.commit();
	}
	
}
