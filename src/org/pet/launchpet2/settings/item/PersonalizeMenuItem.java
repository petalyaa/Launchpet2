package org.pet.launchpet2.settings.item;

import org.pet.launchpet2.model.SettingAction;
import org.pet.launchpet2.preference.PersonalizeSettingActivity;

import android.content.Context;
import android.content.Intent;

public class PersonalizeMenuItem implements SettingAction  {
	
	private Context context;
	
	public PersonalizeMenuItem(Context context) {
		this.context = context;
	}

	@Override
	public void performClick() {
		Intent personalizedSettingIntent = new Intent(context, PersonalizeSettingActivity.class);
		personalizedSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(personalizedSettingIntent);
	}

}
