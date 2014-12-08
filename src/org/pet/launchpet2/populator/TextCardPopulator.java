package org.pet.launchpet2.populator;

import org.jsoup.Jsoup;
import org.pet.launchpet2.R;
import org.pet.launchpet2.model.HomeNewsItem;
import org.pet.launchpet2.thread.FetchFaviconAsync;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
		TextView contentTextView = (TextView) card.findViewById(R.id.card_text_content_label);
		RelativeLayout titleHolder = (RelativeLayout) card.findViewById(R.id.card_text_title_holder);
		int titleBackgroundColor = getTitleBackgroundColor();
		if(titleBackgroundColor != 0)
			titleHolder.setBackgroundColor(titleBackgroundColor);
		imageView.setImageResource(R.drawable.rss);
		new FetchFaviconAsync(imageView).execute(item.get("parentLink"));
		titleTextView.setText(item.getTitle());
		String text = item.getContent();
		text = Jsoup.parse(text).text();
		contentTextView.setText("\"" + text + "…\"");
		return card;
	}

}
