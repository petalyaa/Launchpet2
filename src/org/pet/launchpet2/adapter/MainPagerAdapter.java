package org.pet.launchpet2.adapter;

import java.io.File;
import java.util.Calendar;

import org.pet.launchpet2.R;
import org.pet.launchpet2.listener.Callback;
import org.pet.launchpet2.util.BitmapUtil;
import org.pet.launchpet2.util.ConfigurationUtil;
import org.pet.launchpet2.util.StringUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

	private TextView mDateHeaderLabel;

	private ImageView mIconView;

	private TextView mNameDisplayLabel;

	private TextView mDayHeaderLabel;

	private Callback callback;

	private Context context;

	public MainPagerAdapter(Context context, FragmentManager fm,
			Callback callback) {
		super(fm);
		this.callback = callback;
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		return new MainPagerFragment(position);
	}

	@Override
	public int getCount() {
		return 2;
	}

	public void updateClock(String value) {
		setText(mDateHeaderLabel, value);
		if (!StringUtil.isNullEmptyString(value)) {
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			String dayStr = null;
			switch (day) {
			case Calendar.SUNDAY:
				dayStr = context.getString(R.string.day_sun);
				break;
			case Calendar.MONDAY:
				dayStr = context.getString(R.string.day_mon);
				break;
			case Calendar.TUESDAY:
				dayStr = context.getString(R.string.day_tue);
				break;
			case Calendar.WEDNESDAY:
				dayStr = context.getString(R.string.day_wed);
				break;
			case Calendar.THURSDAY:
				dayStr = context.getString(R.string.day_thu);
				break;
			case Calendar.FRIDAY:
				dayStr = context.getString(R.string.day_fri);
				break;
			case Calendar.SATURDAY:
				dayStr = context.getString(R.string.day_sat);
				break;
			}
			setText(mDayHeaderLabel, dayStr);
		}
	}

	public void updateDisplayName(String value) {
		Log.v("Launchpet2", "Value : " + value);
		setText(mNameDisplayLabel, value);
	}

	private void setText(TextView txtView, String value) {
		if (txtView != null) {
			if (StringUtil.isNullEmptyString(value))
				txtView.setVisibility(View.INVISIBLE);
			else {
				txtView.setVisibility(View.VISIBLE);
				txtView.setText(value);
			}
		}
	}

	private class MainPagerFragment extends Fragment {

		private int position;

		public MainPagerFragment(int position) {
			this.position = position;
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = null;
			if (position == 0) {
				view = inflater.inflate(R.layout.home_main_header, container,
						false);
				populateMainView(view);
			} else if (position == 1) {
				view = inflater.inflate(R.layout.home_secondary_header,
						container, false);
				populateSecondaryView(view);
			}
			return view;
		}

		private void populateMainView(View view) {
			Bitmap icon = null;
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/"
					+ ConfigurationUtil.APPLICATION_SD_DIRECTORY + "/"
					+ ConfigurationUtil.SUBDIRECTORY_MEDIA);
			if (!myDir.exists())
				return;
			File profileImageFile = new File(myDir,
					ConfigurationUtil.FILENAME_PROFILE_IMAGE);
			if (profileImageFile.exists())
				icon = BitmapUtil.getBitmapFromFile(profileImageFile);
			if (icon == null)
				icon = BitmapFactory.decodeResource(getResources(),
						R.drawable.launcher);
			icon = BitmapUtil.getRoundedCornerBitmap(icon, Color.WHITE, 5, 3,
					getActivity());
			mIconView = (ImageView) view.findViewById(R.id.profile_image);
			mDateHeaderLabel = (TextView) view
					.findViewById(R.id.date_header_label);
			mNameDisplayLabel = (TextView) view
					.findViewById(R.id.name_display_label);
			mDayHeaderLabel = (TextView) view
					.findViewById(R.id.day_header_label);
			mIconView.setImageBitmap(icon);
			if (callback != null)
				callback.performCallback();
		}

		private void populateSecondaryView(View view) {

		}

	}

}
