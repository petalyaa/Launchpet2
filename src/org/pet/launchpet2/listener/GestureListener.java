package org.pet.launchpet2.listener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
	
	private boolean isLongPress = false;
	
	public GestureListener() {
	}

	@Override
	public void onLongPress(MotionEvent e) {
		//super.onLongPress(e);
		Log.v("Launchpet2", "Long press detected...");
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
    public boolean onDown(MotionEvent e) {
		isLongPress = false;
        return false;
    }
	
}
