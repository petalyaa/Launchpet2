package org.pet.launchpet2.populator;


import org.pet.launchpet2.R;
import org.pet.launchpet2.model.HomeNewsItem;
import org.pet.launchpet2.thread.FetchFaviconAsync;
import org.pet.launchpet2.thread.FetchImageAsync;
import org.pet.launchpet2.util.StringUtil;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageCardPopulator extends Populator {
	
	public ImageCardPopulator(HomeNewsItem item) {
		super(item);
	}

	@SuppressLint("InflateParams")
	@Override
	public View populateView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.home_image_card, null);
		ImageView imageView = (ImageView) card.findViewById(R.id.card_image_icon_image);
		TextView titleTextView = (TextView) card.findViewById(R.id.card_image_title_label);
		ImageView imageNewsView = (ImageView) card.findViewById(R.id.card_image_image);
		titleTextView.setText(Html.fromHtml(item.getTitle()));
		String link = item.get("parentLink");
		String imageUrl = item.getImageUrl();
		if(!StringUtil.isNullEmptyString(link))
			new FetchFaviconAsync(imageView).execute(link);
		if(!StringUtil.isNullEmptyString(imageUrl))
			new FetchImageAsync(imageNewsView).execute(imageUrl);
		return card;
	}

}
