package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pet.launchpet2.adapter.GroupAppSettingListAdapter;
import org.pet.launchpet2.listener.OnSettingBackButtonClickListener;
import org.pet.launchpet2.model.GroupApps;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.util.ConfigurationUtil;
import org.pet.launchpet2.util.DialogUtil;
import org.pet.launchpet2.util.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupAppsSettingActivity extends Activity {
	
	private ImageButton mBackButton;
	
	private ImageButton mAddButton;
	
	private ListView mGroupListView;
	
	private GroupAppSettingListAdapter mGroupAppListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_apps_setting);
		
		mBackButton = (ImageButton) findViewById(R.id.group_apps_toolbar_back_btn);
		mAddButton = (ImageButton) findViewById(R.id.group_apps_toolbar_add_btn);
		mGroupListView = (ListView) findViewById(R.id.group_apps_list_view);
		
		List<GroupApps> items = new ArrayList<GroupApps>();
		mGroupAppListAdapter = new GroupAppSettingListAdapter(getApplicationContext(), items);
		mGroupListView.setAdapter(mGroupAppListAdapter);
		reloadMainList();
		mBackButton.setOnClickListener(new OnSettingBackButtonClickListener(this));
		mAddButton.setOnClickListener(new OnAddGroupSettingClickListener());
	}
	
	private List<GroupApps> getExistingGroupApps() {
		List<GroupApps> groupAppsList = new ArrayList<GroupApps>();
		SharedPreferences prefs = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS, Context.MODE_PRIVATE);
		String jsonStr = prefs.getString(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS_LIST_KEY, null);
		Log.v("Launchpet2", "jsonStr : " + jsonStr);
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
	
	private void addGroup(GroupApps group) {
		List<GroupApps> groupAppsList = getExistingGroupApps();
		if(groupAppsList == null)
			groupAppsList = new ArrayList<GroupApps>();
		if(groupAppsList != null && groupAppsList.size() > 0) {
			for(GroupApps tmpGroup : groupAppsList) {
				String name = tmpGroup.getName();
				if(group.getName().equals(name)) {
					Toast.makeText(getApplicationContext(), getString(R.string.error_group_name_exist), Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		groupAppsList.add(group);
		writeGroupList(groupAppsList);
	}
	
	private void addApplicationToGroup(String groupName, List<LauncherApplication> items) {
		List<GroupApps> groupAppsList = getExistingGroupApps();
		Iterator<GroupApps> groupAppIter = groupAppsList.iterator();
		while(groupAppIter.hasNext()) {
			GroupApps groupApp = groupAppIter.next();
			String name = groupApp.getName();
			if(name.equals(groupName)) {
				groupApp.setAppList(items);
			}
		}
		writeGroupList(groupAppsList);
	}
	
	private void writeGroupList(List<GroupApps> groupAppsList) {
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
		SharedPreferences prefs = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS_LIST_KEY, jsonStr);
		editor.commit();
		reloadMainList();
		ConfigurationUtil.setRequireReload(getApplicationContext());
	}
	
	private void reloadMainList() {
		List<GroupApps> groupAppsList = getExistingGroupApps();
		mGroupAppListAdapter.setItems(groupAppsList);
		mGroupAppListAdapter.notifyDataSetChanged();
		mGroupListView.setAdapter(mGroupAppListAdapter);
		mGroupListView.setOnItemLongClickListener(new OnGroupLongClickListener());
		mGroupListView.setOnItemClickListener(new OnGroupClickListener());
	}
	
	private void showRenameDialog(final GroupApps item) {
		DialogUtil.createInputTextDialog(this, item.getName(), getString(R.string.rename_to), new DialogUtil.DialogUtilCallback() {
			
			@Override
			public void onTextInputClickListener(String textInput) {
				List<GroupApps> thisItems = getExistingGroupApps();
				Iterator<GroupApps> itemIter = thisItems.iterator();
				while(itemIter.hasNext()) {
					GroupApps thisItem = itemIter.next();
					String name = thisItem.getName();
					if(name.equals(item.getName()))
						thisItem.setName(textInput);
				}
				writeGroupList(thisItems);
			}
		}).show();
	}
	
	private void showLongClickDialog(final GroupApps item) {
		String[] labels = {getString(R.string.delete), getString(R.string.rename)};
		DialogUtil.createSelectDialogItem(this, labels, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<GroupApps> thisItems = getExistingGroupApps();
				switch(which) {
				case 0 :
					Iterator<GroupApps> itemIter1 = thisItems.iterator();
					while(itemIter1.hasNext()) {
						GroupApps thisItem = itemIter1.next();
						String name = thisItem.getName();
						if(name.equals(item.getName()))
							itemIter1.remove();
					}
					writeGroupList(thisItems);
					break;
				case 1 :
					showRenameDialog(item);
					break;
				}
			}
		}).show();
	}
	
	private void onAddGroupClick() {
		DialogUtil.createInputTextDialog(this, null, getString(R.string.setting_input_group_name), new DialogUtil.DialogUtilCallback() {
			
			@Override
			public void onTextInputClickListener(String textInput) {
				GroupApps group = new GroupApps();
				group.setName(textInput);
				addGroup(group);
			}
		}).show();
	}
	
	private boolean isBelongToOther(String packageName, String groupName) {
		List<GroupApps> groupAppsList = getExistingGroupApps();
		boolean isBelongToOtherGroup = false;
		for(GroupApps groupApp : groupAppsList) {
			String thisGroupName = groupApp.getName();
			if(thisGroupName.equals(groupName))
				continue;
			List<LauncherApplication> launcherAppList = groupApp.getAppList();
			if(launcherAppList != null && launcherAppList.size() > 0) {
				for(LauncherApplication app : launcherAppList) {
					if(packageName.equals(app)) {
						isBelongToOtherGroup = true;
						break;
					}
				}
			}
			if(isBelongToOtherGroup)
				break;
		}
		return isBelongToOtherGroup;
	}
	
	private boolean isGroupFor(String packageName, String groupName) {
		List<GroupApps> groupAppsList = getExistingGroupApps();
		boolean isBelongToThisGroup = false;
		for(GroupApps groupApp : groupAppsList) {
			String thisGroupName = groupApp.getName();
			if(thisGroupName.equals(groupName)) {
				List<LauncherApplication> launcherAppList = groupApp.getAppList();
				if(launcherAppList != null && launcherAppList.size() > 0) {
					for(LauncherApplication app : launcherAppList) {
						if(packageName.equals(app.getPackageName())) {
							isBelongToThisGroup = true;
							break;
						}
					}
				}
			}
			if(isBelongToThisGroup) 
				break;
		}
		return isBelongToThisGroup;
	}
	
	private class OnAddGroupSettingClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			onAddGroupClick();
		}
		
	}
	
	private void showApplicationListDialog(final List<LauncherApplication> launcherAppList, final String groupName) {
		final boolean[] isSelected = new boolean[launcherAppList.size()];
		String[] sArr = new String[launcherAppList.size()];
		for(int i = 0; i < launcherAppList.size(); i++) {
			sArr[i] = launcherAppList.get(i).getName();
			isSelected[i] = launcherAppList.get(i).isInTheGroup();
		}
		DialogUtil.createMultiSelectDialogItem(this, groupName, sArr, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				isSelected[which] = isChecked;
			}
		}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				List<LauncherApplication> selectedApp = new ArrayList<LauncherApplication>();
				for(int i = 0; i < launcherAppList.size(); i++) {
					boolean isChecked = isSelected[i];
					if(isChecked) {
						selectedApp.add(launcherAppList.get(i));
					}
				}
				addApplicationToGroup(groupName, selectedApp);
			}
			
		}).show();
	}
	
	private class OnGroupClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			List<GroupApps> items = getExistingGroupApps();
			final GroupApps item = items.get(position);
			PackageManager pm = getPackageManager();
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> availableActivities = pm.queryIntentActivities(intent, 0);
			List<LauncherApplication> launcherAppList = new ArrayList<LauncherApplication>();
			for(ResolveInfo ri:availableActivities){
				String packageName = ri.activityInfo.packageName;
				if(!isBelongToOther(packageName, item.getName())) {
					LauncherApplication app = new LauncherApplication();
					app.setPackageName(packageName);
					app.setName(ri.loadLabel(pm).toString());
					if(isGroupFor(packageName, item.getName())) {
						app.setInTheGroup(true);
					}
					launcherAppList.add(app);
				}
			}
			Collections.sort(launcherAppList);
			showApplicationListDialog(launcherAppList, item.getName());
		}
		
	}
	
	private class OnGroupLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
			List<GroupApps> items = getExistingGroupApps();
			final GroupApps item = items.get(position);
			showLongClickDialog(item);
			return true;
		}

	}
	
}
