package org.pet.launchpet2.populator;

import org.pet.launchpet2.R;
import org.pet.launchpet2.model.HomeNewsItem;
import org.pet.launchpet2.util.ConfigurationUtil;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TextCardPopulator extends Populator {
	
	public TextCardPopulator(HomeNewsItem item) {
		super(item);
	}

	@SuppressLint("InflateParams")
	@Override
	public View populateView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.home_text_card, null);
		ImageView imageView = (ImageView) card.findViewById(R.id.card_text_icon_image);
		TextView titleTextView = (TextView) card.findViewById(R.id.card_text_title_label);
		TextView dateTextView = (TextView) card.findViewById(R.id.card_text_date_label);
		TextView contentTextView = (TextView) card.findViewById(R.id.card_text_content_label);
		if(item.getIcon() != null)
			imageView.setImageBitmap(item.getIcon());
		titleTextView.setText(item.getTitle());
		String text = Html.fromHtml(item.getContent()).toString();
		contentTextView.setText(text);
		dateTextView.setText(ConfigurationUtil.NEWS_FEED_DATE_FORMAT.format(item.getDate()));
		return card;
	}

}
