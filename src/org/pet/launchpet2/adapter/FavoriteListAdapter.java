package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.FavoriteApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavoriteListAdapter extends BaseAdapter {
	
	private List<FavoriteApp> favAppList;
	
	private Context context;
	
	public FavoriteListAdapter(Context context) {
		this.context = context;
	}
	
	public void setFavAppList(List<FavoriteApp> favAppList) {
		this.favAppList = favAppList;
	}

	@Override
	public int getCount() {
		return favAppList != null ? favAppList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? favAppList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View listItemView = convertView;
		ViewHolder holder = null;
		FavoriteApp favApp = (FavoriteApp) getItem(position);
		if(listItemView == null) {
			listItemView = inflater.inflate(R.layout.favorite_floating_menu_item, parent, false);
			holder = new ViewHolder();
			holder.label = (TextView) listItemView.findViewById(R.id.favorite_menu_item_label);
			holder.actionButton = (ImageButton) listItemView.findViewById(R.id.favorite_menu_item_button);
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.actionButton.getLayoutParams();
			int rightMargin = lp.rightMargin;
			lp.rightMargin = rightMargin;
			holder.actionButton.setLayoutParams(lp);
			listItemView.setTag(holder);
		} else {
			holder = (ViewHolder) listItemView.getTag();
		}
		holder.actionButton.setImageDrawable(favApp.getIcon());
		holder.label.setText(favApp.getName());
		return listItemView;
	}
	
	static class ViewHolder {
		private TextView label;
		private ImageButton actionButton;
	}

}
