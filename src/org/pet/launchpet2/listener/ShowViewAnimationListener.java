package org.pet.launchpet2.listener;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class ShowViewAnimationListener implements AnimationListener {
	
	private View view;
	
	public ShowViewAnimationListener(View view) {
		this.view = view;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		view.setVisibility(View.VISIBLE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	
	
}
