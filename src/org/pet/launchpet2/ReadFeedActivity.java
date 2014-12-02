package org.pet.launchpet2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pet.launchpet2.animation.SplitAnimation;
import org.pet.launchpet2.thread.FetchFaviconAsync;
import org.pet.launchpet2.thread.FetchImageAsync;
import org.pet.launchpet2.util.StringUtil;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ortiz.touch.ExtendedViewPager;
import com.ortiz.touch.TouchImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReadFeedActivity extends FragmentActivity {
	
	private TextView mToolbarTitle;
	
	private ImageButton mToolbarBackButton;
	
	private ImageView mContentImage;
	
	private ImageView mContentSourceIcon;
	
	private TextView mContentSourceName;
	
	private TextView mContentSourceDate;
	
	private TextView mContentText;
	
	private FloatingActionButton mFollowLinkBtn;

	private ExtendedViewPager mImageViewPager;
	
	private PagerAdapter mPagerAdapter;
	
	private RelativeLayout mOverlayImageView;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_feed);
		
		mToolbarTitle = (TextView) findViewById(R.id.read_feed_toolbar_title);
		mToolbarBackButton = (ImageButton) findViewById(R.id.read_feed_toolbar_back_btn);
		mContentImage = (ImageView) findViewById(R.id.read_feed_content_image);
		mContentSourceIcon = (ImageView) findViewById(R.id.read_feed_content_source_icon);
		mContentSourceName = (TextView) findViewById(R.id.read_feed_content_source_name);
		mContentSourceDate = (TextView) findViewById(R.id.read_feed_content_source_date);
		mContentText = (TextView) findViewById(R.id.read_feed_content_text);
		mFollowLinkBtn = (FloatingActionButton) findViewById(R.id.read_feed_content_follow_link);
		mImageViewPager = (ExtendedViewPager) findViewById(R.id.read_feed_image_pager);
		mOverlayImageView = (RelativeLayout) findViewById(R.id.read_feed_overlay_image_view);
		mOverlayImageView.setVisibility(View.INVISIBLE);
		mOverlayImageView.setOnClickListener(new OnOverlayImageClick());
		Bundle b = getIntent().getExtras();
		HashMap<String, String> item = (HashMap<String, String>) b.getSerializable("home_news_item");
		String title = item.get("title");
		String parentTitle = item.get("parentTitle");
		String parentLink = item.get("parentLink");
		String description = item.get("description");
		String pubDate = item.get("pubDate");
		String link = item.get("link");
		Document doc = Jsoup.parse(description);
		Elements imgs = doc.getElementsByTag("img");
		String firstImgUrl = null;
		List<String> imageSrcUrlList = new ArrayList<String>();
		for (Element el : imgs) {
			String src = el.absUrl("src");
			if(StringUtil.isNullEmptyString(firstImgUrl))
				firstImgUrl = src;
			imageSrcUrlList.add(src);
		}
		if(!StringUtil.isNullEmptyString(parentLink))
			new FetchFaviconAsync(mContentSourceIcon).execute(parentLink);
		if(!StringUtil.isNullEmptyString(firstImgUrl))
			new FetchImageAsync(mContentImage).execute(firstImgUrl);
		
		String displayContent = Html.fromHtml(description.replaceAll("<img.+?>", "")).toString();
		mPagerAdapter = new ImageSlidePagerAdapter(getSupportFragmentManager(), imageSrcUrlList);
		mImageViewPager.setAdapter(mPagerAdapter);
		mContentSourceDate.setText(pubDate);
		mContentSourceName.setText(parentTitle);
		mContentText.setText(displayContent);
		mToolbarTitle.setText(Html.fromHtml(title));
		mToolbarBackButton.setOnClickListener(new OnToolbarBackClickListener());
		mContentImage.setOnClickListener(new OnContentImageClickListener(imageSrcUrlList));
		mFollowLinkBtn.setOnClickListener(new OnFollowLinkClickListiner(link));
	}
	
	@Override
	public void onBackPressed() {
		if(mOverlayImageView.getVisibility() == View.VISIBLE)
			mOverlayImageView.setVisibility(View.INVISIBLE);
		else
			super.onBackPressed();
	}

	@Override
    protected void onStop() {
		SplitAnimation.cancel();
        super.onStop();
    }
	
	private class OnFollowLinkClickListiner implements OnClickListener {
		
		private String link;
		
		public OnFollowLinkClickListiner(String link) {
			this.link = link;
		}

		@Override
		public void onClick(View v) {
			if (!link.startsWith("http://") && !link.startsWith("https://"))
				link = "http://" + link;
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			startActivity(browserIntent);
		}
		
	}
	
	private class OnContentImageClickListener implements OnClickListener {
		
		public List<String> imageUrlList;
		
		public OnContentImageClickListener(List<String> imageUrlList) {
			this.imageUrlList = imageUrlList;
		}

		@Override
		public void onClick(View v) {
			if(imageUrlList != null && imageUrlList.size() > 0) {
				mOverlayImageView.setVisibility(View.VISIBLE);
				mOverlayImageView.bringToFront();
			}
		}
		
	}
	
	private class OnToolbarBackClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}
		
	}
	
	private class OnOverlayImageClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			mOverlayImageView.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private class ImageSlidePagerAdapter extends FragmentStatePagerAdapter {
		
		private List<String> imageUrlList;
		
		public ImageSlidePagerAdapter(FragmentManager fm, List<String> imageUrlList) {
            super(fm);
            this.imageUrlList = imageUrlList;
        }

		@Override
		public int getCount() {
			return imageUrlList != null ? imageUrlList.size() : 0;
		}

		@Override
		public Fragment getItem(int position) {
			String urlStr = imageUrlList.get(position);
			return new ImageScreenPageFragment(urlStr);
		}
		
	}
	
	private class ImageScreenPageFragment extends Fragment {
		
		private String urlStr;
		
		public ImageScreenPageFragment(String urlStr) {
			this.urlStr = urlStr;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.image_slides_page_view, container, false);
			TouchImageView mImgView = (TouchImageView) rootView.findViewById(R.id.image_slides_page_image_view);
			new FetchImageAsync(mImgView).execute(urlStr);
			return rootView;
		}
	}

}
