package org.pet.launchpet2.thread;

import org.pet.launchpet2.util.BitmapUtil;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FetchFaviconAsync extends AsyncTask<String, Void, Bitmap> {
	
	private ImageView imageView;
	
	public FetchFaviconAsync(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		return BitmapUtil.getFavicon(urls[0]);
	}
	
	@Override
    protected void onPostExecute(Bitmap image) {
		imageView.setImageBitmap(image);
	}

}
