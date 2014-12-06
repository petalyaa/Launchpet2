package org.pet.launchpet2.settings.item;

import org.pet.launchpet2.SocialSettingActivity;
import org.pet.launchpet2.model.SettingAction;

import android.content.Context;
import android.content.Intent;

public class SocialNetworkMenuItem implements SettingAction {
	
	private Context context;
	
	public SocialNetworkMenuItem(Context context) {
		this.context = context;
	}

	@Override
	public void performClick() {
		Intent intent = new Intent(context, SocialSettingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
