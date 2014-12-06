package org.pet.launchpet2.populator;

import org.pet.launchpet2.R;
import org.pet.launchpet2.listener.ActionPerformedListener;
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

public class DzoneCardPopulator extends Populator {
	
	@SuppressWarnings("unused")
	private ActionPerformedListener actionPerformedListener;

	public DzoneCardPopulator(HomeNewsItem item, ActionPerformedListener actionPerformedListener) {
		super(item);
		this.actionPerformedListener = actionPerformedListener;
	}

	@SuppressLint("InflateParams")
	@Override
	public View populateView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.home_specific_dzone_card, null);
//		ImageView imageView = (ImageView) card.findViewById(R.id.card_dzone_icon_image);
		TextView titleTextView = (TextView) card.findViewById(R.id.card_dzone_title_label);
//		TextView dateTextView = (TextView) card.findViewById(R.id.card_dzone_date_label);
//		TextView contentTextView = (TextView) card.findViewById(R.id.card_dzone_content_label);
		ImageView imageNewsView = (ImageView) card.findViewById(R.id.card_dzone_image);
		TextView voteUpTextView = (TextView) card.findViewById(R.id.card_dzone_footer_vote_up_label);
		TextView voteDownTextView = (TextView) card.findViewById(R.id.card_dzone_footer_vote_down_label);
		TextView commentTextView = (TextView) card.findViewById(R.id.card_dzone_footer_comments_label);
		TextView clickTextView = (TextView) card.findViewById(R.id.card_dzone_footer_view_label);
		ImageView hostIcon = (ImageView) card.findViewById(R.id.card_dzone_title_icon_image);
//		TextView readMoreLink = (TextView) card.findViewById(R.id.card_dzone_read_more);

		String userImageUrlStr = item.getIconUrl();
		String thumbnailImageUrlStr = item.getImageUrl();
		if(!StringUtil.isNullEmptyString(userImageUrlStr))
//			new FetchImageAsync(imageView).execute(userImageUrlStr);
		if(!StringUtil.isNullEmptyString(thumbnailImageUrlStr))
			new FetchImageAsync(imageNewsView, false).execute(thumbnailImageUrlStr);
		String link = item.get("parentLink");
//		Bitmap favicon = BitmapUtil.getFavicon(link);
		if(!StringUtil.isNullEmptyString(link))
			new FetchFaviconAsync(hostIcon).execute(link);
//			hostIcon.setImageBitmap(favicon);
		
		titleTextView.setText(Html.fromHtml(item.getTitle()));
//		contentTextView.setText(item.getContent());
		voteUpTextView.setText(item.get("dz:voteUpCount"));
		voteDownTextView.setText(item.get("dz:voteDownCount"));
		commentTextView.setText(item.get("dz:commentCount"));
		clickTextView.setText(item.get("dz:clickCount"));
//		dateTextView.setText(ConfigurationUtil.NEWS_FEED_DATE_FORMAT.format(item.getDate()));
//		readMoreLink.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				String linkStr = item.get("link");
//				Parameter param = new Parameter();
//				param.put("url", linkStr);
//				actionPerformedListener.performAction(ConfigurationUtil.SIGNAL_OPEN_LINK_BROWSER, param);
//			}
//			
//		});
		return card;
	}
	
}
