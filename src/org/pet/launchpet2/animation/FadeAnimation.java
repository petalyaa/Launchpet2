package org.pet.launchpet2.animation;

import org.pet.launchpet2.R;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

public class FadeAnimation {
	
	public static final Animation getFadeInAnimation(Context context) {
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(context.getResources().getInteger(R.integer.anim_slide_duration));
		return fadeIn;
	}
	
	public static final Animation getFadeOutAnimation(Context context) {
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setStartOffset(1000);
		fadeOut.setDuration(context.getResources().getInteger(R.integer.anim_slide_duration));
		return fadeOut;
	}

}
