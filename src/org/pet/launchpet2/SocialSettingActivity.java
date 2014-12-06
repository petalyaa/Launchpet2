package org.pet.launchpet2;

import java.util.Arrays;
import java.util.List;

import org.pet.launchpet2.listener.OnSettingBackButtonClickListener;

import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.android.Facebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

public class SocialSettingActivity extends Activity {

	private ImageButton mBackButton;

	private ImageButton mAddButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_setting);

		mBackButton = (ImageButton) findViewById(R.id.social_setting_toolbar_back_btn);
		mAddButton = (ImageButton) findViewById(R.id.social_setting_toolbar_add_btn);

		mBackButton.setOnClickListener(new OnSettingBackButtonClickListener(this));
		mAddButton.setOnClickListener(new OnAddButtonClickListener());
	}

	private void loginToFacebook() {
	}

	private class OnAddButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(SocialSettingActivity.this);
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SocialSettingActivity.this, android.R.layout.select_dialog_singlechoice);
			arrayAdapter.add(getString(R.string.social_setting_facebook));
			arrayAdapter.add(getString(R.string.social_setting_twitter));
			builderSingle.setNegativeButton(getString(R.string.button_close), null);
			builderSingle.setAdapter(arrayAdapter, new OnSocialSettingSelect());
			builderSingle.show();
		}

	}

	private class OnSocialSettingSelect implements android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
			case 0 :
				loginToFacebook();
				break;
			}
		}

	}

}
