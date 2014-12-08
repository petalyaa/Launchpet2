package org.pet.launchpet2.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;

public class BitmapUtil {
	
	static enum BitmapType {
		FAVICON(ConfigurationUtil.SUBDIRECTORY_FAVICON), IMAGES(ConfigurationUtil.SUBDIRECTORY_IMAGES);
		
		private BitmapType(String s) {
			this.s = s;
		}
		
		public String getSubdirectory() {
			return s;
		}
		
		private String s;
	};
	
	public static final Bitmap getFavicon(String urlStr) {
		if(urlStr.startsWith("http://9gag.com/")) // Hardcoded for 9gag
			urlStr = "http://9gag.com/";
		if(urlStr.contains("cnn.com/")) // Hardcoded for cnn
			urlStr = "http://edition.cnn.com/";
		if(!urlStr.endsWith("/"))
			urlStr = urlStr + "/";
		urlStr = urlStr + "favicon.ico";
		return getBitmapFromCache(urlStr, BitmapType.FAVICON);
	}
	
	public static final Bitmap getFeedImageContent(String urlStr) {
		return getBitmapFromCache(urlStr, BitmapType.IMAGES);
	}
	
	public static final String constructUniqueName(String url) {
		return CommonUtil.getMD5(url);
	}
	
	public static final Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}
	
	public static final Bitmap getBitmapFromCache(String urlStr, BitmapType type) {
		Bitmap bmp = null;
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/" + ConfigurationUtil.APPLICATION_SD_DIRECTORY);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, ConfigurationUtil.SUBDIRECTORY_CACHE);
		if(!myDir.exists())
			myDir.mkdir();
		myDir = new File(myDir, type.getSubdirectory());
		if(!myDir.exists())
			myDir.mkdir();
		String fileName = constructUniqueName(urlStr);
		myDir = new File(myDir, fileName);
		if(myDir.exists()) {
			// Bitmap file exist in cache. No need to get from internet
			bmp = getBitmapFromFile(myDir);
		} else {
			// Bitmap file not exist in cache. No other choice, have to fetch from the net... :(
			bmp = getBitmapFromURL(urlStr);
			// Then we store it for future use... :)
			writeBitmapToFile(bmp, myDir);
		}
		return bmp;
	}
	
	public static final void writeBitmapToFile(Bitmap bmp, File file) {
		FileOutputStream out = null;
		try {
		    out = new FileOutputStream(file);
		    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (out != null) {
		            out.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	public static final Bitmap getBitmapFromURL(String urlStr) {
		Bitmap bmp = null;
		if(urlStr != null && !urlStr.equals("")) {
			try {
				URL url = new URL(urlStr);
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bmp;
	}
	
	public static final Bitmap getBitmapFromFile(File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		return bitmap;
	}
	
	public static final Bitmap decodeUriAndResize(Uri selectedImage, ContentResolver contentResolver, int size) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = size;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE
					|| height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o2);
	}
	
	public static final Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap, int borderWidth, int borderColor) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		final int width = bitmap.getWidth() + borderWidth;
		final int height = bitmap.getHeight() + borderWidth;

		Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);

		Canvas canvas = new Canvas(canvasBitmap);
		float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
		canvas.drawCircle(width / 2, height / 2, radius, paint);
		paint.setShader(null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(borderColor);
		paint.setStrokeWidth(borderWidth);
		canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
		return canvasBitmap;
	}

}
