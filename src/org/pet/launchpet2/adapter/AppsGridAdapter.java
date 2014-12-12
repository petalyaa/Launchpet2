package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.thread.FetchLocalImageAsync;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppsGridAdapter extends BaseAdapter {
	
	private List<LauncherApplication> appList;
	
	private Context context;
	
	public AppsGridAdapter(Context context, List<LauncherApplication> appList) {
		this.appList = appList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return appList.size();
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? appList.get(position) : null;
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
			view = inflater.inflate(R.layout.native_drawer_icon_view, parent, false);
			holder = new ViewHolder();
			holder.native_drawer_icon_view_icon = (ImageView) view.findViewById(R.id.native_drawer_icon_view_icon);
			holder.native_drawer_icon_view_label = (TextView) view.findViewById(R.id.native_drawer_icon_view_label);
			holder.native_drawer_icon_folder = (RelativeLayout) view.findViewById(R.id.native_drawer_icon_folder);
			holder.folder_view = inflater.inflate(R.layout.apps_folder_view, parent, false);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		LauncherApplication app = (LauncherApplication) getItem(position);
		holder.native_drawer_icon_view_label.setText(app.getName());
		if(app.getType() == LauncherApplication.Type.APPLICATION) {
			holder.native_drawer_icon_view_icon.setVisibility(View.VISIBLE);
			holder.native_drawer_icon_folder.setVisibility(View.INVISIBLE);
			new FetchLocalImageAsync(context, holder.native_drawer_icon_view_icon).execute(app.getPackageName());
		} else {
			holder.native_drawer_icon_view_icon.setVisibility(View.INVISIBLE);
			holder.native_drawer_icon_folder.setVisibility(View.VISIBLE);
			View folderView = holder.folder_view;
			List<LauncherApplication> groupAppList = app.getGroupAppList();
			if(groupAppList != null && groupAppList.size() > 0) {
				for(int i = 0; i < ConfigurationUtil.FOLDER_ICON_STACK_LIMIT; i++) {
					int nameResourceID = context.getResources().getIdentifier("folder_view_icon_" + i, "id", context.getApplicationInfo().packageName);
					ImageView thisImageView = (ImageView) folderView.findViewById(nameResourceID);
					try {
						LauncherApplication groupApp = groupAppList.get(i);
						String packageName = groupApp.getPackageName();
						new FetchLocalImageAsync(context, thisImageView).execute(packageName);
					} catch (Exception e) {
						thisImageView.setVisibility(View.INVISIBLE);
					}
				}
			}
			holder.native_drawer_icon_folder.addView(holder.folder_view);
		}
		return view;
	}
	
	private class ViewHolder {
		ImageView native_drawer_icon_view_icon;
		TextView native_drawer_icon_view_label;
		RelativeLayout native_drawer_icon_folder;
		View folder_view;
	}
	
}