package org.pet.launchpet2.adapter;

import java.util.ArrayList;
import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.GroupApps;
import org.pet.launchpet2.util.BitmapUtil;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.StackView;
import android.widget.TextView;

public class GroupAppSettingListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<GroupApps> items;
	
	public GroupAppSettingListAdapter(Context context, List<GroupApps> items) {
		this.context = context;
		this.setItems(items);
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
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(view == null) {
			view = inflater.inflate(R.layout.group_apps_item_list, parent, false);
			holder = new ViewHolder();
			holder.group_apps_item_list_icon = (RelativeLayout) view.findViewById(R.id.group_apps_item_list_icon);
			holder.group_apps_item_list_label = (TextView) view.findViewById(R.id.group_apps_item_list_label);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		GroupApps item = (GroupApps) getItem(position);
		holder.group_apps_item_list_label.setText(item.getName());
		List<String> packageList = item.getPackageList();
		List<Bitmap> imageViewList = new ArrayList<Bitmap>();
		if(packageList != null && packageList.size() > 0) {
			for(String s : packageList) {
				imageViewList.add(BitmapUtil.getBitmapFromPackage(context, s));
			}
		} 
		if(imageViewList != null && imageViewList.size() > 0) {
			holder.group_apps_item_list_icon.removeAllViews();
			View folderView = inflater.inflate(R.layout.apps_folder_view, parent, false);
			for(int i = 0; i < ConfigurationUtil.FOLDER_ICON_STACK_LIMIT; i++) {
				int nameResourceID = context.getResources().getIdentifier("folder_view_icon_" + i, "id", context.getApplicationInfo().packageName);
				ImageView thisImageView = (ImageView) folderView.findViewById(nameResourceID);
				if(i >= imageViewList.size()) {
					thisImageView.setVisibility(View.INVISIBLE);
				} else {
					thisImageView.setImageBitmap(imageViewList.get(i));
					thisImageView.setVisibility(View.VISIBLE);
				}
			}
			holder.group_apps_item_list_icon.addView(folderView);
		}
		return view;
	}

	public List<GroupApps> getItems() {
		return items;
	}

	public void setItems(List<GroupApps> items) {
		this.items = items;
	}
	
	static class ViewHolder {
		RelativeLayout group_apps_item_list_icon;
		TextView group_apps_item_list_label;
	}

}
