package org.pet.launchpet2;

import java.util.List;

import org.pet.launchpet2.listener.OnSettingBackButtonClickListener;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.thread.FetchLocalImageAsync;
import org.pet.launchpet2.util.ApplicationUtil;
import org.pet.launchpet2.util.DialogUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FolderDrawerOpenActivity extends Activity {
	
	private TextView mFolderName;
	
	private RelativeLayout mToolbar;
	
	private GridLayout mGridLayout;
	
	private ImageButton mBackImgBtn;
	
	private LauncherApplication app;
	
	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_folder_drawer_open);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		receiver = new ApplicationBroadcastReceiver();
		registerReceiver(receiver, filter);
		
		mFolderName = (TextView) findViewById(R.id.folder_drawer_name);
		mToolbar = (RelativeLayout) findViewById(R.id.folder_drawer_header);
		mGridLayout = (GridLayout) findViewById(R.id.folder_grid_view);
		mBackImgBtn = (ImageButton) findViewById(R.id.folder_drawer_close_icon);
		
		Intent thisIntent = getIntent();
		app = (LauncherApplication) thisIntent.getSerializableExtra("group");
		String name = app.getName();
		int toolbarColor = thisIntent.getIntExtra("toolbarColor", Color.GRAY);
		mFolderName.setText(name);
		mToolbar.setBackgroundColor(toolbarColor);
		mBackImgBtn.setOnClickListener(new OnSettingBackButtonClickListener(this));
		populateFolderContent();
	}
	
	private void populateFolderContent() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		List<LauncherApplication> groupAppList = app.getGroupAppList();
		if(groupAppList != null && groupAppList.size() > 0) {
			mGridLayout.removeAllViews();
			for(LauncherApplication groupApp : groupAppList) {
				String packageName = groupApp.getPackageName();
				if(!ApplicationUtil.isPackageExisted(getApplicationContext(), packageName))
					continue;
				View relLayout = inflater.inflate(R.layout.folder_item_app_view, mGridLayout, false);
				ImageView appIconView = (ImageView) relLayout.findViewById(R.id.folder_item_app_view_icon);
				TextView appTextView = (TextView) relLayout.findViewById(R.id.folder_item_app_view_label);
				appTextView.setText(groupApp.getName());
				new FetchLocalImageAsync(getApplicationContext(), appIconView).execute(packageName);
				mGridLayout.addView(relLayout);
				relLayout.setOnClickListener(new OnApplicationClickListener(groupApp));
				relLayout.setOnLongClickListener(new OnApplicationLongClickListener(groupApp));
			}
		} else {
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	private void showLongPressPopup(final LauncherApplication app) {
		String[] items = {getString(R.string.application_details), getString(R.string.application_uninstall)};
		DialogUtil.createSelectDialogItem(FolderDrawerOpenActivity.this, items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
//				case 0 :
//					addApplicationAsFavorite(app);
//					break;
				case 0 : 
					try {
						Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.parse("package:" + app.getPackageName()));
						startActivity(intent);
					} catch ( ActivityNotFoundException e ) {
						Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
						startActivity(intent);
					}
					break;
				case 1 :
					Intent intent = new Intent(Intent.ACTION_DELETE);
					intent.setData(Uri.parse("package:" + app.getPackageName()));
					startActivity(intent);
					break;
				}
			}
		}).show();
	}
	
	private class OnApplicationClickListener implements OnClickListener {
		
		private LauncherApplication app;
		
		public OnApplicationClickListener(LauncherApplication app) {
			this.app = app;
		}

		@Override
		public void onClick(View v) {
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app.getPackageName());
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(launchIntent);
		}
		
	}

	private class OnApplicationLongClickListener implements OnLongClickListener {
		
		private LauncherApplication app;
		
		public OnApplicationLongClickListener(LauncherApplication app) {
			this.app = app;
		}

		@Override
		public boolean onLongClick(View arg0) {
			showLongPressPopup(app);
			return true;
		}
		
	}
	
	private class ApplicationBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			populateFolderContent();
		}

	}
	
}
