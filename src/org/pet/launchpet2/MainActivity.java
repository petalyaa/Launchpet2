package org.pet.launchpet2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pet.launchpet2.adapter.ApplicationListAdapter;
import org.pet.launchpet2.adapter.MainPagerAdapter;
import org.pet.launchpet2.adapter.SettingListAdapter;
import org.pet.launchpet2.animation.viewpagertransformer.DepthViewPagerTransformer;
import org.pet.launchpet2.animation.viewpagertransformer.FlowViewPagerTransformer;
import org.pet.launchpet2.animation.viewpagertransformer.ZoomOutViewPagerTransformer;
import org.pet.launchpet2.layout.NowCardLayout;
import org.pet.launchpet2.layout.ObservableScrollView;
import org.pet.launchpet2.listener.BrowserLinkOpenListener;
import org.pet.launchpet2.listener.Callback;
import org.pet.launchpet2.listener.HideViewAnimationListener;
import org.pet.launchpet2.listener.OnCardTouchListener;
import org.pet.launchpet2.listener.ShowViewAnimationListener;
import org.pet.launchpet2.model.FeedData;
import org.pet.launchpet2.model.GroupApps;
import org.pet.launchpet2.model.HiddenApplicationItem;
import org.pet.launchpet2.model.HomeNewsItem;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.model.LauncherSettingItem;
import org.pet.launchpet2.model.RSSFeedSource;
import org.pet.launchpet2.model.FeedData.FeedSource;
import org.pet.launchpet2.model.HomeNewsItem.NewsType;
import org.pet.launchpet2.populator.DzoneCardPopulator;
import org.pet.launchpet2.populator.ImageCardPopulator;
import org.pet.launchpet2.populator.Populator;
import org.pet.launchpet2.populator.TextCardPopulator;
import org.pet.launchpet2.services.CacheCleanupService;
import org.pet.launchpet2.settings.item.AppDrawerMenuItem;
import org.pet.launchpet2.settings.item.FeedSourceMenuItem;
import org.pet.launchpet2.settings.item.PersonalizeMenuItem;
import org.pet.launchpet2.thread.WeatherServiceThread;
import org.pet.launchpet2.util.ApplicationUtil;
import org.pet.launchpet2.util.BitmapUtil;
import org.pet.launchpet2.util.CommonUtil;
import org.pet.launchpet2.util.ConfigurationUtil;
import org.pet.launchpet2.util.DialogUtil;
import org.pet.launchpet2.util.FBUtil;
import org.pet.launchpet2.util.StringUtil;
import org.pet.launchpet2.util.XMLParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.sromku.simple.fb.SimpleFacebook;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class MainActivity extends FragmentActivity implements ObservableScrollView.Callbacks {

	private static final String RSS_FEED_JSON_KEY = "rss";

	private static final String HEADER_DATE_FORMAT = "dd MMM yyyy";

	private static final SimpleDateFormat GENERIC_DATE_FORMAT = new SimpleDateFormat(HEADER_DATE_FORMAT, Locale.getDefault());

	private static final int TOOLBAR_ADJUSTER = 600;

	private static final int PROFILE_IMAGE_BORDER_SIZE = 2;

	private static final float BANNER_SPEED = 1.5f;

	private RelativeLayout mStickyView;

	private View mPlaceholderView;

	private ObservableScrollView mObservableScrollView;

	private RelativeLayout topHeaderImage;

	private ListView mAppListView;

	private LinearLayout mMainContent;

	private LayoutInflater inflater;

	private NowCardLayout nowCardLayout;

	private View applicationView;

	private View settingsView;

	private View homeView;

	private ListView mSettingListView;

	private SlidingMenu slidingMenu;

	private ImageView mRefreshButton;

	private ImageView mGoogleSearchBtn;

	private RelativeLayout mSecondaryProfileImageHolder;

	private ImageView mSecondaryProfileImage;

	private Animation mAnimSecondaryProfileShow;

	private Animation mAnimSecondaryProfileHide;

	private FloatingActionsMenu mFloatingFavButton;

	private SmoothProgressBar mLoadingProgressbar;

	private RelativeLayout mSettingToolbar;

	private RelativeLayout mApplicationToolbar;

	private static List<LauncherApplication> launcherAppsList = new ArrayList<LauncherApplication>();

	private boolean isSecondaryProfileImageVisible = false;

	private int navbarColor;

	private int dateTextColor;

	private int statusbarColor;

	private int toolbarColor;

	private int backgroundColor;

	private int cardTitleBackgroundColor;

	private int cardContentBackgroundColor;

	private int headerHolderBackgroundColor;

	private int headerImageOverlayColor;

	private int appTitleCircleColor;

	private RelativeLayout mHeaderHolder;

	private RelativeLayout mHeaderOverlay;

	private Timer timer;

	private static boolean isWeatherThreadStarted;

	private ViewPager mMainPager;

	private MainPagerAdapter mMainPagerAdapter;
	
	private LinearLayout mMainPagerIndicator;

	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addDataScheme("package");
		BroadcastReceiver receiver = new ApplicationBroadcastReceiver();
		registerReceiver(receiver, filter);
		registerReceiver(new ClockBroadcastReceiver(), new IntentFilter(Intent.ACTION_TIME_TICK));

		Intent mServiceIntent = new Intent(this, CacheCleanupService.class);
		startService(mServiceIntent);

		SimpleFacebook.setConfiguration(FBUtil.FB_CONFIGURATION);

		mObservableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
		mObservableScrollView.setCallbacks(this);

		mStickyView = (RelativeLayout) findViewById(R.id.top_toolbar);
		mPlaceholderView = findViewById(R.id.placeholder);
		topHeaderImage = (RelativeLayout) findViewById(R.id.top_header_image);

		mMainContent = (LinearLayout) findViewById(R.id.main_content);
		mRefreshButton = (ImageView) findViewById(R.id.top_toolbar_refresh_button);
		mGoogleSearchBtn = (ImageView) findViewById(R.id.top_toolbar_google_search_button);
		mSecondaryProfileImageHolder = (RelativeLayout) findViewById(R.id.floating_profile_image_holder);
		mSecondaryProfileImage = (ImageView) findViewById(R.id.secondary_profile_image);
		mLoadingProgressbar = (SmoothProgressBar) findViewById(R.id.toolbar_loading_progressbar);
		mFloatingFavButton = (FloatingActionsMenu) findViewById(R.id.floating_favorite_button);
		mHeaderHolder = (RelativeLayout) findViewById(R.id.top_header_image_holder);
		mHeaderOverlay = (RelativeLayout) findViewById(R.id.top_header_image_overlay);
		mMainPager = (ViewPager) findViewById(R.id.main_view_pager);
		mMainPagerIndicator = (LinearLayout) findViewById(R.id.main_view_pager_indicator);
		mMainPagerAdapter = new MainPagerAdapter(getApplicationContext(), getSupportFragmentManager(), new Callback() {
			
			@Override
			public void performCallback() {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
				reloadDate();
				reloadDisplayName(prefs);
			}
		});
		mMainPager.setAdapter(mMainPagerAdapter);
		updateDrawerPagingIndicator(0);
		mMainPager.setOnPageChangeListener(new OnAppDrawerPageChangeListener());
		mMainPager.setPageTransformer(true, new FlowViewPagerTransformer());

		mLoadingProgressbar.setVisibility(View.INVISIBLE);

		mAnimSecondaryProfileShow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_to_right);
		mAnimSecondaryProfileHide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_to_left);

		mSecondaryProfileImageHolder.setVisibility(View.INVISIBLE);
		inflater = LayoutInflater.from(getApplicationContext());
		applicationView = inflater.inflate(R.layout.activity_applications, null);
		settingsView = inflater.inflate(R.layout.activity_settings, null);
		homeView = inflater.inflate(R.layout.activity_home, null);
		nowCardLayout = (NowCardLayout) homeView.findViewById(R.id.now_card_layout);
		mSettingToolbar = (RelativeLayout) settingsView.findViewById(R.id.setting_top_header);
		mApplicationToolbar = (RelativeLayout) applicationView.findViewById(R.id.application_toolbar);

		mStickyView.setOnClickListener(new OnToolbarClickListener());

		mMainContent.removeAllViews();
		mMainContent.addView(homeView);
		mAppListView = (ListView) applicationView.findViewById(R.id.application_list_view);
		populateSlidingMenu();

		mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						onScrollChanged(mObservableScrollView.getScrollY());
					}
				});

		mRefreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				populateHomeCard();
			}
		});

		mGoogleSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String packageToRun = ConfigurationUtil.GOOGLE_NOW_PACKAGE;
				if(ApplicationUtil.isPackageExisted(getApplicationContext(), packageToRun)) {
					Intent intent = getPackageManager().getLaunchIntentForPackage(packageToRun);
					startActivity(intent);
				} else {
					Uri mobileUri = Uri.parse(ConfigurationUtil.PLAY_STORE_MOBILE_URI_STR + packageToRun);
					Uri webUri = Uri.parse(ConfigurationUtil.PLAY_STORE_WEB_URI_STR + packageToRun);
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, mobileUri));
					} catch (ActivityNotFoundException e) {
						startActivity(new Intent(Intent.ACTION_VIEW, webUri));
					}
				}
			}
		});

		mAnimSecondaryProfileHide.setAnimationListener(new HideViewAnimationListener(mSecondaryProfileImageHolder));
		mAnimSecondaryProfileShow.setAnimationListener(new ShowViewAnimationListener(mSecondaryProfileImageHolder));

		initUserPreference(false);

	}
	
	private void updateDrawerPagingIndicator(int page) {
		int count = mMainPagerAdapter.getCount();
		mMainPagerIndicator.removeAllViews();
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
			mMainPagerIndicator.addView(indicatorIcon);
		}
	}

	private void populateSlidingMenu() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isQuickHackEnable = prefs.getBoolean("personalize_advanced_quick_access_hack", false);
		boolean isUseNativeDrawer = prefs.getBoolean("personalize_advanced_quick_access_native_drawer", false);

		slidingMenu = new SlidingMenu(this);

		if(isQuickHackEnable && isUseNativeDrawer) {
			slidingMenu.setMode(SlidingMenu.LEFT);
			slidingMenu.setShadowDrawable(R.drawable.slide_drawer_shadow_left);
			slidingMenu.setMenu(settingsView);
		} else {
			slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
			slidingMenu.setShadowDrawable(R.drawable.slide_drawer_shadow_left);
			slidingMenu.setSecondaryShadowDrawable(R.drawable.slide_drawer_shadow_right);
			slidingMenu.setMenu(settingsView);
			slidingMenu.setSecondaryMenu(applicationView);
		}

		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setShadowWidth(20);
		slidingMenu.setBehindOffsetRes(R.dimen.behind_menu_offset);
		slidingMenu.setFadeDegree(1f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setOnCloseListener(new OnSlidingCloseListener());
		slidingMenu.setOnOpenListener(new OnSlidingOpenListener());
		slidingMenu.setOnClosedListener(new OnSlidingClosedListener());
		slidingMenu.setOnOpenedListener(new OnSlidingOpenedListener());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void restartActivity() {
		Toast.makeText(getApplicationContext(), getString(R.string.application_restarting), Toast.LENGTH_SHORT).show();
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initUserPreference(boolean isReloadApplicationOnly) {
		if(!isReloadApplicationOnly) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			reloadBitmap();
			reloadOtherData(prefs);
			reloadColors(prefs);
			populateHomeCard();
			reloadDate();
			populateSettings();
			reloadFavorite();
		}
		new FetchApplicationListTask(appTitleCircleColor).execute();
		if(isRequireFeedUpdate())
			populateHomeCard();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isWeatherThreadStarted && timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onBackPressed() {
		if(mFloatingFavButton != null && mFloatingFavButton.isExpanded())
			mFloatingFavButton.collapse();
		if(slidingMenu != null && slidingMenu.isMenuShowing())
			slidingMenu.toggle();
		if(mObservableScrollView != null)
			mObservableScrollView.smoothScrollTo(0, 0);
	}

	private void reloadDate() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isDateDisplay = prefs.getBoolean("personalize_general_display_date", true);
		if(isDateDisplay) {
			String dateStr = null;
			String dateFormat = prefs.getString("personalize_general_date_format", HEADER_DATE_FORMAT);
			try { 
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
				dateStr = sdf.format(date);
			} catch (Exception e) {
				Date date = new Date();
				dateStr = GENERIC_DATE_FORMAT.format(date);
			}
			mMainPagerAdapter.updateClock(dateStr);
		} else {
			mMainPagerAdapter.updateClock(null);
		}
	}

	private void populateSettings() {
		final List<LauncherSettingItem> launcherSettingItems = new ArrayList<LauncherSettingItem>();
		launcherSettingItems.add(new LauncherSettingItem(R.drawable.ic_palette, getString(R.string.settings_item_personalize), new PersonalizeMenuItem(getApplicationContext())));
		launcherSettingItems.add(new LauncherSettingItem(R.drawable.ic_input, getString(R.string.settings_item_feed_source), new FeedSourceMenuItem(getApplicationContext())));
		launcherSettingItems.add(new LauncherSettingItem(R.drawable.ic_apps, getString(R.string.settings_item_app_drawer), new AppDrawerMenuItem(getApplicationContext())));
		mSettingListView = (ListView) settingsView.findViewById(R.id.settings_list_view);
		final SettingListAdapter adapter = new SettingListAdapter(getApplicationContext(), launcherSettingItems);
		mSettingListView.setAdapter(adapter);
		mSettingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LauncherSettingItem item = launcherSettingItems.get(position);
				item.getAction().performClick();
			}
		});
	}

	private void showNewsFeedLoading() {
		mLoadingProgressbar.setVisibility(View.VISIBLE);
	}

	private void hideNewsFeedLoading() {
		mLoadingProgressbar.setVisibility(View.INVISIBLE);
	}

	private String getExistingSourceFromSharedPreference() {
		String sourceJson = null;
		SharedPreferences pref = this.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_FEED_SETTINGS, Context.MODE_PRIVATE);
		sourceJson = pref.getString(ConfigurationUtil.SHARED_PREFERENCE_FEED_SOURCE, "");
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_FEED_SOURCE, sourceJson);
		editor.putBoolean(ConfigurationUtil.SHARED_PREFERENCE_FEED_UPDATE, false);
		editor.commit();
		return sourceJson;
	}

	private boolean isRequireFeedUpdate() {
		SharedPreferences pref = this.getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_FEED_SETTINGS, Context.MODE_PRIVATE);
		return pref.getBoolean(ConfigurationUtil.SHARED_PREFERENCE_FEED_UPDATE, true);
	}

	private List<RSSFeedSource> getExistingFeedSource() {
		List<RSSFeedSource> list = new ArrayList<RSSFeedSource>();
		String jsonStr = getExistingSourceFromSharedPreference();
		if(!StringUtil.isNullEmptyString(jsonStr)) {
			try {
				JSONArray jsonArray = new JSONArray(jsonStr);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject favJsonObj = jsonArray.getJSONObject(i);
					String name = favJsonObj.getString("name");
					String feedUrl = favJsonObj.getString("feedUrl");
					String webUrl = favJsonObj.getString("webUrl");
					boolean isFavorite = favJsonObj.getBoolean("isFavorite");
					long dateAdded = favJsonObj.getLong("dateAdded");
					RSSFeedSource source = new RSSFeedSource();
					source.setName(name);
					source.setUrl(webUrl);
					source.setFeedUrl(feedUrl);
					source.setFavorite(isFavorite);
					source.setDateAdded(dateAdded);
					list.add(source);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(list);
		return list;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@SuppressLint("InflateParams")
	private void populateHomeCard() {
		JSONArray rssList = new JSONArray();
		List<RSSFeedSource> sourceList = getExistingFeedSource();
		if(sourceList != null && sourceList.size() > 0) {
			for(RSSFeedSource source : sourceList) {
				rssList.put(source.getFeedUrl());
			}
		}
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put(RSS_FEED_JSON_KEY, rssList);
			FetchNewsTask task = new FetchNewsTask();
			task.execute(jsonObj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		if (requestCode == 1) {
			if(resultCode == RESULT_OK){
				int result = imageReturnedIntent.getIntExtra("result", 0);
				if(result == ConfigurationUtil.RESULT_RELOAD_FAVORITE) {
					reloadFavorite();
				}
			}
			if (resultCode == RESULT_CANCELED) {
			}
		}		
	}

	float currentY;

	@Override
	public void onScrollChanged(int scrollY) {
		float translationY = mStickyView.getTranslationY();
		int stickyViewTop = mStickyView.getTop();
		float alpha = ConfigurationUtil.MAX_TOOLBAR_TRANSPARENCY;
		if(Math.max(mPlaceholderView.getTop(), scrollY) >= stickyViewTop) {
			translationY = +(scrollY - stickyViewTop);
			alpha = ConfigurationUtil.MIN_TOOLBAR_TRANSPARENCY;
		} else {
			translationY = -(scrollY / TOOLBAR_ADJUSTER);
		}
		mStickyView.setTranslationY(translationY);

		//		if(scrollY <= mHeaderHolder.getMeasuredWidth())
		//			mHeaderHolder.setTranslationX(-scrollY);
		currentY = mHeaderHolder.getTranslationY();
		mHeaderHolder.setTranslationY(scrollY / 3);

		int diff = (int) (mStickyView.getY() - scrollY);
		alpha = (((diff * 1f) / 600)* (ConfigurationUtil.MAX_TOOLBAR_TRANSPARENCY - ConfigurationUtil.MIN_TOOLBAR_TRANSPARENCY)) + ConfigurationUtil.MIN_TOOLBAR_TRANSPARENCY;
		mStickyView.setAlpha(alpha);
		mSecondaryProfileImageHolder.setTranslationY(translationY);
		mSecondaryProfileImageHolder.setTranslationZ(16f);
		if(translationY > 0) { // This indicate toolbar already reach top
			if(!isSecondaryProfileImageVisible)
				mSecondaryProfileImageHolder.startAnimation(mAnimSecondaryProfileShow);
			isSecondaryProfileImageVisible = true;
		} else {
			if(isSecondaryProfileImageVisible)
				mSecondaryProfileImageHolder.startAnimation(mAnimSecondaryProfileHide);
			isSecondaryProfileImageVisible = false;
		}
		float bannerYTranslation = scrollY / BANNER_SPEED;
		topHeaderImage.setTranslationY(bannerYTranslation);
	}

	@Override
	public void onScrollDown() {
		//showFavButton();
	}

	@Override
	public void onScrollUp() {
		//hideFavButton();
	}

	@Override
	public boolean onDownMotionEvent() {
		return true;
	}

	@Override
	public boolean onCancelMotionEvent() {
		return true;
		//		return !isFavMenuVisible;
	}

	@Override
	public boolean onUpMotionEvent() {
		return true;
		//		return !isFavMenuVisible;
	}

	@Override
	public boolean onActionMoveEvent() {
		return true;
		//		return !isFavMenuVisible && !isFavButtonTouch;
	}

	private class OnSlidingClosedListener implements OnClosedListener {

		@Override
		public void onClosed() {
			if(ConfigurationUtil.isRequireRestart(getApplicationContext())) {
				ConfigurationUtil.unsetRequireRestart(getApplicationContext());
				restartActivity();
				return; // When restarting, no need to proceed, because it will be executed when onCreate is call
			}
			if(ConfigurationUtil.isRequireReload(getApplicationContext())) {
				initUserPreference(false);
				ConfigurationUtil.unsetRequireReload(getApplicationContext());
			}
			if(ConfigurationUtil.isRequireFeedReload(getApplicationContext())) {
				populateHomeCard();
				ConfigurationUtil.unsetRequireFeedReload(getApplicationContext());
			}
		}

	}

	private class OnSlidingOpenedListener implements OnOpenedListener {

		@Override
		public void onOpened() {

		}

	}

	private class OnSlidingCloseListener implements OnCloseListener {

		@Override
		public void onClose() {

		}

	}

	private class OnSlidingOpenListener implements OnOpenListener {

		@Override
		public void onOpen() {
		}

	}
	
	private void reloadDisplayName(SharedPreferences prefs) {
		String displayNameStr = prefs.getString("personalize_general_display_name", null);
		mMainPagerAdapter.updateDisplayName(displayNameStr);
	}

	private void reloadOtherData(SharedPreferences prefs) {
		// For display name
		reloadDisplayName(prefs);

		// For quick access hack
		final boolean isQuickAccessHackEnable = prefs.getBoolean("personalize_advanced_quick_access_hack", false);
		final boolean isUseNativeDrawerEnable = prefs.getBoolean("personalize_advanced_quick_access_native_drawer", false);
		if(mFloatingFavButton != null) {
			mFloatingFavButton.setQuickHackEnable(isQuickAccessHackEnable, new Callback() {

				@Override
				public void performCallback() {
					if(isUseNativeDrawerEnable && isQuickAccessHackEnable) {
						Intent i = new Intent(MainActivity.this, AppDrawerActivity.class);
						i.putExtra("toolbarColor", toolbarColor);
						startActivityForResult(i, 1);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					} else {
						slidingMenu.showSecondaryMenu(true);
					}
				}
			});
		}
	}


	private void reloadColors(SharedPreferences prefs) {
		boolean isNeedOverride = prefs.getBoolean("personalize_color_override_default", false);
		navbarColor = getResources().getColor(R.color.navbar_color);
		dateTextColor = Color.WHITE;
		statusbarColor = getResources().getColor(R.color.status_bar_color);
		toolbarColor = getResources().getColor(R.color.toolbar_color);
		backgroundColor = getResources().getColor(R.color.content_background);
		cardTitleBackgroundColor = getResources().getColor(R.color.toolbar_color);
		appTitleCircleColor = getResources().getColor(R.color.toolbar_color);
		cardContentBackgroundColor = Color.WHITE;

		if(isNeedOverride) {
			navbarColor = prefs.getInt("personalize_color_navbar_color", Color.TRANSPARENT);
			dateTextColor = prefs.getInt("personalize_color_date_text_color", 0);
			statusbarColor = prefs.getInt("personalize_color_statusbar_color", Color.TRANSPARENT);
			toolbarColor = prefs.getInt("personalize_color_toolbar_color", Color.BLACK);
			backgroundColor = prefs.getInt("personalize_color_background_color", Color.WHITE);
		} else {
			String themeSelected = prefs.getString("personalize_general_theme", null);
			if(!StringUtil.isNullEmptyString(themeSelected)) {
				int arrayResId = getArrayResId(getApplicationContext(), "theme_" + themeSelected);
				if(arrayResId > 0) {
					String[] mTestArray = getResources().getStringArray(arrayResId);
					statusbarColor = Color.parseColor(mTestArray[9]);
					toolbarColor = Color.parseColor(mTestArray[8]);
					backgroundColor = Color.parseColor(mTestArray[4]);
					dateTextColor = Color.parseColor(mTestArray[0]);

					String navbarColorStr = mTestArray[9];
					navbarColorStr = navbarColorStr.replaceFirst("#", "#71");

					navbarColor = Color.parseColor(navbarColorStr);
					cardTitleBackgroundColor = Color.parseColor(mTestArray[7]);
					appTitleCircleColor = Color.parseColor(mTestArray[3]);
					String headerHolderBackgroundColorStr = mTestArray[8];
					String headerOverlayBackgroundColorStr = mTestArray[8];
					headerHolderBackgroundColorStr = headerHolderBackgroundColorStr.replaceFirst("#", "#71");
					headerOverlayBackgroundColorStr = headerOverlayBackgroundColorStr.replaceFirst("#", "#60");
					headerHolderBackgroundColor = Color.parseColor(headerHolderBackgroundColorStr);
					headerImageOverlayColor = Color.parseColor(headerOverlayBackgroundColorStr);
				}
			}
		}

		getWindow().setNavigationBarColor(navbarColor);
		getWindow().setStatusBarColor(statusbarColor);
		mStickyView.setBackgroundColor(toolbarColor);
		mMainContent.setBackgroundColor(backgroundColor);
		mObservableScrollView.setBackgroundColor(backgroundColor);
		mSettingToolbar.setBackgroundColor(toolbarColor);
		mApplicationToolbar.setBackgroundColor(toolbarColor);
		if(headerHolderBackgroundColor != 0)
			mHeaderHolder.setBackgroundColor(headerHolderBackgroundColor);
		if(headerImageOverlayColor != 0)
			mHeaderOverlay.setBackgroundColor(headerImageOverlayColor);
	}

	public static int getArrayResId(Context context, String name) {
		return context.getResources().getIdentifier(name, "array", context.getPackageName());
	}

	private void reloadBitmap() {
		Bitmap icon = null;
		Bitmap bannerImage = null;
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/" + ConfigurationUtil.APPLICATION_SD_DIRECTORY + "/" + ConfigurationUtil.SUBDIRECTORY_MEDIA); 
		if(!myDir.exists())
			return;
		File profileImageFile = new File(myDir, ConfigurationUtil.FILENAME_PROFILE_IMAGE);
		File bannerImageFile = new File(myDir, ConfigurationUtil.FILENAME_BANNER_IMAGE);
		if(profileImageFile.exists())
			icon = BitmapUtil.getBitmapFromFile(profileImageFile); 
		if(bannerImageFile.exists())
			bannerImage = BitmapUtil.getBitmapFromFile(bannerImageFile);
		if(bannerImage != null) {
			BitmapDrawable d = new BitmapDrawable(getResources(), bannerImage);
			topHeaderImage.setBackground(d);
		}
		if(icon == null)
			icon = BitmapFactory.decodeResource(getResources(), R.drawable.launcher);
		Bitmap finalProfileImage = BitmapUtil.getCircularBitmapWithWhiteBorder(icon, PROFILE_IMAGE_BORDER_SIZE, Color.WHITE);
		mSecondaryProfileImage.setImageBitmap(finalProfileImage);
	}

	private class FetchApplicationListTask extends AsyncTask<LauncherApplication, Void, List<LauncherApplication>> {

		private int titleColor;

		public FetchApplicationListTask(int titleColor) {
			this.titleColor = titleColor;
		}

		@Override
		protected List<LauncherApplication> doInBackground(LauncherApplication... params) {
			List<HiddenApplicationItem> itemList = ApplicationUtil.getExistingHiddenApps(MainActivity.this);
			List<String> packageNameList = new ArrayList<String>();
			if(itemList != null && itemList.size() > 0) {
				for(HiddenApplicationItem item : itemList) {
					packageNameList.add(item.getPackageName());
				}
			}
			launcherAppsList.clear();
			List<GroupApps> groupList = ApplicationUtil.getExistingGroupApps(MainActivity.this);
			List<String> packageInGroup = new ArrayList<String>();
			if(groupList != null && groupList.size() > 0) {
				for(GroupApps group : groupList) {
					List<LauncherApplication> groupAppList = group.getAppList();
					if(groupAppList != null && groupAppList.size() > 0) {
						for(LauncherApplication item : groupAppList) {
							String packageName = item.getPackageName();
							packageInGroup.add(packageName);
						}
					}
					LauncherApplication launcherGroup = new LauncherApplication();
					launcherGroup.setType(LauncherApplication.Type.FOLDER);
					launcherGroup.setName(group.getName());
					launcherGroup.setGroupAppList(groupAppList);
					launcherAppsList.add(launcherGroup);
				}
			}

			PackageManager pm = getPackageManager();
			Intent i = new Intent(Intent.ACTION_MAIN, null);
			i.addCategory(Intent.CATEGORY_LAUNCHER);
			//get a list of installed apps.
			List<ResolveInfo> availableActivities = pm.queryIntentActivities(i, 0);
			for(ResolveInfo ri:availableActivities){
				LauncherApplication app = new LauncherApplication();
				String packageName = ri.activityInfo.packageName;
				if(packageNameList.contains(packageName) || packageInGroup.contains(packageName))
					continue;
				app.setPackageName(packageName);
				app.setName(ri.loadLabel(pm).toString());
				app.setType(LauncherApplication.Type.APPLICATION);
				launcherAppsList.add(app);
			}

			Collections.sort(launcherAppsList);
			return launcherAppsList;
		}

		@Override
		protected void onPostExecute(List<LauncherApplication> homeNewsItemList) {
			Iterator<LauncherApplication> launcherAppIter = launcherAppsList.iterator();
			String prevLetter = null;
			while(launcherAppIter.hasNext()) {
				LauncherApplication app = launcherAppIter.next();
				if(app.getName() == null) {
					launcherAppIter.remove();
					continue;
				}
				String thisLetter = app.getName().substring(0, 1).toLowerCase(Locale.getDefault());
				if(prevLetter != null && !thisLetter.equals(prevLetter)) {
					app.setStartGroup(true);
					prevLetter = thisLetter;
				}
				if(prevLetter == null) {
					app.setStartGroup(true);
					prevLetter = thisLetter;
				}

			}
			final ApplicationListAdapter adapter = new ApplicationListAdapter(launcherAppsList, getApplicationContext(), titleColor);
			mAppListView.setAdapter(adapter);
			mAppListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					position = adapter.getRealPosition(position);
					LauncherApplication app = launcherAppsList.get(position);
					if(app.getType() == LauncherApplication.Type.APPLICATION) {
						Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(app.getPackageName());
						startActivity(LaunchIntent);
					} else {
						Intent intentFolder = new Intent(getApplicationContext(), FolderDrawerOpenActivity.class);
						intentFolder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intentFolder.putExtra("group", app);
						intentFolder.putExtra("toolbarColor", toolbarColor);
						startActivity(intentFolder);
					}
				}
			});

			mAppListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					position = adapter.getRealPosition(position);
					final LauncherApplication app = launcherAppsList.get(position);
					if(app.getType() == LauncherApplication.Type.APPLICATION) {
						String[] items = {getString(R.string.application_add_favorite), getString(R.string.application_details), getString(R.string.application_uninstall)};
						DialogUtil.createSelectDialogItem(MainActivity.this, items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch(which) {
								case 0 :
									ApplicationUtil.addApplicationAsFavorite(MainActivity.this, app, new Callback() {

										@Override
										public void performCallback() {
											reloadFavorite();
										}
									});
									break;
								case 1 : 
									try {
										Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
										intent.setData(Uri.parse("package:" + app.getPackageName()));
										startActivity(intent);
									} catch ( ActivityNotFoundException e ) {
										Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
										startActivity(intent);
									}
									break;
								case 2 :
									Intent intent = new Intent(Intent.ACTION_DELETE);
									intent.setData(Uri.parse("package:" + app.getPackageName()));
									startActivity(intent);
									break;
								}
							}
						}).show();
					} else {
						String[] items = {getString(R.string.delete)};
						DialogUtil.createSelectDialogItem(MainActivity.this, items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case 0 :
									ApplicationUtil.deleteGroup(MainActivity.this, app, new Callback() {

										@Override
										public void performCallback() {
											initUserPreference(true);
										}
									});
									break;
								}
							}
						}).show();
					}
					return true;
				}
			});
			final ImageView closeIcon = (ImageView) applicationView.findViewById(R.id.application_close_icon);
			final ImageView appSearch = (ImageView) applicationView.findViewById(R.id.application_search_icon);
			final EditText searchCriteria = (EditText) applicationView.findViewById(R.id.search_text);
			final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			appSearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					appSearch.setVisibility(View.GONE);
					closeIcon.setVisibility(View.VISIBLE);
					closeIcon.bringToFront();
					searchCriteria.setVisibility(View.VISIBLE);
					searchCriteria.setFocusableInTouchMode(true);
					searchCriteria.requestFocus();
					inputMethodManager.showSoftInput(searchCriteria, InputMethodManager.SHOW_IMPLICIT);
				}
			});
			closeIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					appSearch.setVisibility(View.VISIBLE);
					closeIcon.setVisibility(View.INVISIBLE);
					searchCriteria.setVisibility(View.INVISIBLE);
					searchCriteria.setText("");
				}
			});
			searchCriteria.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					String searchFor = s.toString();
					for(int i = 0; i < launcherAppsList.size(); i++) {
						LauncherApplication app = launcherAppsList.get(i);
						String appName = app.getName().toLowerCase(Locale.getDefault());
						if(appName.contains(searchFor)) {
							//							Log.v("Launchpet2", "Showing : " + appName);
							adapter.show(i);
						} else {
							//							Log.v("Launchpet2", "Hiding : " + appName);
							adapter.hide(i);
						}
					}
				}
			});
		}

	}

	//	private List<LauncherApplication> getFavoriteApplicationList() {
	//		List<LauncherApplication> appList = new ArrayList<LauncherApplication>();
	//		SharedPreferences pref = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_KEY_APPLICATION_SETTING, MODE_PRIVATE);
	//		String jsonStr = pref.getString(ConfigurationUtil.SHARED_PREFERENCE_KEY_FAVORITE, "");
	//		if(!StringUtil.isNullEmptyString(jsonStr)) {
	//			try {
	//				JSONArray jsonArr = new JSONArray(jsonStr);
	//				if(jsonArr != null && jsonArr.length() > 0) {
	//					for(int i = 0; i < jsonArr.length(); i++) {
	//						JSONObject jsonObj = jsonArr.getJSONObject(i);
	//						LauncherApplication app = new LauncherApplication();
	//						app.setName(jsonObj.getString("name"));
	//						app.setPackageName(jsonObj.getString("packageName"));
	//						app.setIconResId(jsonObj.getInt("iconResId"));
	//						appList.add(app);
	//					}
	//				}
	//			} catch (JSONException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//		return appList;
	//	}
	//
	//	private void addApplicationAsFavorite(LauncherApplication app) {
	//		List<LauncherApplication> existingFavAppList = getFavoriteApplicationList();
	//		if(existingFavAppList != null && existingFavAppList.size() > 0) {
	//			for(LauncherApplication existingApp : existingFavAppList) {
	//				String existingPackageName = existingApp.getPackageName();
	//				if(app.getPackageName().equals(existingPackageName)) {
	//					Toast.makeText(getApplicationContext(), getString(R.string.error_already_in_favorite), Toast.LENGTH_SHORT).show();
	//					return;
	//				}
	//			}
	//		}
	//		if(existingFavAppList == null)
	//			existingFavAppList = new ArrayList<LauncherApplication>();
	//		if(existingFavAppList.size() >= ConfigurationUtil.MAX_FAVORITE) {
	//			Toast.makeText(getApplicationContext(), getString(R.string.error_max_fav_reach), Toast.LENGTH_SHORT).show();
	//			return;
	//		}
	//		existingFavAppList.add(app);
	//		writeFavoriteApplicationList(existingFavAppList);
	//		reloadFavorite();
	//	}
	//
	//	public void writeFavoriteApplicationList(List<LauncherApplication> favAppList) {
	//		JSONArray jsonArray = new JSONArray();
	//		for(LauncherApplication appList : favAppList) {
	//			JSONObject jsonObj = new JSONObject();
	//			try {
	//				jsonObj.put("name", appList.getName());
	//				jsonObj.put("packageName", appList.getPackageName());
	//				jsonObj.put("iconResId", appList.getIconResId());
	//			} catch (JSONException e) {
	//				e.printStackTrace();
	//			}
	//			jsonArray.put(jsonObj);
	//		}
	//		String jsonStr = jsonArray.toString();
	//		SharedPreferences pref = getSharedPreferences(ConfigurationUtil.SHARED_PREFERENCE_KEY_APPLICATION_SETTING, MODE_PRIVATE);
	//		SharedPreferences.Editor editor = pref.edit();
	//		editor.putString(ConfigurationUtil.SHARED_PREFERENCE_KEY_FAVORITE, jsonStr);
	//		editor.commit();
	//	}

	private void reloadFavorite() {
		List<LauncherApplication> appList = ApplicationUtil.getFavoriteApplicationList(MainActivity.this);
		if(appList != null && appList.size() > 0) {
			int currentIndex = 0;
			for(int i = ConfigurationUtil.MAX_FAVORITE - 1; i >= 0; i--) {
				int resID = getResources().getIdentifier("floating_fav_menu_" + i, "id", getPackageName());
				LinearLayout linearLayout = (LinearLayout) mFloatingFavButton.findViewById(resID);
				if(currentIndex >= appList.size()) {
					linearLayout.setVisibility(View.INVISIBLE);
				} else {
					LauncherApplication app = appList.get(currentIndex);
					try {
						int btnResID = getResources().getIdentifier("floating_fav_menu_" + i + "_button", "id", getPackageName());
						int txtResID = getResources().getIdentifier("floating_fav_menu_" + i + "_text", "id", getPackageName());
						FloatingActionButton fab = (FloatingActionButton) linearLayout.findViewById(btnResID);
						TextView txt = (TextView) linearLayout.findViewById(txtResID);
						Drawable icon = getPackageManager().getApplicationIcon(app.getPackageName());
						fab.setIconDrawable(icon);
						String appName = app.getName();
						txt.setText(appName);
						linearLayout.setVisibility(View.VISIBLE);
						fab.setOnClickListener(new OnFloatingMenuItemClick(app));
						fab.setOnLongClickListener(new OnFloatingMenuItemLongClick(app));
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
				}
				currentIndex++;
			}
		} else {
			for(int i = 0; i < 6; i++) {
				int resID = getResources().getIdentifier("floating_fav_menu_" + i, "id", getPackageName());
				LinearLayout linearLayout = (LinearLayout) mFloatingFavButton.findViewById(resID);
				linearLayout.setVisibility(View.INVISIBLE);
			}
		}
	}

	private class FetchNewsTask extends AsyncTask<String, Void, List<HomeNewsItem>> {

		@SuppressLint("InflateParams")
		@Override
		protected List<HomeNewsItem> doInBackground(String... arg0) {
			JSONArray rssFeedUrlArray = null;
			try {
				JSONObject jsonObj = new JSONObject(arg0[0]);
				rssFeedUrlArray = jsonObj.getJSONArray(RSS_FEED_JSON_KEY);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			List<FeedData> feedDataList = new ArrayList<FeedData>();
			if(rssFeedUrlArray != null) {
				for(int i = 0; i < rssFeedUrlArray.length(); i++) {
					InputStream stream = null;
					try {
						String feedUrl = rssFeedUrlArray.getString(i);
						URL url = new URL(feedUrl);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(10000 /* milliseconds */);
						conn.setConnectTimeout(15000 /* milliseconds */);
						conn.setRequestMethod("GET");
						conn.setDoInput(true);
						// Starts the query
						conn.connect();
						stream = conn.getInputStream();
						XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
						XmlPullParser myparser = xmlFactoryObject.newPullParser();
						myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
						myparser.setInput(stream, null);
						XMLParser parser = new XMLParser();
						feedDataList.addAll(parser.parseXMLAndStoreIt(myparser));
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if(stream != null)
								stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			List<HomeNewsItem> homeNewsItemList = new ArrayList<HomeNewsItem>(feedDataList.size());
			// Feed data
			SimpleDateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
			for(FeedData rssFeedData : feedDataList) {
				NewsType type = NewsType.TEXT;
				HomeNewsItem item = new HomeNewsItem();
				item.setContent(rssFeedData.getDescription());
				try {
					item.setDate(rssDateFormat.parse(rssFeedData.getPubDate()));
				} catch (ParseException e) {
					item.setDate(new Date());
				}

				String iconUrl = null;
				String thumbnailUrl = null;

				// Hardcoded icon for dzone
				FeedSource source = rssFeedData.getSource();
				if(FeedSource.DZONE == source) {
					iconUrl = rssFeedData.get("dz:userimage");
					thumbnailUrl = rssFeedData.get("dz:thumbnail");
					type = NewsType.IMAGE;
				} else {
					String content = rssFeedData.getDescription();
					Document doc = Jsoup.parse(content);
					Elements imgs = doc.getElementsByTag("img");
					List<String> imageSrcUrlList = new ArrayList<String>();
					for (Element el : imgs) {
						String src = el.absUrl("src");
						if(thumbnailUrl == null)
							thumbnailUrl = src;
						imageSrcUrlList.add(src);
						type = NewsType.IMAGE;
						item.setImageUrlList(imageSrcUrlList);
					}

				}
				item.setIconUrl(iconUrl);
				item.setImageUrl(thumbnailUrl);
				item.setTitle(rssFeedData.getTitle());
				item.setType(type);
				item.putAll(rssFeedData);
				homeNewsItemList.add(item);
			}

			Collections.sort(homeNewsItemList);
			CommonUtil.serializeHomeNewsObjectList(homeNewsItemList);
			return homeNewsItemList;
		}

		@Override
		protected void onPreExecute() {
			List<HomeNewsItem> homeNewsItemList = CommonUtil.deserializeHomeNewsObject();
			if(homeNewsItemList != null && homeNewsItemList.size() > 0) {
				for(HomeNewsItem item : homeNewsItemList) {
					addNewsToView(item);
				}
			}
			showNewsFeedLoading();
		}

		@SuppressLint("InflateParams")
		@Override
		protected void onPostExecute(final List<HomeNewsItem> homeNewsItemList) {
			nowCardLayout.removeAllViews();
			if(homeNewsItemList != null && homeNewsItemList.size() > 0) {
				for(HomeNewsItem item : homeNewsItemList) {
					addNewsToView(item);
				}
			}
			hideNewsFeedLoading();
		}
	}

	private void addNewsToView(HomeNewsItem item) {
		NewsType type = item.getType();
		View card = null;
		Bitmap imageNewsBmp = item.getImage();
		Populator populator = null;
		if(type == NewsType.IMAGE && (imageNewsBmp == null && StringUtil.isNullEmptyString(item.getImageUrl()))) // If type is image but the image is null, we change the type to text
			type = NewsType.TEXT;

		if(type == NewsType.TEXT)
			populator = new TextCardPopulator(item);
		else if (type == NewsType.IMAGE)
			populator = new ImageCardPopulator(item);
		else if (type == NewsType.DZONE)
			populator = new DzoneCardPopulator(item, new BrowserLinkOpenListener(getApplicationContext()));
		else
			return;
		populator.setTitleBackgroundColor(cardTitleBackgroundColor);
		populator.setContentBackgroundColor(cardContentBackgroundColor);
		card = populator.populateView(inflater);
		if(card != null) {
			card.setOnClickListener(new OnCardClickListener(item));
			card.setOnTouchListener(new OnCardTouchListener(card));
			nowCardLayout.addView(card);
		}
	}

	private class OnFloatingMenuItemClick implements OnClickListener {

		private LauncherApplication app;

		public OnFloatingMenuItemClick(LauncherApplication app) {
			this.app = app;
		}

		@Override
		public void onClick(View v) {
			if(mFloatingFavButton != null && mFloatingFavButton.isExpanded())
				mFloatingFavButton.toggle();
			Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(app.getPackageName());
			startActivity(LaunchIntent);
		}

	}

	private class OnFloatingMenuItemLongClick implements OnLongClickListener {

		private LauncherApplication launcherApp;

		public OnFloatingMenuItemLongClick(LauncherApplication launcherApp) {
			this.launcherApp = launcherApp;
		}

		@Override
		public boolean onLongClick(View arg0) {
			String[] items = {getString(R.string.remove_from_favorite), getString(R.string.move_up), getString(R.string.move_down)};
			DialogUtil.createSelectDialogItem(MainActivity.this, items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(which) {
					case 0 :
						List<LauncherApplication> existingFavorite = ApplicationUtil.getFavoriteApplicationList(MainActivity.this);
						Iterator<LauncherApplication> existingFavoriteIter = existingFavorite.iterator();
						while(existingFavoriteIter.hasNext()) {
							LauncherApplication app = existingFavoriteIter.next();
							String packageName = app.getPackageName();
							String thisPackageName = launcherApp.getPackageName();
							if(packageName.equalsIgnoreCase(thisPackageName))
								existingFavoriteIter.remove();
						}
						ApplicationUtil.writeFavoriteApplicationList(MainActivity.this, existingFavorite);
						reloadFavorite();
						break;
					case 1 :
						// Move up
						changeFavoriteOrder(launcherApp, 1);
						break;
					case 2 :
						// Move down
						changeFavoriteOrder(launcherApp, -1);
						break;
					}
				}
			}).show();
			return true;
		}

	}

	private void changeFavoriteOrder(LauncherApplication app, int ordering) {
		List<LauncherApplication> existingFavorite = ApplicationUtil.getFavoriteApplicationList(MainActivity.this);
		Iterator<LauncherApplication> existingFavoriteIter = existingFavorite.iterator();
		int currentOrder = 0;
		int currentIndex = 0;
		while(existingFavoriteIter.hasNext()) {
			LauncherApplication tmpApp = existingFavoriteIter.next();
			String packageName = tmpApp.getPackageName();
			String thisPackageName = app.getPackageName();
			if(packageName.equalsIgnoreCase(thisPackageName)) {
				currentOrder = currentIndex;
				existingFavoriteIter.remove();
			}
			currentIndex++;
		}
		int newOrder = currentOrder + ordering;
		if(newOrder >= (existingFavorite.size() + 1)) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_item_already_on_top), Toast.LENGTH_SHORT).show();
		} else if (newOrder < 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_item_already_on_bottom), Toast.LENGTH_SHORT).show();
		} else {
			existingFavorite.add(newOrder, app);
			ApplicationUtil.writeFavoriteApplicationList(MainActivity.this, existingFavorite);
			reloadFavorite();
		}
	}

	private class OnCardClickListener implements OnClickListener {

		private HomeNewsItem item;

		public OnCardClickListener(HomeNewsItem item) {
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			Bundle b = new Bundle();
			b.putSerializable("home_news_item", item);
			Intent readIntent = new Intent(getApplicationContext(), ReadFeedActivity.class);
			readIntent.putExtras(b);
			startActivity(readIntent);
		}

	}

	private class ApplicationBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			initUserPreference(true);
		}

	}

	private class OnToolbarClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}

	}

	private class ClockBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			reloadDate();
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

}
