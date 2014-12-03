package org.pet.launchpet2.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class GifWebView extends WebView {

	public GifWebView(Context context) {
		super(context);
	}
	
	public GifWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void play(String path) {
		loadUrl(path);
	}

}
