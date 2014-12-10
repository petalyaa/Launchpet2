package org.pet.launchpet2.preference;

import java.io.File;

import org.pet.launchpet2.R;
import org.pet.launchpet2.util.ConfigurationUtil;

import com.android.camera.CropImageIntentBuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PersonalizeSettingActivity extends PreferenceActivity {

	private static final int PICK_PROFILE_IMAGE = 100;

	private static final int PICK_BANNER_IMAGE = 200;

	private static final int SAVE_CROPPED_PROFILE_IMAGE = 300;

	private static final int SAVE_CROPPED_BANNER_IMAGE = 400;
	
	private ListPreference mThemePreference;

	private EditTextPreference mDateFormatPreference;

	private CheckBoxPreference mDisplayDateCheckbox;
	
	private CheckBoxPreference mQuickAccessHackCheckbox;

	private CheckBoxPreference mOverrideThemeColorCheckbox;

	private ColorPreference mNavbarColor;

	private ColorPreference mStatusBarColor;

	private ColorPreference mToolbarColor;

	private Preference mProfileImagePref;

	private Preference mBannerImagePref;
	
	private EditTextPreference mDisplayNamePref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PersonalizeSettingFragment()).commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && (requestCode == PICK_BANNER_IMAGE || requestCode == PICK_PROFILE_IMAGE)) {
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/" + ConfigurationUtil.APPLICATION_SD_DIRECTORY);
			if(!myDir.exists())
				myDir.mkdir();
			myDir = new File(myDir, "/" + ConfigurationUtil.SUBDIRECTORY_MEDIA);
			if(!myDir.exists())
				myDir.mkdir();
			Uri selectedImage = data.getData();
			if(requestCode == PICK_BANNER_IMAGE) {
				File outputFile = new File(myDir, ConfigurationUtil.FILENAME_BANNER_IMAGE);
				Uri outputUri = Uri.fromFile(outputFile);
				CropImageIntentBuilder cropImage = new CropImageIntentBuilder(ConfigurationUtil.DIMENSION_X_BANNER_IMAGE, ConfigurationUtil.DIMENSION_Y_BANNER_IMAGE, ConfigurationUtil.DIMENSION_X_BANNER_IMAGE, ConfigurationUtil.DIMENSION_Y_BANNER_IMAGE, outputUri);
				cropImage.setSourceImage(selectedImage);
				startActivityForResult(cropImage.getIntent(this), SAVE_CROPPED_BANNER_IMAGE);
			} else if (requestCode == PICK_PROFILE_IMAGE) {
				File outputFile = new File(myDir, ConfigurationUtil.FILENAME_PROFILE_IMAGE);
				Uri outputUri = Uri.fromFile(outputFile);
				CropImageIntentBuilder cropImage = new CropImageIntentBuilder(ConfigurationUtil.DIMENSION_X_PROFILE_IMAGE, ConfigurationUtil.DIMENSION_X_PROFILE_IMAGE, outputUri);
				cropImage.setOutputQuality(100);
				cropImage.setSourceImage(selectedImage);
				startActivityForResult(cropImage.getIntent(this), SAVE_CROPPED_PROFILE_IMAGE);
			}
		} else if (resultCode == RESULT_OK && (requestCode == SAVE_CROPPED_BANNER_IMAGE || requestCode == SAVE_CROPPED_PROFILE_IMAGE)) {
			
		}
		ConfigurationUtil.setRequireReload(getApplicationContext());
	}

	public class PersonalizeSettingFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_personalize, false);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.pref_personalize);

			mDisplayDateCheckbox = (CheckBoxPreference) getPreferenceManager().findPreference("personalize_general_display_date");
			mDateFormatPreference = (EditTextPreference) getPreferenceManager().findPreference("personalize_general_date_format");
			mOverrideThemeColorCheckbox = (CheckBoxPreference) getPreferenceManager().findPreference("personalize_color_override_default");
			mNavbarColor = (ColorPreference) getPreferenceManager().findPreference("personalize_color_navbar_color");
			mStatusBarColor = (ColorPreference) getPreferenceManager().findPreference("personalize_color_statusbar_color");
			mToolbarColor = (ColorPreference) getPreferenceManager().findPreference("personalize_color_toolbar_color");
			mProfileImagePref = (Preference) getPreferenceManager().findPreference("personalize_general_profile_image_selection");
			mBannerImagePref = (Preference) getPreferenceManager().findPreference("personalize_general_banner_image_selection");
			mThemePreference = (ListPreference) getPreferenceManager().findPreference("personalize_general_theme");
			mDisplayNamePref = (EditTextPreference) getPreferenceManager().findPreference("personalize_general_display_name");
			mQuickAccessHackCheckbox = (CheckBoxPreference) getPreferenceManager().findPreference("personalize_advanced_quick_access_hack");
			
			boolean isDateDisplay = mDisplayDateCheckbox.isChecked();
			if(!isDateDisplay)
				mDateFormatPreference.setEnabled(false);
			else
				mDateFormatPreference.setEnabled(true);
			mDisplayDateCheckbox.setOnPreferenceChangeListener(new OnDisplayDateCheckboxChange());
			mOverrideThemeColorCheckbox.setOnPreferenceChangeListener(new OnOverrideThemeColorChange());
			mProfileImagePref.setOnPreferenceClickListener(new OnProfileImagePreferenceClick());
			mBannerImagePref.setOnPreferenceClickListener(new OnBannerImagePreferenceClick());
			mThemePreference.setOnPreferenceChangeListener(new OnThemePreferenceChange());
			mDisplayNamePref.setOnPreferenceChangeListener(new OnDisplayNamePrefChange());
			mQuickAccessHackCheckbox.setOnPreferenceChangeListener(new OnQuickAccessPreferenceChange());
		}

	}
	
	private class OnDisplayNamePrefChange implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference arg0, Object arg1) {
			ConfigurationUtil.setRequireReload(getApplicationContext());
			return true;
		}
		
	}
	
	private class OnThemePreferenceChange implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference pref, Object newValue) {
			ConfigurationUtil.setRequireReload(getApplicationContext());
			return true;
		}
		
	}

	private class OnBannerImagePreferenceClick implements OnPreferenceClickListener {

		@Override
		public boolean onPreferenceClick(Preference pref) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_BANNER_IMAGE);
			return true;
		}

	}

	private class OnProfileImagePreferenceClick implements OnPreferenceClickListener {

		@Override
		public boolean onPreferenceClick(Preference pref) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PROFILE_IMAGE);
			return true;
		}

	}

	private class OnOverrideThemeColorChange implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			boolean isChecked = Boolean.valueOf(newValue.toString());
			if(isChecked) {
				mNavbarColor.setEnabled(true);
				mStatusBarColor.setEnabled(true);
				mToolbarColor.setEnabled(true);
			} else {
				mNavbarColor.setEnabled(false);
				mStatusBarColor.setEnabled(false);
				mToolbarColor.setEnabled(false);
			}
			ConfigurationUtil.setRequireReload(getApplicationContext());
			return true;
		}

	}
	
	private class OnQuickAccessPreferenceChange implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference arg0, Object newValue) {
			ConfigurationUtil.setRequireReload(getApplicationContext());
			return true;
		}
		
	}

	private class OnDisplayDateCheckboxChange implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			boolean isChecked = Boolean.valueOf(newValue.toString());
			if(isChecked) {
				mDateFormatPreference.setEnabled(true);
			} else {
				mDateFormatPreference.setEnabled(false);
			}
			ConfigurationUtil.setRequireReload(getApplicationContext());
			return true;
		}

	}
	
}
