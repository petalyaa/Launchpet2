package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pet.launchpet2.adapter.HiddenApplicationListAdapter;
import org.pet.launchpet2.listener.OnSettingBackButtonClickListener;
import org.pet.launchpet2.model.HiddenApplicationItem;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class HiddenApplicationActivity extends Activity {
	
	private ImageButton mAddButton;
	
	private ImageButton mBackButton;
	
	private ListView mAppListView;
	
	private HiddenApplicationListAdapter mAdapter;
	
	private List<HiddenApplicationItem> hiddenApplicationItemList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hidden_application);
		
		mAddButton = (ImageButton) findViewById(R.id.hidden_apps_toolbar_add_btn);
		mBackButton = (ImageButton) findViewById(R.id.hidden_apps_toolbar_back_btn);
		mAppListView = (ListView) findViewById(R.id.hidden_apps_list);
		mAddButton.setOnClickListener(new OnAddHiddenApplicationButtonClickListener());
		mBackButton.setOnClickListener(new OnSettingBackButtonClickListener(this));
		List<String> existingHiddenPackageList = new ArrayList<String>();
		List<HiddenApplicationItem> existingItemList = getExistingHiddenApps();
		for(HiddenApplicationItem tmpItem : existingItemList) {
			existingHiddenPackageList.add(tmpItem.getPackageName());
		}
		hiddenApplicationItemList = new ArrayList<HiddenApplicationItem>();
		PackageManager pm = getPackageManager();
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		//get a list of installed apps.
		List<ResolveInfo> availableActivities = pm.queryIntentActivities(i, 0);
		for(ResolveInfo ri:availableActivities){
			HiddenApplicationItem item = new HiddenApplicationItem();
			String packageName = ri.activityInfo.packageName;
			if(existingHiddenPackageList.contains(packageName))
				continue;
			item.setName(ri.loadLabel(pm).toString());
			item.setPackageName(packageName);
			hiddenApplicationItemList.add(item);
		}
		Collections.sort(hiddenApplicationItemList);
		mAdapter = new HiddenApplicationListAdapter(getApplicationContext(), existingItemList);
		mAppListView.setAdapter(mAdapter);
	}
	
	public void populateMainList() {
		List<HiddenApplicationItem> existingItemList = getExistingHiddenApps();
		mAdapter.setDataList(existingItemList);
		mAdapter.notifyDataSetChanged();
		mAppListView.setAdapter(mAdapter);
	}
	
	public void addHiddenApplication(HiddenApplicationItem item) {
		Iterator<HiddenApplicationItem> itemIter = hiddenApplicationItemList.iterator();
		while(itemIter.hasNext()) {
			HiddenApplicationItem tmpItem = itemIter.next();
			String packageName = tmpItem.getPackageName();
			if(item.getPackageName().equals(packageName)) 
				itemIter.remove();
		}
		List<HiddenApplicationItem> existingItemList = getExistingHiddenApps();
		existingItemList.add(item);
		JSONArray jsonArr = new JSONArray();
		if(existingItemList != null && existingItemList.size() > 0) {
			for(HiddenApplicationItem existingItem : existingItemList) {
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj.put("name", existingItem.getName());
					jsonObj.put("package", existingItem.getPackageName());
					jsonArr.put(jsonObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		String jsonStr = jsonArr.toString();
		SharedPreferences pref = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_HIDDEN_APPS_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_EXISTING_HIDDEN_APPS_KEY, jsonStr);
		editor.commit();
	}
	
	public List<HiddenApplicationItem> getExistingHiddenApps() {
		SharedPreferences pref = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_HIDDEN_APPS_SETTINGS, Context.MODE_PRIVATE);
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
	
	private class OnAddHiddenApplicationButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(hiddenApplicationItemList != null && hiddenApplicationItemList.size() > 0) {
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(HiddenApplicationActivity.this);
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HiddenApplicationActivity.this, android.R.layout.select_dialog_singlechoice);
				for(HiddenApplicationItem item : hiddenApplicationItemList) {
					String name = item.getName();
					arrayAdapter.add(name);
				}
				builderSingle.setNegativeButton(getString(R.string.button_close), null);
				builderSingle.setAdapter(arrayAdapter, new OnSelectionClick());
				builderSingle.show();
			}
		}
		
	}
	
	private class OnSelectionClick implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(hiddenApplicationItemList != null && hiddenApplicationItemList.size() > 0) {
				HiddenApplicationItem item = hiddenApplicationItemList.get(which);
				addHiddenApplication(item);
				populateMainList();
			}
		}
		
	}

}
