package org.pet.launchpet2.listener;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class HideViewAnimationListener implements AnimationListener {
	
	private View view;
	
	public HideViewAnimationListener(View view) {
		this.view = view;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		view.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

}
