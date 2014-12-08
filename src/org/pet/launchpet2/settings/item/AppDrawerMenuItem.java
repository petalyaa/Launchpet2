package org.pet.launchpet2.settings.item;

import org.pet.launchpet2.AppsDrawerSettingActivity;
import org.pet.launchpet2.model.SettingAction;

import android.content.Context;
import android.content.Intent;

public class AppDrawerMenuItem implements SettingAction {
	
	private Context context;
	
	public AppDrawerMenuItem(Context context) {
		this.context = context;
	}

	@Override
	public void performClick() {
		Intent intent = new Intent(context, AppsDrawerSettingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
