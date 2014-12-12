package org.pet.launchpet2.animation.viewpagertransformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class FlowViewPagerTransformer implements ViewPager.PageTransformer {

	@Override
	public void transformPage(View view, float position) {
		view.setRotationY(position * -30f);		
	}

}
