package org.pet.launchpet2.listener;

import org.pet.launchpet2.model.Parameter;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class BrowserLinkOpenListener implements ActionPerformedListener {
	
	private Context context;
	
	public BrowserLinkOpenListener(Context context) {
		this.context = context;
	}

	@Override
	public void performAction(int signal, Parameter param) {
		if(signal == ConfigurationUtil.SIGNAL_OPEN_LINK_BROWSER) {
			String linkStr = param.get("url");
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkStr));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(browserIntent);
		}
	}

}
