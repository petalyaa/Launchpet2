package org.pet.launchpet2.layout;

import org.pet.launchpet2.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CardLayout extends RelativeLayout {

	public CardLayout(Context context) {
		super(context);
	}
	
	public CardLayout(Context context, AttributeSet attrs) {
		super(context, attrs, R.attr.cardStyleRef);
	}

	public CardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, null, R.attr.cardStyleRef);
	}

}
