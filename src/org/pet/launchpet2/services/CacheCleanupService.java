package org.pet.launchpet2.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.pet.launchpet2.util.ConfigurationUtil;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class CacheCleanupService extends IntentService {
	
	private static boolean IS_ALREADY_STARTED = false;
	
	public CacheCleanupService() {
		super("CacheCleanupService");
	}

	public CacheCleanupService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(IS_ALREADY_STARTED)
			return;
		startScheduling();
		IS_ALREADY_STARTED = true;
	}

	private void startScheduling() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				processCache();
			}
		}, ConfigurationUtil.CACHE_SERVICE_STARTUP_DELAY, ConfigurationUtil.CACHE_SERVICE_CHECKING_INTERVAL);
	}
	
	private List<File> getDirectory2Monitor() {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/" + ConfigurationUtil.APPLICATION_SD_DIRECTORY);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_CACHE);
		if(!myDir.exists())
			myDir.mkdir();
		File faviconDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_FAVICON);
		File imagesDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_IMAGES);
		if(!faviconDir.exists())
			faviconDir.mkdir();
		if(!imagesDir.exists())
			imagesDir.mkdir();
		List<File> cacheDir2Monitor = new ArrayList<File>();
		cacheDir2Monitor.add(faviconDir);
		cacheDir2Monitor.add(imagesDir);
		return cacheDir2Monitor;
	}
	
	private void processCache() {
		Log.v("Launchpet2", "Running cachecleanupservice...");
		List<File> cacheDir2Monitor = getDirectory2Monitor();
		Calendar todaysCalendar = Calendar.getInstance();
		todaysCalendar.setTime(new Date());
		for(File file : cacheDir2Monitor) {
			for(File childFile : file.listFiles()) {
				long lastModified = childFile.lastModified();
				Calendar modifiedCalendar = Calendar.getInstance();
				modifiedCalendar.setTimeInMillis(lastModified);
				modifiedCalendar.add(Calendar.DATE, ConfigurationUtil.CACHE_MAX_DAYS);
				if(modifiedCalendar.before(todaysCalendar)) {
					childFile.delete();
				}
			}
		}
	}
	
}
