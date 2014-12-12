package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.List;

import org.pet.launchpet2.fragment.LauncherFavoriteFragment;
import org.pet.launchpet2.fragment.LauncherHomeFragment;
import org.pet.launchpet2.fragment.LauncherNewsFragment;

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class LauncherActivity extends Activity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private RelativeLayout mLauncherMain;

	private WallpaperManager mWallpaperManager;
	
	private FloatingActionButton mLauncherFab;
	
	private static final List<LauncherHomeFragment> mLauncherHomeFragment = new ArrayList<LauncherHomeFragment>();
	static {
		mLauncherHomeFragment.add(new LauncherNewsFragment());
		mLauncherHomeFragment.add(new LauncherFavoriteFragment());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		
		mWallpaperManager = WallpaperManager.getInstance(this);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		mLauncherMain = (RelativeLayout) findViewById(R.id.launcher_main);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mLauncherFab = (FloatingActionButton) findViewById(R.id.launcher_fab);
		
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mLauncherMain.setBackground(mWallpaperManager.getDrawable());
		
		mLauncherFab.setOnClickListener(new OnFabBtnClickListener());
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mLauncherHomeFragment.get(position);
		}

		@Override
		public int getCount() {
			return mLauncherHomeFragment.size();
		}

	}
	
	private class OnFabBtnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), AppDrawerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		
	}

}
