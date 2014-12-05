package org.pet.launchpet2.thread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pet.launchpet2.util.StringUtil;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherServiceThread extends AsyncTask<LocationManager, Void, String> {

	private TextView mLocTxtView;

	private Context context;
	
	private ImageView mWeatherIcon;

	public WeatherServiceThread(Context context, TextView mLocTxtView, ImageView mWeatherIcon) {
		this.mLocTxtView = mLocTxtView;
		this.context = context;
		this.mWeatherIcon = mWeatherIcon;
	}

	@Override
	protected String doInBackground(LocationManager... params) {
		LocationManager mLocManager = params[0];
		Location location = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		List<String>  providerList = mLocManager.getAllProviders();
		String locationStr = null;
		if(null != location && null != providerList && providerList.size()>0){                 
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());                 
			try {
				List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
				if(null!=listAddresses&&listAddresses.size()>0){
					locationStr = listAddresses.get(0).getLocality();
				}
				String weatherJson = getCurrentWeather(longitude, latitude);
				JSONObject restRespJsonObj = new JSONObject(weatherJson);
				JSONArray weatherArr = restRespJsonObj.getJSONArray("weather");
				JSONObject weatherJsonObj = weatherArr.getJSONObject(0);
				String weatherStr = weatherJsonObj.getString("main");
				String iconStr = weatherJsonObj.getString("icon");
				String iconUrl = "http://openweathermap.org/img/w/" + iconStr + ".png";
				new FetchImageAsync(mWeatherIcon, false).execute(iconUrl);
				locationStr = locationStr + " (" + weatherStr + ")";
 			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return locationStr;
	}

	private String getCurrentWeather(double longitude, double latitude) {
		String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude;
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		String responseString = null;
		try {
			response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String locationStr) {
		super.onPostExecute(locationStr);
		if(!StringUtil.isNullEmptyString(locationStr)) {
			mLocTxtView.setText(locationStr);
			mLocTxtView.setVisibility(View.VISIBLE);
		} else
			mLocTxtView.setVisibility(View.INVISIBLE);
	}

}
