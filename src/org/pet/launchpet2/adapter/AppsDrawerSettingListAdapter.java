package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.AppsDrawerSettingItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppsDrawerSettingListAdapter extends BaseAdapter {
	
	private List<AppsDrawerSettingItem> items;
	
	private Context context;
	
	public AppsDrawerSettingListAdapter(Context context, List<AppsDrawerSettingItem> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items != null ? items.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? items.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.apps_drawer_setting_list_item, parent, false);
			holder = new ViewHolder();
			holder.apps_drawer_setting_item_icon = (ImageView) view.findViewById(R.id.apps_drawer_setting_item_icon);
			holder.apps_drawer_setting_item_name = (TextView) view.findViewById(R.id.apps_drawer_setting_item_name);
			holder.apps_drawer_setting_item_desc = (TextView) view.findViewById(R.id.apps_drawer_setting_item_desc);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		AppsDrawerSettingItem item = (AppsDrawerSettingItem) getItem(position);
		holder.apps_drawer_setting_item_icon.setImageResource(item.getIconResId());
		holder.apps_drawer_setting_item_name.setText(item.getLabel());
		holder.apps_drawer_setting_item_desc.setText(item.getDescription());
		return view;
	}
	
	static class ViewHolder {
		ImageView apps_drawer_setting_item_icon;
		TextView apps_drawer_setting_item_name;
		TextView apps_drawer_setting_item_desc;
	}

}
