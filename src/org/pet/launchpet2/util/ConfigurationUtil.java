package org.pet.launchpet2.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfigurationUtil {
	
	public static final SimpleDateFormat NEWS_FEED_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
	
	public static final String FAVORITE_FEED_SOURCE_JSON = "favorite_feed_source.json";
	
	public static final String SHARED_PREFERENCE_KEY_APPLICATION_SETTING = "org.pet.launchpet2.MainActivity.application_settings";
	
	public static final String SHARED_PREFERENCE_KEY_FAVORITE = "favorite_app_list";
	
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
	
	public static final String FILENAME_NEWS_CACHE = "news";
	
	public static final String SUBDIRECTORY_CACHE = "cache";
	
	public static final String SUBDIRECTORY_MEDIA = "media";
	
	public static final String SUBDIRECTORY_IMAGES = "images";
	
	public static final String SUBDIRECTORY_FAVICON = "favicon";
	
	public static final String SUBDIRECTORY_NEWS = "news";
	
	public static final int DIMENSION_X_PROFILE_IMAGE = 200;
	
	public static final int DIMENSION_Y_PROFILE_IMAGE = 200;
	
	public static final int DIMENSION_X_BANNER_IMAGE = 1280;
	
	public static final int DIMENSION_Y_BANNER_IMAGE = 800;
	
	public static final List<String> RSS_URL_TO_LOAD = new ArrayList<String>();
	static {
		RSS_URL_TO_LOAD.add("http://feeds.dzone.com/dzone/frontpage?format=xml");
//		RSS_URL_TO_LOAD.add("http://podcasts.engadget.com/rss.xml");
		RSS_URL_TO_LOAD.add("http://feeds.feedburner.com/androidcentral?format=xml");
		RSS_URL_TO_LOAD.add("http://feeds.feedburner.com/xda-developers/ShsH?format=xml");
	}
	
}
