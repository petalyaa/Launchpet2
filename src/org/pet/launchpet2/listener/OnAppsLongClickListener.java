package org.pet.launchpet2.listener;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.util.ApplicationUtil;
import org.pet.launchpet2.util.ConfigurationUtil;
import org.pet.launchpet2.util.DialogUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OnAppsLongClickListener implements OnItemLongClickListener {

	private List<LauncherApplication> appList;
	
	private Activity activity;
	
	private Callback deleteCallback;
	
	public OnAppsLongClickListener(Activity activity, List<LauncherApplication> appList, Callback deleteCallback) {
		this.appList = appList;
		this.activity = activity;
		this.deleteCallback = deleteCallback;
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
		final LauncherApplication app = appList.get(position);
		if(app.getType() == LauncherApplication.Type.APPLICATION) {
			String[] items = {activity.getString(R.string.application_add_favorite), activity.getString(R.string.application_details), activity.getString(R.string.application_uninstall)};
			DialogUtil.createSelectDialogItem(activity, items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(which) {
					case 0 :
						ApplicationUtil.addApplicationAsFavorite(activity, app, null);
						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", ConfigurationUtil.RESULT_RELOAD_FAVORITE);
						activity.setResult(Activity.RESULT_OK,returnIntent);
						activity.finish();
						break;
					case 1 : 
						try {
							Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
							intent.setData(Uri.parse("package:" + app.getPackageName()));
							activity.startActivity(intent);
						} catch ( ActivityNotFoundException e ) {
							Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
							activity.startActivity(intent);
						}
						break;
					case 2 :
						Intent intent = new Intent(Intent.ACTION_DELETE);
						intent.setData(Uri.parse("package:" + app.getPackageName()));
						activity.startActivity(intent);
						break;
					}
				}
			}).show();
		} else {
			String[] items = {activity.getString(R.string.delete)};
			DialogUtil.createSelectDialogItem(activity, items, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0 :
						ApplicationUtil.deleteGroup(activity, app, new Callback() {
							
							@Override
							public void performCallback() {
								if(deleteCallback != null)
									deleteCallback.performCallback();
							}
						});
						break;
					}
				}
			}).show();
		}
		return true;
	}
	
}