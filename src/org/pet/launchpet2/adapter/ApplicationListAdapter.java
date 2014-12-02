package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.LauncherApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationListAdapter extends BaseAdapter {
	
	private List<LauncherApplication> launcherAppList;
	
	private boolean[] hiddenItems;
	
	private Context context;
	
	public ApplicationListAdapter(List<LauncherApplication> launcherAppList, Context context) {
		this.launcherAppList = launcherAppList;
		this.context = context;
		this.hiddenItems = new boolean[launcherAppList.size()];
		for(int i = 0; i < launcherAppList.size(); i++) {
			this.hiddenItems[i] = false;
		}
	}
	
	private int getHiddenCount() {
		int count = 0;
		for(int i = 0; i < launcherAppList.size(); i++)
			if(hiddenItems[i])
				count++;
		return count;
	}
	
	public void hide(int position) {
		hiddenItems[position] = true;
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
	
	public void show(int position) {
		hiddenItems[position] = false;
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
	
//	public int getRealPosition(int position) {
//		int hiddenCount = getHiddenCount();
//		int notHidden = 0;
//		for(int i = 0; i < launcherAppList.size(); i++) {
//			if(position == notHidden) {
//				position = i;
//				break;
//			}
//			if(!hiddenItems[i])
//				notHidden++;
//		}
//		return position;
//	}
	
	public int getRealPosition(int position) {
		int hElements = getHiddenCountUpTo(position);
		int diff = 0;
		for(int i=0;i<hElements;i++) {
			diff++;
			if(hiddenItems[position+diff])
				i--;
		}
		return (position + diff);
	}
	
	private int getHiddenCountUpTo(int location) {
		int count = 0;
		for(int i=0;i<=location;i++) {
			if(hiddenItems[i])
				count++;
		}
		return count;
	}
	
	@Override
	public int getCount() {
		return launcherAppList != null ? launcherAppList.size() - getHiddenCount() : 0;
	}

	@Override
	public Object getItem(int position) {
//		Object obj = null;
//		if(getCount() > 0 && !hiddenItems[position])
//			obj = launcherAppList.get(position);
//		return obj;
		return getCount() > 0 ? launcherAppList.get(position) : null;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		position = getRealPosition(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridIconView = convertView;
		ViewHolder holder = null;
		Object obj = getItem(position);
		LauncherApplication app = (LauncherApplication) obj;
		if(gridIconView == null) {
			gridIconView = inflater.inflate(R.layout.icon_layout, parent, false);
			holder = new ViewHolder();
			holder.name_view = (TextView) gridIconView.findViewById(R.id.application_name);
			holder.icon_view = (ImageView) gridIconView.findViewById(R.id.application_icon);
			gridIconView.setTag(holder);
		} else {
			holder = (ViewHolder) gridIconView.getTag();
		}
		
		holder.icon_view.setImageDrawable(app.getIcon());
		holder.name_view.setText(app.getName());
		return gridIconView;
	}
	
	static class ViewHolder {
		private  ImageView icon_view;
		private TextView name_view;
	}

}
