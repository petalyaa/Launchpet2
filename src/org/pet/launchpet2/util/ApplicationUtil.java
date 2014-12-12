package org.pet.launchpet2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pet.launchpet2.R;
import org.pet.launchpet2.listener.Callback;
import org.pet.launchpet2.model.GroupApps;
import org.pet.launchpet2.model.HiddenApplicationItem;
import org.pet.launchpet2.model.LauncherApplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

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
	
	public static final void writeGroupList(Activity activity, List<GroupApps> groupAppsList) {
		JSONArray jsonArray = new JSONArray();
		if(groupAppsList != null && groupAppsList.size() > 0) {
			for(GroupApps group : groupAppsList) {
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj.put("name", group.getName());
					JSONArray launcherAppJsonArr = new JSONArray();
					List<LauncherApplication> launcherAppsList = group.getAppList();
					if(launcherAppsList != null && launcherAppsList.size() > 0) {
						for(LauncherApplication app : launcherAppsList) {
							JSONObject appJsonObj = new JSONObject();
							appJsonObj.put("name", app.getName());
							appJsonObj.put("package", app.getPackageName());
							launcherAppJsonArr.put(appJsonObj);
						}
					}
					jsonObj.put("appList", launcherAppJsonArr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jsonArray.put(jsonObj);
			}
		}
		String jsonStr = jsonArray.toString();
		SharedPreferences prefs = activity.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS_LIST_KEY, jsonStr);
		editor.commit();
	}
	
	public static final void deleteGroup(final Activity activity, final LauncherApplication app, final Callback callback) {
		final List<GroupApps> thisItems = getExistingGroupApps(activity);
		DialogUtil.createConfirmDialog(activity, activity.getString(R.string.confirm), activity.getString(R.string.confirm_delete_group), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String appName = app.getName();
				Iterator<GroupApps> itemIter1 = thisItems.iterator();
				while(itemIter1.hasNext()) {
					GroupApps thisItem = itemIter1.next();
					String name = thisItem.getName();
					if(name.equals(appName))
						itemIter1.remove();
				}
				writeGroupList(activity, thisItems);
				if(callback != null) 
					callback.performCallback();
			}
		}).show();
	}
	
	public static final List<LauncherApplication> getFavoriteApplicationList(Activity activity) {
		List<LauncherApplication> appList = new ArrayList<LauncherApplication>();
		SharedPreferences pref = activity.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_KEY_APPLICATION_SETTING, Context.MODE_PRIVATE);
		String jsonStr = pref.getString(ConfigurationUtil.SHARED_PREFERENCE_KEY_FAVORITE, "");
		if(!StringUtil.isNullEmptyString(jsonStr)) {
			try {
				JSONArray jsonArr = new JSONArray(jsonStr);
				if(jsonArr != null && jsonArr.length() > 0) {
					for(int i = 0; i < jsonArr.length(); i++) {
						JSONObject jsonObj = jsonArr.getJSONObject(i);
						LauncherApplication app = new LauncherApplication();
						app.setName(jsonObj.getString("name"));
						app.setPackageName(jsonObj.getString("packageName"));
						app.setIconResId(jsonObj.getInt("iconResId"));
						appList.add(app);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return appList;
	}
	
	public static final void writeFavoriteApplicationList(Activity activity, List<LauncherApplication> favAppList) {
		JSONArray jsonArray = new JSONArray();
		for(LauncherApplication appList : favAppList) {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("name", appList.getName());
				jsonObj.put("packageName", appList.getPackageName());
				jsonObj.put("iconResId", appList.getIconResId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonArray.put(jsonObj);
		}
		String jsonStr = jsonArray.toString();
		SharedPreferences pref = activity.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_KEY_APPLICATION_SETTING, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_KEY_FAVORITE, jsonStr);
		editor.commit();
	}
	
	public static final void addApplicationAsFavorite(Activity activity, LauncherApplication app, final Callback callback) {
		List<LauncherApplication> existingFavAppList = getFavoriteApplicationList(activity);
		if(existingFavAppList != null && existingFavAppList.size() > 0) {
			for(LauncherApplication existingApp : existingFavAppList) {
				String existingPackageName = existingApp.getPackageName();
				if(app.getPackageName().equals(existingPackageName)) {
					Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.error_already_in_favorite), Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		if(existingFavAppList == null)
			existingFavAppList = new ArrayList<LauncherApplication>();
		if(existingFavAppList.size() >= ConfigurationUtil.MAX_FAVORITE) {
			Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.error_max_fav_reach), Toast.LENGTH_SHORT).show();
			return;
		}
		existingFavAppList.add(app);
		writeFavoriteApplicationList(activity, existingFavAppList);
		if(callback != null)
			callback.performCallback();
	}
	
	public static final List<LauncherApplication> getLauncherApplication(Activity activity) {
		List<LauncherApplication> launcherAppsList = new ArrayList<LauncherApplication>();
		List<HiddenApplicationItem> itemList = getExistingHiddenApps(activity);
		List<String> packageNameList = new ArrayList<String>();
		if(itemList != null && itemList.size() > 0) {
			for(HiddenApplicationItem item : itemList) {
				packageNameList.add(item.getPackageName());
			}
		}
		launcherAppsList.clear();
		List<GroupApps> groupList = getExistingGroupApps(activity);
		List<String> packageInGroup = new ArrayList<String>();
		if(groupList != null && groupList.size() > 0) {
			for(GroupApps group : groupList) {
				List<LauncherApplication> groupAppList = group.getAppList();
				if(groupAppList != null && groupAppList.size() > 0) {
					for(LauncherApplication item : groupAppList) {
						String packageName = item.getPackageName();
						packageInGroup.add(packageName);
					}
				}
				LauncherApplication launcherGroup = new LauncherApplication();
				launcherGroup.setType(LauncherApplication.Type.FOLDER);
				launcherGroup.setName(group.getName());
				launcherGroup.setGroupAppList(groupAppList);
				launcherAppsList.add(launcherGroup);
			}
		}
		
		PackageManager pm = activity.getPackageManager();
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		//get a list of installed apps.
		List<ResolveInfo> availableActivities = pm.queryIntentActivities(i, 0);
		for(ResolveInfo ri:availableActivities){
			LauncherApplication app = new LauncherApplication();
			String packageName = ri.activityInfo.packageName;
			if(packageNameList.contains(packageName) || packageInGroup.contains(packageName))
				continue;
			app.setPackageName(packageName);
			app.setName(ri.loadLabel(pm).toString());
			app.setType(LauncherApplication.Type.APPLICATION);
			launcherAppsList.add(app);
		}
		
		Collections.sort(launcherAppsList);
		return launcherAppsList;
	}
	
	public static final List<GroupApps> getExistingGroupApps(Activity activity) {
		List<GroupApps> groupAppsList = new ArrayList<GroupApps>();
		SharedPreferences prefs = activity.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS, Context.MODE_PRIVATE);
		String jsonStr = prefs.getString(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS_LIST_KEY, null);
		if(!StringUtil.isNullEmptyString(jsonStr)) {
			try {
				JSONArray jsonArr = new JSONArray(jsonStr);
				for(int i = 0; i < jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					String name = jsonObj.getString("name");
					JSONArray appList = jsonObj.getJSONArray("appList");
					List<LauncherApplication> launcherAppList = new ArrayList<LauncherApplication>(appList.length());
					List<String> packageList = new ArrayList<String>();
					for(int x = 0; x < appList.length(); x++) {
						JSONObject appJsonObj = appList.getJSONObject(x);
						LauncherApplication app = new LauncherApplication();
						String packageName = appJsonObj.getString("package");
						app.setName(appJsonObj.getString("name"));
						app.setPackageName(packageName);
						packageList.add(packageName);
						launcherAppList.add(app);
					}
					GroupApps item = new GroupApps();
					item.setName(name);
					item.setPackageList(packageList);
					item.setAppList(launcherAppList);
					groupAppsList.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return groupAppsList;
	}
	
	public static final List<HiddenApplicationItem> getExistingHiddenApps(Activity activity) {
		SharedPreferences pref = activity.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_HIDDEN_APPS_SETTINGS, Context.MODE_PRIVATE);
		String hiddenAppJsonStr = pref.getString(ConfigurationUtil.SHARED_PREFERENCE_EXISTING_HIDDEN_APPS_KEY, "");
		List<HiddenApplicationItem> itemList = new ArrayList<HiddenApplicationItem>();
		try {
			JSONArray hiddenAppJsonArr = new JSONArray(hiddenAppJsonStr);
			if(hiddenAppJsonArr != null && hiddenAppJsonArr.length() > 0) {
				for(int i = 0; i < hiddenAppJsonArr.length(); i++) {
					JSONObject hiddenAppJsonObj = hiddenAppJsonArr.getJSONObject(i);
					HiddenApplicationItem item = new HiddenApplicationItem();
					item.setHidden(true);
					item.setName(hiddenAppJsonObj.getString("name"));
					item.setPackageName(hiddenAppJsonObj.getString("package"));
					itemList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}

}
