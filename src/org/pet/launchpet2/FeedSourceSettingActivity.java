package org.pet.launchpet2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pet.launchpet2.adapter.SettingFeedListAdapter;
import org.pet.launchpet2.listener.OnSettingBackButtonClickListener;
import org.pet.launchpet2.model.RSSFeedSource;
import org.pet.launchpet2.util.ConfigurationUtil;
import org.pet.launchpet2.util.DialogUtil;
import org.pet.launchpet2.util.StringUtil;
import org.pet.launchpet2.util.XMLParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class FeedSourceSettingActivity extends Activity {

	private ImageButton mBackBtn;

	private ImageButton mAddBtn;
	
	private ListView mListView;

	private List<RSSFeedSource> favoriteFeedSource;

	private SettingFeedListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_source_setting);
		mListView = (ListView) findViewById(R.id.feed_source_rss_list);

		mBackBtn = (ImageButton) findViewById(R.id.feed_source_toolbar_back_btn);
		mAddBtn = (ImageButton) findViewById(R.id.feed_source_toolbar_add_btn);
		mBackBtn.setOnClickListener(new OnSettingBackButtonClickListener(this));
		mAddBtn.setOnClickListener(new OnAddButtonClick());

		favoriteFeedSource = new ArrayList<RSSFeedSource>();

		StringBuilder buf = new StringBuilder();
		InputStream jsonIn = null;
		BufferedReader reader = null;
		try {
			jsonIn = getResources().getAssets().open(ConfigurationUtil.FAVORITE_FEED_SOURCE_JSON);
			reader = new BufferedReader(new InputStreamReader(jsonIn, "UTF-8"));
			String str = null;
			while ((str = reader.readLine()) != null) {
				buf.append(str);
			}
			String jsonStr = buf.toString();
			if(!StringUtil.isNullEmptyString(jsonStr)) {
				JSONObject jsonObj = new JSONObject(jsonStr);
				JSONArray favJsonObjList = jsonObj.getJSONArray("favorites");
				if(favJsonObjList != null && favJsonObjList.length() > 0) {
					for(int i = 0; i < favJsonObjList.length(); i++) {
						JSONObject favJsonObj = favJsonObjList.getJSONObject(i);
						String name = favJsonObj.getString("name");
						String feedUrl = favJsonObj.getString("feedUrl");
						String webUrl = favJsonObj.getString("webUrl");
						RSSFeedSource source = new RSSFeedSource();
						source.setName(name);
						source.setUrl(webUrl);
						source.setFeedUrl(feedUrl);
						favoriteFeedSource.add(source);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(jsonIn != null)
					jsonIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		mAdapter = new SettingFeedListAdapter(getApplicationContext());
		final List<RSSFeedSource> feedDataList = getExistingFeedSource();
		populateMainListView(feedDataList);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				String[] items = {getString(R.string.delete_feed_source)};
				DialogUtil.createSelectDialogItem(FeedSourceSettingActivity.this, items, new OnLongPressSelectionClick(feedDataList.get(position))).show();
				return true;
			}
		});
	}
	
	private void populateMainListView(List<RSSFeedSource> feedDataList) {
		mAdapter.setDataList(feedDataList);
		mAdapter.notifyDataSetChanged();
		mListView.setAdapter(mAdapter);
	}

	private class OnAddButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			String[] items = {getString(R.string.pick_source_favorite), getString(R.string.pick_source_manual)};
			DialogUtil.createSelectDialogItem(FeedSourceSettingActivity.this, items, new OnAddSelectionSourceClick()).show();
		}

	}

	@SuppressLint("DefaultLocale")
	private void onAddFavoriteSourceClick() {
		final List<RSSFeedSource> sourceList = getExistingFeedSource();
		final String[] items = new String[favoriteFeedSource.size()];
		final boolean[] selected = new boolean[favoriteFeedSource.size()];
		final List<Integer> seletedItems = new ArrayList<Integer>();
		for(int i = 0; i < favoriteFeedSource.size(); i++) {
			RSSFeedSource source = favoriteFeedSource.get(i);
			items[i] = source.getName();
			String webUrl = source.getUrl();
			boolean isSelected = false;
			for(RSSFeedSource existingSource : sourceList) {
				if(existingSource.isFavorite()) {
					String thisWebUrl = existingSource.getUrl();
					if(webUrl.toLowerCase().startsWith(thisWebUrl.toLowerCase())) {
						isSelected = true;
						break;
					}
				}
			}
			selected[i] = isSelected;
		}
		
		String title = getString(R.string.pick_your_source);
		DialogUtil.createMultiSelectDialogItem(this, title, items, selected, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
				if (isChecked) {
					seletedItems.add(indexSelected);
					selected[indexSelected] = true;
				} else if (seletedItems.contains(indexSelected)) {
					seletedItems.remove(Integer.valueOf(indexSelected));
					selected[indexSelected] = false;
				}
			}
		}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
               Iterator<RSSFeedSource> sourceIterator = sourceList.iterator();
               while(sourceIterator.hasNext()) {
               	RSSFeedSource source = sourceIterator.next();
               	if(source.isFavorite())
               		sourceIterator.remove();
               }
               for(int i = 0; i < selected.length; i++) {
               	RSSFeedSource source = favoriteFeedSource.get(i);
               	if(source != null && selected[i]) {
           			source.setFavorite(true);
           			sourceList.add(source);
           		}
               }
               try {
					writeSourceToSharedPreference(sourceList);
					List<RSSFeedSource> feedDataList = getExistingFeedSource();
					populateMainListView(feedDataList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        }).show();
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
	
	private String getExistingSourceFromSharedPreference() {
		String sourceJson = null;
		SharedPreferences pref = this.getSharedPreferences("feed_settings", Context.MODE_PRIVATE);
		sourceJson = pref.getString("feed_source", "");
		return sourceJson;
	}
	
	private void writeSourceToSharedPreference(List<RSSFeedSource> sourceList) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < sourceList.size(); i++) {
			RSSFeedSource favJsonObj = sourceList.get(i);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", favJsonObj.getName());
			jsonObj.put("feedUrl", favJsonObj.getFeedUrl());
			jsonObj.put("webUrl", favJsonObj.getUrl());
			jsonObj.put("isFavorite", favJsonObj.isFavorite());
			jsonObj.put("dateAdded", System.currentTimeMillis());
			jsonArray.put(jsonObj);
		}
		String jsonStr = jsonArray.toString();
		SharedPreferences pref = this.getSharedPreferences("feed_settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("feed_source", jsonStr);
		editor.commit();
		ConfigurationUtil.setRequireFeedReload(getApplicationContext());
	}

	private void onAddManualSourceClick() {
		DialogUtil.createInputTextDialog(this, null, getString(R.string.enter_feed_url), new DialogUtil.DialogUtilCallback() {
			
			@Override
			public void onTextInputClickListener(String textInput) {
				new FetchManualUrlFeed().execute(textInput);
			}
		}).show();
	}

	private class OnAddSelectionSourceClick implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(which == 0) 
				onAddFavoriteSourceClick();
			else
				onAddManualSourceClick();
		}

	}
	
	private void onFeedDelete(RSSFeedSource feedSource) {
		String feedUrl = feedSource.getFeedUrl();
		List<RSSFeedSource> feedSourceList = getExistingFeedSource();
		if(feedSourceList != null && feedSourceList.size() > 0) {
			Iterator<RSSFeedSource> feedSourceIter = feedSourceList.iterator();
			while(feedSourceIter.hasNext()) {
				RSSFeedSource tmpFeedSource = feedSourceIter.next();
				String tmpFeedUrl = tmpFeedSource.getFeedUrl();
				if(tmpFeedUrl.equals(feedUrl)) {
					feedSourceIter.remove();
					break;
				}
			}
			try {
				writeSourceToSharedPreference(feedSourceList);
				populateMainListView(feedSourceList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class OnLongPressSelectionClick implements DialogInterface.OnClickListener {
		
		private RSSFeedSource feedSource;
		
		public OnLongPressSelectionClick(RSSFeedSource feedSource) {
			this.feedSource = feedSource;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Log.v("Launchpet2", "Selected : " + which);
			switch (which) {
			case 0 :
				onFeedDelete(feedSource);
				break;
			}
		}
		
	}
	
	private class FetchManualUrlFeed extends AsyncTask<String, Void, RSSFeedSource> {
		
		@SuppressLint("DefaultLocale")
		@Override
		protected void onPostExecute(RSSFeedSource source) {
			if(source == null)
				Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_rss_feed_url), Toast.LENGTH_SHORT).show();
			else {
				boolean hasError = false;
				List<RSSFeedSource> feedDataList = getExistingFeedSource();
				for(RSSFeedSource existingSource : feedDataList) {
					String feedUrl = existingSource.getFeedUrl();
					if(feedUrl.toLowerCase().equals(source.getFeedUrl().toLowerCase())) {
						hasError = true;
						Toast.makeText(getApplicationContext(), getString(R.string.error_feed_already_exist), Toast.LENGTH_SHORT).show();
						break;
					} 
				}
				if(!hasError) {
					feedDataList.add(source);
					try {
						writeSourceToSharedPreference(feedDataList);
						Toast.makeText(getApplicationContext(), getString(R.string.feed_added_success), Toast.LENGTH_SHORT).show();
						List<RSSFeedSource> newFeedDataList = getExistingFeedSource();
						populateMainListView(newFeedDataList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		protected RSSFeedSource doInBackground(String... params) {
			URL url = null;
			InputStream stream = null;
			HttpURLConnection conn = null;
			RSSFeedSource source = null;
			try {
				url = new URL(params[0]);
				conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				stream = conn.getInputStream();
				XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser myparser = xmlFactoryObject.newPullParser();
				myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
				myparser.setInput(stream, null);
				XMLParser parser = new XMLParser();
				source = parser.parseFeedSource(params[0], myparser);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} finally {
				if(conn != null)
					conn.disconnect();
				try {
					if(stream != null)
						stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return source;
		}
		
	}


}
