package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.List;

import org.pet.launchpet2.adapter.AppsDrawerSettingListAdapter;
import org.pet.launchpet2.listener.OnSettingBackButtonClickListener;
import org.pet.launchpet2.model.AppsDrawerSettingItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AppsDrawerSettingActivity extends Activity {
	
	private ImageButton mBackButton;
	
	private ListView mSettingList;
	
	private AppsDrawerSettingListAdapter mSettingListAdapter;
	
	private List<AppsDrawerSettingItem> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apps_drawer_setting);
		mBackButton = (ImageButton) findViewById(R.id.apps_drawer_toolbar_back_btn);
		mSettingList = (ListView) findViewById(R.id.apps_drawer_list);
		
		items = new ArrayList<AppsDrawerSettingItem>();
		items.add(new AppsDrawerSettingItem(R.drawable.ic_action_remove_red_eye, getString(R.string.settings_item_hidden_application), getString(R.string.settings_item_hidden_application_desc), HiddenApplicationActivity.class));
		items.add(new AppsDrawerSettingItem(R.drawable.ic_action_group_work, getString(R.string.settings_item_group_application), getString(R.string.settings_item_group_application_desc), HiddenApplicationActivity.class));
		mSettingListAdapter = new AppsDrawerSettingListAdapter(getApplicationContext(), items);
		mSettingList.setAdapter(mSettingListAdapter);
		mSettingList.setOnItemClickListener(new OnAppDrawerItemClickListener());
		
		mBackButton.setOnClickListener(new OnSettingBackButtonClickListener(this));
	}
	
	private class OnAppDrawerItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			AppsDrawerSettingItem item = items.get(position);
			Class<?> activityClass = item.getActivityClass();
			if(activityClass != null) {
				Intent intent = new Intent(getApplicationContext(), activityClass);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
		
	}

}
