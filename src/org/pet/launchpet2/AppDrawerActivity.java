package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.List;

import org.pet.launchpet2.adapter.AppsGridAdapter;
import org.pet.launchpet2.animation.ZoomOutViewPagerTransformer;
import org.pet.launchpet2.listener.Callback;
import org.pet.launchpet2.listener.OnAppsClickListener;
import org.pet.launchpet2.listener.OnAppsLongClickListener;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.util.ApplicationUtil;
import org.pet.launchpet2.util.CommonUtil;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AppDrawerActivity extends FragmentActivity {

	private ViewPager mAppsViewPager;
	
	private PagerAdapter mAppsPagerAdapter;
	
	private LinearLayout mIndicatorHolder;
	
	private int toolbarColor = Color.BLACK;

	private BroadcastReceiver mHomeKeyReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_drawer);
		
		Bundle bundle = getIntent().getExtras();
		toolbarColor = bundle.getInt("toolbarColor");
		
		mHomeKeyReceiver = new HomeKeyBroadcastReceiver();
		IntentFilter filter = new IntentFilter(Intent.CATEGORY_HOME);
		registerReceiver(mHomeKeyReceiver, filter);

		mAppsViewPager = (ViewPager) findViewById(R.id.apps_view_pager);
		mIndicatorHolder = (LinearLayout) findViewById(R.id.apps_view_pager_indicator);
		
//		List<LauncherApplication> launcherAppsList = CommonUtil.deserializeLauncherApps();
//		if(launcherAppsList != null && launcherAppsList.size() > 0) {
//			populateDrawerPage(launcherAppsList);
//		}
		new FetchApplication().execute("");
	}
	
	private void populateDrawerPage(List<LauncherApplication> launcherAppsList) {
		mAppsViewPager.setPageMargin(100);
		mAppsPagerAdapter = new AppsDrawerPagerAdapter(getSupportFragmentManager(), launcherAppsList);
		mAppsViewPager.setAdapter(mAppsPagerAdapter);
		updateDrawerPagingIndicator(0);
		mAppsViewPager.setOnPageChangeListener(new OnAppDrawerPageChangeListener());
		mAppsViewPager.setPageTransformer(true, new ZoomOutViewPagerTransformer());
	}
	
	private void closeDrawer() {
		finish();
		AppDrawerActivity.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		AppDrawerActivity.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}

	private void updateDrawerPagingIndicator(int page) {
		int count = mAppsPagerAdapter.getCount();
		mIndicatorHolder.removeAllViews();
		for(int i = 0; i < count; i++) {
			ImageView indicatorIcon = new ImageView(getApplicationContext());
			if(page == i)
				indicatorIcon.setBackground(getDrawable(R.drawable.drawer_circle_selected));
			else
				indicatorIcon.setBackground(getDrawable(R.drawable.drawer_circle_idle));
			int circleSize = (int) getResources().getDimension(R.dimen.activity_native_app_drawer_pager_indicator_size);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(circleSize, circleSize);
			layoutParams.setMarginStart(10);
			layoutParams.setMarginEnd(10);
			indicatorIcon.setLayoutParams(layoutParams);
			mIndicatorHolder.addView(indicatorIcon);
		}
	}
	
	private int getDrawerColCount() {
		return ConfigurationUtil.NATIVE_DRAWER_COL_COUNT;
	}
	
	private int getDrawerRowCount() {
		return ConfigurationUtil.NATIVE_DRAWER_ROW_COUNT;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            int result = imageReturnedIntent.getIntExtra("result", 0);
	            if(result == 1) {
	            	finish();
	            }
	        }
	        if (resultCode == RESULT_CANCELED) {
	        }
	    }
	}

	private void reloadDrawer() {
		Intent i = getIntent();
		finish();
		startActivity(i);
	}
	
	private class FetchApplication extends AsyncTask<String, Void, List<LauncherApplication>> {

		@Override
		protected List<LauncherApplication> doInBackground(String... params) {
			return ApplicationUtil.getLauncherApplication(AppDrawerActivity.this);
		}

		@Override
		protected void onPostExecute(List<LauncherApplication> result) {
			if(result != null && result.size() > 0) {
				populateDrawerPage(result);
				//CommonUtil.serializeApplicationobjectList(result);
			} else {
				closeDrawer();
			}
			
		}
		
	}

	private class AppsDrawerPagerAdapter extends FragmentStatePagerAdapter {
		
		private List<LauncherApplication> launcherApplicationList;
		
		private int appsPerPage = getDrawerColCount() * getDrawerRowCount();
		
		public AppsDrawerPagerAdapter(FragmentManager fm, List<LauncherApplication> launcherApplicationList) {
			super(fm);
			this.launcherApplicationList = launcherApplicationList;
		}

		@Override
		public Fragment getItem(int position) {
			List<LauncherApplication> appList = new ArrayList<LauncherApplication>();
			for(int i = 0; i < appsPerPage; i++) {
				int currentIndex = (appsPerPage * position) + i;
				try {
					LauncherApplication app = launcherApplicationList.get(currentIndex);
					appList.add(app);
				} catch (Exception e) {
				}
			}
			Log.v("Launchpet2", "position " + position + " has " + appList.size() + " applications");
			return new AppsSlidePageFragment(appList);
		}

		@Override
		public int getCount() {
			int page = launcherApplicationList.size() / appsPerPage;
			int balance = launcherApplicationList.size() % appsPerPage;
			if(balance > 0)
				page++;
			return page;
		}
	}
	
	private class AppsSlidePageFragment extends Fragment {
		
		private List<LauncherApplication> appList;
		
		public AppsSlidePageFragment(List<LauncherApplication> appList) {
			this.appList = appList;
		}
		
		@SuppressLint("InflateParams")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.native_apps_drawer, container, false);
			GridView appsGridLayout = (GridView) view.findViewById(R.id.native_apps_drawer_grid_content);
			appsGridLayout.setNumColumns(getDrawerColCount());
			AppsGridAdapter adapter = new AppsGridAdapter(getApplicationContext(), appList);
			appsGridLayout.setAdapter(adapter);
			appsGridLayout.setOnItemClickListener(new OnAppsClickListener(AppDrawerActivity.this, toolbarColor, appList));
			appsGridLayout.setOnItemLongClickListener(new OnAppsLongClickListener(AppDrawerActivity.this, appList, new Callback() {
				
				@Override
				public void performCallback() {
					reloadDrawer();
				}
			}));
			return view;
		}
		
	}
	
	private class OnAppDrawerPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int page) {
			updateDrawerPagingIndicator(page);
		}
		
	}
	
	private class HomeKeyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			Log.v("Launchpet2", "Action : " + action);
		}
 		
	}

}
