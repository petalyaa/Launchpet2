package org.pet.launchpet2.listener;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class OnSettingBackButtonClickListener implements OnClickListener {
	
	private Activity activity;
	
	public OnSettingBackButtonClickListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		activity.finish();
	}

}
