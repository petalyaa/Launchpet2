package org.pet.launchpet2.adapter;

import java.util.List;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.RSSFeedSource;
import org.pet.launchpet2.thread.FetchFaviconAsync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingFeedListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<RSSFeedSource> feedDataList;
	
	public SettingFeedListAdapter(Context context) {
		this.context = context;
	}
	
	public void setDataList(List<RSSFeedSource> feedDataList) {
		this.feedDataList = feedDataList;
	}

	@Override
	public int getCount() {
		return feedDataList != null ? feedDataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? feedDataList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = convertView;
		ViewHolder holder = null;
		RSSFeedSource data = (RSSFeedSource) getItem(position);
		if(view == null) {
			view = inflater.inflate(R.layout.feed_source_list_view_item, parent, false);
			holder = new ViewHolder();
			holder.name_view = (TextView) view.findViewById(R.id.feed_source_url_name);
			holder.icon_view = (ImageView) view.findViewById(R.id.feed_source_icon);
			holder.url_view = (TextView) view.findViewById(R.id.feed_source_url_value);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.url_view.setText(data.getUrl());
		holder.name_view.setText(data.getName());
		new FetchFaviconAsync(holder.icon_view).execute(data.getUrl());
		
		return view;
	}
	
	static class ViewHolder {
		private TextView url_view;
		private TextView name_view;
		private ImageView icon_view;
	}

}
