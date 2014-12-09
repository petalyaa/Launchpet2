package org.pet.launchpet2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ApplicationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
			
		}
	}

}
