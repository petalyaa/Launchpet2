package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.LauncherSettingItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<LauncherSettingItem> launcherSettingItem;
	
	public SettingListAdapter(Context context, List<LauncherSettingItem> launcherSettingItem) {
		this.context = context;
		this.launcherSettingItem = launcherSettingItem;
		
	}

	@Override
	public int getCount() {
		return launcherSettingItem != null ? launcherSettingItem.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? launcherSettingItem.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View settingItemView = convertView;
		ViewHolder holder = null;
		if(settingItemView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			settingItemView = inflater.inflate(R.layout.setting_item_view, parent, false);
			holder = new ViewHolder();
			holder.setting_item_icon = (ImageView) settingItemView.findViewById(R.id.setting_item_icon);
			holder.setting_item_text_view = (TextView) settingItemView.findViewById(R.id.setting_item_view_label);
			settingItemView.setTag(holder);
		} else {
			holder = (ViewHolder) settingItemView.getTag();
		}
		LauncherSettingItem item = (LauncherSettingItem) getItem(position);
		holder.setting_item_icon.setImageResource(item.getIconResId());
		holder.setting_item_text_view.setText(item.getLabel());
		return settingItemView;
	}
	
	static class ViewHolder {
		private ImageView setting_item_icon;
		private TextView setting_item_text_view;
	}

}
