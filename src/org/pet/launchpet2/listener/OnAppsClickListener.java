package org.pet.launchpet2.listener;

import java.util.List;

import org.pet.launchpet2.FolderDrawerOpenActivity;
import org.pet.launchpet2.model.LauncherApplication;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnAppsClickListener implements OnItemClickListener {
	
	private List<LauncherApplication> appList;
	
	private Activity activity;
	
	private int toolbarColor;
	
	public OnAppsClickListener(Activity activity, int toolbarColor, List<LauncherApplication> appList) {
		this.appList = appList;
		this.activity = activity;
		this.toolbarColor = toolbarColor;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		LauncherApplication app = appList.get(position);
		if(app.getType() == LauncherApplication.Type.APPLICATION) {
			Intent LaunchIntent = activity.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
			activity.startActivity(LaunchIntent);
			activity.finish();
		} else {
			Intent intentFolder = new Intent(activity.getApplicationContext(), FolderDrawerOpenActivity.class);
			intentFolder.putExtra("group", app);
			intentFolder.putExtra("toolbarColor", toolbarColor);
			activity.startActivityForResult(intentFolder, 1);
		}
	}
	
}