package org.pet.launchpet2.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.pet.launchpet2.layout.GifWebView;

import android.os.AsyncTask;
import android.view.View;

public class GifImageLoader extends AsyncTask<String, Void, InputStream> {
	
	private GifWebView gifImageView;
	
	public GifImageLoader(GifWebView gifImageView) {
		this.gifImageView = gifImageView;
	}

	@Override
	protected InputStream doInBackground(String... urls) {
		URL url = null;
		InputStream in = null;
		try {
			url = new URL(urls[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(25000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.connect();
			in = conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	protected void onPostExecute(InputStream in) {
		if(gifImageView != null && in != null) {
			gifImageView.setVisibility(View.VISIBLE);
		}
	}

}
