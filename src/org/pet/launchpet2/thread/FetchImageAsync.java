package org.pet.launchpet2.thread;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.pet.launchpet2.listener.FetchBitmapTaskCallbackListener;
import org.pet.launchpet2.util.BitmapUtil;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FetchImageAsync extends AsyncTask<String, Void, Bitmap> {
	
	private ImageView imageView;
	
	private FetchBitmapTaskCallbackListener listener;
	
	private String url;
	
	private boolean isGif = false;
	
	private boolean isRequireMimeChecking;
	
	public FetchImageAsync(ImageView imageView, boolean isRequireMimeChecking) {
		this.imageView = imageView;
		this.isRequireMimeChecking = isRequireMimeChecking;
	}
	
	public FetchImageAsync(FetchBitmapTaskCallbackListener listener, boolean isRequireMimeChecking) {
		this.listener = listener;
		this.isRequireMimeChecking = isRequireMimeChecking;
	}
	
	private boolean isGif(String url) {
		String type = null;
	    URL u = null;
	    URLConnection uc = null;
	    boolean isGif = false;
	    try {
	    	u = new URL(url);
			uc = u.openConnection();
			type = uc.getContentType();
			if(type.equalsIgnoreCase("image/gif"))
				isGif = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return isGif;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		url = urls[0];
		if(isRequireMimeChecking)
			isGif = isGif(url);
		return BitmapUtil.getFeedImageContent(url);
	}
	
	@Override
    protected void onPostExecute(Bitmap image) {
		if(imageView != null)
			imageView.setImageBitmap(image);
		if(listener != null)
			listener.onComplete(isGif, url, image);
	}

}
