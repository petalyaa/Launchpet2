package org.pet.launchpet2.thread;

import org.pet.launchpet2.util.BitmapUtil;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FetchImageAsync extends AsyncTask<String, Void, Bitmap> {
	
	private ImageView imageView;
	
	public FetchImageAsync(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		return BitmapUtil.getFeedImageContent(urls[0]);
	}
	
	@Override
    protected void onPostExecute(Bitmap image) {
		imageView.setImageBitmap(image);
	}

}
