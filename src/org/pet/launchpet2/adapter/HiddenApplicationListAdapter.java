package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.HiddenApplicationItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HiddenApplicationListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<HiddenApplicationItem> hiddenApplicationItemList;
	
	public HiddenApplicationListAdapter(Context context, List<HiddenApplicationItem> hiddenApplicationItemList) {
		this.context = context;
		this.hiddenApplicationItemList = hiddenApplicationItemList;
	}
	
	public void setDataList(List<HiddenApplicationItem> hiddenApplicationItemList) {
		this.hiddenApplicationItemList = hiddenApplicationItemList;
	}

	@Override
	public int getCount() {
		return hiddenApplicationItemList != null ? hiddenApplicationItemList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? hiddenApplicationItemList.get(position) : null;
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
			view = inflater.inflate(R.layout.hidden_app_item_list_view, parent, false);
			holder = new ViewHolder();
			holder.hidden_app_item_name = (TextView) view.findViewById(R.id.hidden_app_item_name);
			holder.hidden_app_item_package = (TextView) view.findViewById(R.id.hidden_app_item_package);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HiddenApplicationItem item = (HiddenApplicationItem) getItem(position);
		holder.hidden_app_item_name.setText(item.getName());
		holder.hidden_app_item_package.setText(item.getPackageName());
		return view;
	}
	
	static class ViewHolder {
		TextView hidden_app_item_name;
		TextView hidden_app_item_package;
	}

}
