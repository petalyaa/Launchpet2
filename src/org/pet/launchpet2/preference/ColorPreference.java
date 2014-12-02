package org.pet.launchpet2.preference;

import java.util.HashMap;
import java.util.Map;

import org.pet.launchpet2.R;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ColorPreference extends Preference implements OnClickListener {

	private View mPrefColorPlaceholder;

	private Map<String, String> attributes;
	
	private int mSelectedColor;
	
	private ColorPicker picker;
	
	private SVBar svBar;
	
	private View pickerView;
	
	private String key;
	
	private TextView mLabel;
	
	private AlertDialog.Builder builder;
	
	private AlertDialog dialog;

	public ColorPreference(Context context) {
		super(context);
	}

	@SuppressLint("InflateParams")
	public ColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		attributes = new HashMap<String, String>();
		for (int i=0;i<attrs.getAttributeCount();i++) {
			String attr = attrs.getAttributeName(i);
			String val  = attrs.getAttributeValue(i);
			attributes.put(attr, val);
		}
		this.key = attributes.get("key");
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pickerView = inflater.inflate(R.layout.dialog_color_picker, null);
		picker = (ColorPicker) pickerView.findViewById(R.id.picker);
		svBar = (SVBar) pickerView.findViewById(R.id.svbar);
		picker.addSVBar(svBar);
	}

	private String sanitizeString(String s) {
		if(s.startsWith("@")) {
			s = getContext().getString(Integer.parseInt(s.substring(1, s.length())));
		}
		return s;
	}

	private String getColorTitle() {
		String title = attributes.get("title");
		return sanitizeString(title);
	}
	
	private void saveColor(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(this.key, mSelectedColor);
        editor.commit();
	}
	
	private int getColor(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		return prefs.getInt(this.key, Color.BLACK);
	}
	
	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.preference_color, parent, false);
		mPrefColorPlaceholder = view.findViewById(R.id.preference_color_display);
		mLabel = (TextView) view.findViewById(R.id.preference_color_title);
		mSelectedColor = getColor(getContext());
		mPrefColorPlaceholder.setBackgroundColor(mSelectedColor);
		mLabel.setText(getColorTitle());
		picker.setColor(mSelectedColor);
		view.setOnClickListener(this);
		
		builder = new AlertDialog.Builder(getContext());
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mSelectedColor = picker.getColor();
				mPrefColorPlaceholder.setBackgroundColor(mSelectedColor);
				saveColor(getContext());
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		builder.setTitle(getColorTitle());
		builder.setView(pickerView);
		dialog = builder.create();
		
		return view;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		dialog.show();
	}

}
