package org.pet.launchpet2.settings.item;

import org.pet.launchpet2.HiddenApplicationActivity;
import org.pet.launchpet2.model.SettingAction;

import android.content.Context;
import android.content.Intent;

public class HiddenApplicationMenuItem implements SettingAction {
	
	private Context context;
	
	public HiddenApplicationMenuItem(Context context) {
		this.context = context;
	}

	@Override
	public void performClick() {
		Intent personalizedSettingIntent = new Intent(context, HiddenApplicationActivity.class);
		personalizedSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(personalizedSettingIntent);
	}

}
