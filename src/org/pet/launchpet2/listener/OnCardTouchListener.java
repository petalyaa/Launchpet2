package org.pet.launchpet2.listener;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnCardTouchListener implements OnTouchListener {
	
	private static final double TENSION = 100;
	
    private static final double DAMPER = 20; //friction
	
	private boolean isAlreadyMove = false;
	
	private Spring spring;
	
	public OnCardTouchListener(final View view) {
		SpringSystem springSystem = SpringSystem.create();
		spring = springSystem.createSpring();
		SpringConfig config = new SpringConfig(TENSION, DAMPER);
		spring.setSpringConfig(config);
		spring.addListener(new SpringListener() {
			
			@Override
			public void onSpringUpdate(Spring spring) {
				float value = (float) spring.getCurrentValue();
		        float scale = 1f - (value * 0.1f);
		        view.setScaleX(scale);
		        view.setScaleY(scale);
			}
			
			@Override
			public void onSpringEndStateChange(Spring arg0) {
				
			}
			
			@Override
			public void onSpringAtRest(Spring arg0) {
				
			}
			
			@Override
			public void onSpringActivate(Spring arg0) {
				
			}
		});
	}
	
	@Override
	public boolean onTouch(final View view, MotionEvent motionEvent) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			isAlreadyMove = false;
			spring.setEndValue(1f);
			break;
		case MotionEvent.ACTION_UP:
			spring.setEndValue(0f);
			if(!isAlreadyMove)
				view.performClick();
			break;
		case MotionEvent.ACTION_MOVE:
			spring.setEndValue(0f);
			isAlreadyMove = true;
			break;
		case MotionEvent.ACTION_CANCEL:
			spring.setEndValue(0f);
			break;
		}
		return true;
	}

}
