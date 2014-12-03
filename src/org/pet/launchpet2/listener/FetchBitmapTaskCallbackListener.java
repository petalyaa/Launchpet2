package org.pet.launchpet2.listener;

import android.graphics.Bitmap;

public interface FetchBitmapTaskCallbackListener {
	
	public void onComplete(boolean isGif, String url, Bitmap bitmap);

}
