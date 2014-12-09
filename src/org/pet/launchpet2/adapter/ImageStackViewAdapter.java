package org.pet.launchpet2.adapter;

import org.pet.launchpet2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageStackViewAdapter extends BaseAdapter {
	
	private static final Integer[] DEFAULT_STACK = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
	
	private Context context;
	
	private Integer[] items;
	
	public ImageStackViewAdapter(Context context, Integer[] items) {
		this.context = context;
		if(items == null)
			items = DEFAULT_STACK;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items != null ? items.length : 0;
	}

	@Override
	public Object getItem(int position) {
		return getCount() > 0 ? items[position] : null;
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
			view = inflater.inflate(R.layout.stack_image_view, parent, false);
			holder = new ViewHolder();
			holder.stack_image_view_image = (ImageView) view.findViewById(R.id.stack_image_view_image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Integer i = (Integer) getItem(position);
		holder.stack_image_view_image.setImageResource(i);
		return view;
	}
	
	static class ViewHolder {
		ImageView stack_image_view_image;
	}

}
