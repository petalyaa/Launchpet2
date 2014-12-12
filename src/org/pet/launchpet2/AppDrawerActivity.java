package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pet.launchpet2.model.GroupApps;
import org.pet.launchpet2.model.HiddenApplicationItem;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.thread.FetchLocalImageAsync;
import org.pet.launchpet2.util.BitmapUtil;
import org.pet.launchpet2.util.ConfigurationUtil;
import org.pet.launchpet2.util.DialogUtil;
import org.pet.launchpet2.util.StringUtil;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AppDrawerActivity extends FragmentActivity {

	private ViewPager mAppsViewPager;
	
	private PagerAdapter mAppsPagerAdapter;
	
	private LinearLayout mIndicatorHolder;
	
	private int toolbarColor = Color.BLACK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_drawer);
		
		Bundle bundle = getIntent().getExtras();
		toolbarColor = bundle.getInt("toolbarColor");

		mAppsViewPager = (ViewPager) findViewById(R.id.apps_view_pager);
		mIndicatorHolder = (LinearLayout) findViewById(R.id.apps_view_pager_indicator);
		
		List<LauncherApplication> launcherAppsList = getLauncherApplication();
		if(launcherAppsList != null && launcherAppsList.size() > 0) {
			mAppsViewPager.setPageMargin(100);
			mAppsPagerAdapter = new AppsDrawerPagerAdapter(getSupportFragmentManager(), launcherAppsList);
			mAppsViewPager.setAdapter(mAppsPagerAdapter);
			updateDrawerPagingIndicator(0);
			mAppsViewPager.setOnPageChangeListener(new OnAppDrawerPageChangeListener());
		} else {
			finish();
		}
	}
	
	private void updateDrawerPagingIndicator(int page) {
		int count = mAppsPagerAdapter.getCount();
		mIndicatorHolder.removeAllViews();
		for(int i = 0; i < count; i++) {
			ImageView indicatorIcon = new ImageView(getApplicationContext());
			if(page == i)
				indicatorIcon.setBackground(getDrawable(R.drawable.drawer_circle_selected));
			else
				indicatorIcon.setBackground(getDrawable(R.drawable.drawer_circle_idle));
			int circleSize = (int) getResources().getDimension(R.dimen.activity_native_app_drawer_pager_indicator_size);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(circleSize, circleSize);
			layoutParams.setMarginStart(10);
			layoutParams.setMarginEnd(10);
			indicatorIcon.setLayoutParams(layoutParams);
			mIndicatorHolder.addView(indicatorIcon);
		}
	}
	
	private List<LauncherApplication> getLauncherApplication() {
		List<LauncherApplication> launcherAppsList = new ArrayList<LauncherApplication>();
		List<HiddenApplicationItem> itemList = getExistingHiddenApps();
		List<String> packageNameList = new ArrayList<String>();
		if(itemList != null && itemList.size() > 0) {
			for(HiddenApplicationItem item : itemList) {
				packageNameList.add(item.getPackageName());
			}
		}
		launcherAppsList.clear();
		List<GroupApps> groupList = getExistingGroupApps();
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
		
		PackageManager pm = getPackageManager();
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
	
	private List<GroupApps> getExistingGroupApps() {
		List<GroupApps> groupAppsList = new ArrayList<GroupApps>();
		SharedPreferences prefs = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_APPS_GROUP_SETTINGS, Context.MODE_PRIVATE);
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
	
	private int getDrawerColCount() {
		return ConfigurationUtil.NATIVE_DRAWER_COL_COUNT;
	}
	
	private int getDrawerRowCount() {
		return ConfigurationUtil.NATIVE_DRAWER_ROW_COUNT;
	}
	
	private List<LauncherApplication> getFavoriteApplicationList() {
		List<LauncherApplication> appList = new ArrayList<LauncherApplication>();
		SharedPreferences pref = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_KEY_APPLICATION_SETTING, MODE_PRIVATE);
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
	
	public void writeFavoriteApplicationList(List<LauncherApplication> favAppList) {
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
		SharedPreferences pref = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_KEY_APPLICATION_SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_KEY_FAVORITE, jsonStr);
		editor.commit();
	}
	
	private void addApplicationAsFavorite(LauncherApplication app) {
		List<LauncherApplication> existingFavAppList = getFavoriteApplicationList();
		if(existingFavAppList != null && existingFavAppList.size() > 0) {
			for(LauncherApplication existingApp : existingFavAppList) {
				String existingPackageName = existingApp.getPackageName();
				if(app.getPackageName().equals(existingPackageName)) {
					Toast.makeText(getApplicationContext(), getString(R.string.error_already_in_favorite), Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		if(existingFavAppList == null)
			existingFavAppList = new ArrayList<LauncherApplication>();
		if(existingFavAppList.size() >= ConfigurationUtil.MAX_FAVORITE) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_max_fav_reach), Toast.LENGTH_SHORT).show();
			return;
		}
		existingFavAppList.add(app);
		writeFavoriteApplicationList(existingFavAppList);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void deleteGroup(final LauncherApplication app) {
		DialogUtil.createConfirmDialog(AppDrawerActivity.this, getString(R.string.confirm), getString(R.string.confirm_delete_group), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String appName = app.getName();
				
			}
		}).show();
	}

	private class AppsDrawerPagerAdapter extends FragmentStatePagerAdapter {
		
		private List<LauncherApplication> launcherApplicationList;
		
		private int appsPerPage = getDrawerColCount() * getDrawerRowCount();
		
		public AppsDrawerPagerAdapter(FragmentManager fm, List<LauncherApplication> launcherApplicationList) {
			super(fm);
			this.launcherApplicationList = launcherApplicationList;
		}

		@Override
		public Fragment getItem(int position) {
			List<LauncherApplication> appList = new ArrayList<LauncherApplication>();
			for(int i = 0; i < appsPerPage; i++) {
				int currentIndex = (appsPerPage * position) + i;
				try {
					LauncherApplication app = launcherApplicationList.get(currentIndex);
					appList.add(app);
				} catch (Exception e) {
				}
			}
			Log.v("Launchpet2", "position " + position + " has " + appList.size() + " applications");
			return new AppsSlidePageFragment(appList);
		}

		@Override
		public int getCount() {
			int page = launcherApplicationList.size() / appsPerPage;
			int balance = launcherApplicationList.size() % appsPerPage;
			if(balance > 0)
				page++;
			return page;
		}
	}
	
	private class AppsSlidePageFragment extends Fragment {
		
		private List<LauncherApplication> appList;
		
		public AppsSlidePageFragment(List<LauncherApplication> appList) {
			this.appList = appList;
		}
		
		@SuppressLint("InflateParams")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.native_apps_drawer, container, false);
			GridView appsGridLayout = (GridView) view.findViewById(R.id.native_apps_drawer_grid_content);
			appsGridLayout.setNumColumns(getDrawerColCount());
			AppsGridAdapter adapter = new AppsGridAdapter(appList);
			appsGridLayout.setAdapter(adapter);
			appsGridLayout.setOnItemClickListener(new OnAppsItemClickListener(appList));
			appsGridLayout.setOnItemLongClickListener(new OnAppsItemLongClickListener(appList));
			return view;
		}
		
	}
	
	private class OnAppsItemClickListener implements OnItemClickListener {
		
		private List<LauncherApplication> appList;
		
		public OnAppsItemClickListener(List<LauncherApplication> appList) {
			this.appList = appList;
		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			LauncherApplication app = appList.get(position);
			if(app.getType() == LauncherApplication.Type.APPLICATION) {
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(app.getPackageName());
				startActivity(LaunchIntent);
			} else {
				Intent intentFolder = new Intent(getApplicationContext(), FolderDrawerOpenActivity.class);
				intentFolder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentFolder.putExtra("group", app);
				intentFolder.putExtra("toolbarColor", toolbarColor);
				startActivity(intentFolder);
			}
		}
		
	}
	
	private class OnAppsItemLongClickListener implements OnItemLongClickListener {

		private List<LauncherApplication> appList;
		
		public OnAppsItemLongClickListener(List<LauncherApplication> appList) {
			this.appList = appList;
		}
		
		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
			final LauncherApplication app = appList.get(position);
			if(app.getType() == LauncherApplication.Type.APPLICATION) {
				String[] items = {getString(R.string.application_add_favorite), getString(R.string.application_details), getString(R.string.application_uninstall)};
				DialogUtil.createSelectDialogItem(AppDrawerActivity.this, items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which) {
						case 0 :
							addApplicationAsFavorite(app);
							Intent returnIntent = new Intent();
							returnIntent.putExtra("result", ConfigurationUtil.RESULT_RELOAD_FAVORITE);
							setResult(RESULT_OK,returnIntent);
							finish();
							break;
						case 1 : 
							try {
								Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								intent.setData(Uri.parse("package:" + app.getPackageName()));
								startActivity(intent);
							} catch ( ActivityNotFoundException e ) {
								Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
								startActivity(intent);
							}
							break;
						case 2 :
							Intent intent = new Intent(Intent.ACTION_DELETE);
							intent.setData(Uri.parse("package:" + app.getPackageName()));
							startActivity(intent);
							break;
						}
					}
				}).show();
			} else {
				String[] items = {getString(R.string.delete)};
				DialogUtil.createSelectDialogItem(AppDrawerActivity.this, items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0 :
							deleteGroup(app);
							break;
						}
					}
				}).show();
			}
			return true;
		}
		
	}
	
	private class AppsGridAdapter extends BaseAdapter {
		
		private List<LauncherApplication> appList;
		
		public AppsGridAdapter(List<LauncherApplication> appList) {
			this.appList = appList;
		}

		@Override
		public int getCount() {
			return appList.size();
		}

		@Override
		public Object getItem(int position) {
			return getCount() > 0 ? appList.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder = null;
			if(view == null) {
				LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.native_drawer_icon_view, parent, false);
				holder = new ViewHolder();
				holder.native_drawer_icon_view_icon = (ImageView) view.findViewById(R.id.native_drawer_icon_view_icon);
				holder.native_drawer_icon_view_label = (TextView) view.findViewById(R.id.native_drawer_icon_view_label);
				holder.native_drawer_icon_folder = (RelativeLayout) view.findViewById(R.id.native_drawer_icon_folder);
				holder.folder_view = inflater.inflate(R.layout.apps_folder_view, parent, false);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			LauncherApplication app = (LauncherApplication) getItem(position);
			holder.native_drawer_icon_view_label.setText(app.getName());
			if(app.getType() == LauncherApplication.Type.APPLICATION) {
				holder.native_drawer_icon_view_icon.setVisibility(View.VISIBLE);
				holder.native_drawer_icon_folder.setVisibility(View.INVISIBLE);
				new FetchLocalImageAsync(getApplicationContext(), holder.native_drawer_icon_view_icon).execute(app.getPackageName());
			} else {
				holder.native_drawer_icon_view_icon.setVisibility(View.INVISIBLE);
				holder.native_drawer_icon_folder.setVisibility(View.VISIBLE);
				View folderView = holder.folder_view;
				List<LauncherApplication> groupAppList = app.getGroupAppList();
				if(groupAppList != null && groupAppList.size() > 0) {
					Context context = getApplicationContext();
					for(int i = 0; i < ConfigurationUtil.FOLDER_ICON_STACK_LIMIT; i++) {
						int nameResourceID = context.getResources().getIdentifier("folder_view_icon_" + i, "id", context.getApplicationInfo().packageName);
						ImageView thisImageView = (ImageView) folderView.findViewById(nameResourceID);
						try {
							LauncherApplication groupApp = groupAppList.get(i);
							String packageName = groupApp.getPackageName();
//							Bitmap thisAppBmp = BitmapUtil.getBitmapFromPackage(context, packageName);
//							thisImageView.setImageBitmap(thisAppBmp);
							new FetchLocalImageAsync(context, thisImageView).execute(packageName);
						} catch (Exception e) {
							thisImageView.setVisibility(View.INVISIBLE);
						}
					}
				}
				holder.native_drawer_icon_folder.addView(holder.folder_view);
			}
			return view;
		}
		
		private class ViewHolder {
			ImageView native_drawer_icon_view_icon;
			TextView native_drawer_icon_view_label;
			RelativeLayout native_drawer_icon_folder;
			View folder_view;
		}
		
	}
	
	private class OnAppDrawerPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int page) {
			updateDrawerPagingIndicator(page);
		}
		
	}

}
