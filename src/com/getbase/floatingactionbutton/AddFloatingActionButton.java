package com.getbase.floatingactionbutton;

import org.pet.launchpet2.R;
import org.pet.launchpet2.listener.GestureCallback;
import org.pet.launchpet2.listener.GestureListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class AddFloatingActionButton extends FloatingActionButton {

	private GestureDetector gestureDetector;

	int mPlusColor;

	public AddFloatingActionButton(Context context) {
		this(context, null);
	}

	public AddFloatingActionButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetector = new GestureDetector(context, new GestureListener());
	}

	public AddFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
//		boolean isGestureDetected = gestureDetector.onTouchEvent(e);
//		if(isGestureDetected) {
//			return true;
//		} else {
//			Log.v("Launchpet2", "Handling default touch event");
			return super.onTouchEvent(e);
//		}
	}

	@Override
	void init(Context context, AttributeSet attributeSet) {
		if (attributeSet != null) {
			TypedArray attr = context.obtainStyledAttributes(attributeSet,
					R.styleable.AddFloatingActionButton, 0, 0);
			if (attr != null) {
				try {
					mPlusColor = attr
							.getColor(
									R.styleable.AddFloatingActionButton_fab_plusIconColor,
									getColor(android.R.color.white));
				} finally {
					attr.recycle();
				}
			}
		} else {
			mPlusColor = getColor(android.R.color.white);
		}

		super.init(context, attributeSet);
	}

	@Override
	Drawable getIconDrawable() {
		final float iconSize = getDimension(R.dimen.fab_icon_size);
		final float iconHalfSize = iconSize / 2f;

		final float plusSize = getDimension(R.dimen.fab_plus_icon_size);
		final float plusHalfStroke = getDimension(R.dimen.fab_plus_icon_stroke) / 2f;
		final float plusOffset = (iconSize - plusSize) / 2f;

		final Shape shape = new Shape() {
			@Override
			public void draw(Canvas canvas, Paint paint) {
				canvas.drawRect(plusOffset, iconHalfSize - plusHalfStroke,
						iconSize - plusOffset, iconHalfSize + plusHalfStroke,
						paint);
				canvas.drawRect(iconHalfSize - plusHalfStroke, plusOffset,
						iconHalfSize + plusHalfStroke, iconSize - plusOffset,
						paint);
			}
		};

		ShapeDrawable drawable = new ShapeDrawable(shape);

		final Paint paint = drawable.getPaint();
		paint.setColor(mPlusColor);
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);

		return drawable;
	}
}
