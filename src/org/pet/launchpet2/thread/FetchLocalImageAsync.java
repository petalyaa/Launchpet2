package org.pet.launchpet2.thread;

import org.pet.launchpet2.util.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FetchLocalImageAsync extends AsyncTask<String, Void, Bitmap> {
	
	private Context context;
	
	private ImageView mImgView;
	
	public FetchLocalImageAsync(Context context, ImageView mImgView) {
		this.context = context;
		this.mImgView = mImgView;
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		String packageName = arg0[0];
		return BitmapUtil.getBitmapFromPackage(context, packageName);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if(result != null)
			mImgView.setImageBitmap(result);
	}
	
	

}
