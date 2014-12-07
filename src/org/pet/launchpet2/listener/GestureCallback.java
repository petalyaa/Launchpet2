package org.pet.launchpet2.listener;

import android.view.MotionEvent;

public interface GestureCallback {
	
	public void onSingleTap(MotionEvent event);
	
	public void onDoubleTap(MotionEvent event);

}
