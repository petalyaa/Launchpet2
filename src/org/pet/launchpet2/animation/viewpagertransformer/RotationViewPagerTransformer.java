package org.pet.launchpet2.animation.viewpagertransformer;

import android.view.View;
import android.support.v4.view.ViewPager;

public class RotationViewPagerTransformer implements ViewPager.PageTransformer {

	private float minAlpha;
	private int degrees;
	private float distanceToCentreFactor;

	public RotationViewPagerTransformer() {
		this(165, .7f);
	}

	public RotationViewPagerTransformer(int degrees) {
		this(degrees, 0.7f);
	}

	public RotationViewPagerTransformer(int degrees, float minAlpha) {
		this.degrees = degrees;
		distanceToCentreFactor = (float) Math.tan(Math.toRadians(degrees / 2)) / 2;
		this.minAlpha = minAlpha;
	}

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();
		view.setPivotX((float) pageWidth / 2);
		view.setPivotY((float) (pageHeight + pageWidth * distanceToCentreFactor));

		if (position < -1) { // [-infinity,1)
			// off to the left by a lot
			view.setRotation(0);
			view.setAlpha(0);
		} else if (position <= 1) { // [-1,1]
			view.setTranslationX((-position) * pageWidth); // shift the view
															// over
			view.setRotation(position * (180 - degrees)); // rotate it
			// Fade the page relative to its distance from the center
			view.setAlpha(Math.max(minAlpha, 1 - Math.abs(position) / 3));
		} else { // (1, +infinity]
					// off to the right by a lot
			view.setRotation(0);
			view.setAlpha(0);
		}
	}

}
