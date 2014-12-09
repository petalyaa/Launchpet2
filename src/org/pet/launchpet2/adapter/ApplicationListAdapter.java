package org.pet.launchpet2.adapter;

import java.util.List;
import java.util.Locale;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.LauncherApplication;
import org.pet.launchpet2.util.BitmapUtil;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
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
	
	private int titleColor;
	
	public ApplicationListAdapter(List<LauncherApplication> launcherAppList, Context context, int titleColor) {
		this.launcherAppList = launcherAppList;
		this.context = context;
		this.titleColor = titleColor;
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
			holder.icon_title_view = (TextView) gridIconView.findViewById(R.id.application_title_icon);
			if(app.isStartGroup()) {
				GradientDrawable shapeDrawable = (GradientDrawable) holder.icon_title_view.getBackground();
				shapeDrawable.setColor(titleColor);
			}
			gridIconView.setTag(holder);
		} else {
			holder = (ViewHolder) gridIconView.getTag();
		}
		if(app != null) {
			holder.icon_view.setImageResource(R.drawable.ic_launcher);
			
//			List<LauncherApplication> groupAppList = app.getGroupAppList();
//			if(groupAppList != null && groupAppList.size() > 0) {
//				View view = inflater.inflate(R.layout.apps_folder_view, parent, false);
//				for(int i = 0; i < ConfigurationUtil.FOLDER_ICON_STACK_LIMIT; i++) {
//					int nameResourceID = context.getResources().getIdentifier("folder_view_icon_" + i, "id", context.getApplicationInfo().packageName);
//					ImageView thisImageView = (ImageView) view.findViewById(nameResourceID);
//					try {
//						LauncherApplication groupApp = groupAppList.get(i);
//						String packageName = groupApp.getPackageName();
//						Bitmap thisAppBmp = BitmapUtil.getBitmapFromPackage(context, packageName);
//						thisImageView.setImageBitmap(thisAppBmp);
//					} catch (Exception e) {
//						thisImageView.setVisibility(View.INVISIBLE);
//					}
//				}
//				Bitmap folderBitmap = BitmapUtil.getBitmapFromView(view, 50, 50);
//			}
			
			if(app.getType() == LauncherApplication.Type.APPLICATION) {
				holder.icon_view.setImageBitmap(BitmapUtil.getBitmapFromPackage(context, app.getPackageName()));
			}
			holder.name_view.setText(app.getName());
		}
		if(app.isStartGroup()) {
			holder.icon_title_view.setText(app.getName().substring(0, 1).toUpperCase(Locale.getDefault()));
			holder.icon_title_view.setVisibility(View.VISIBLE);
		} else
			holder.icon_title_view.setVisibility(View.INVISIBLE);
		return gridIconView;
	}
	
	static class ViewHolder {
		private  ImageView icon_view;
		private TextView name_view;
		private TextView icon_title_view;
	}

}
