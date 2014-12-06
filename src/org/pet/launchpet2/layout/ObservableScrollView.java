package org.pet.launchpet2.layout;
/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class ObservableScrollView extends ScrollView {
    private Callbacks mCallbacks;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t);
            if(!isTouchRelease) {
	            if(t > oldt)
	            	mCallbacks.onScrollUp();
	            else
	            	mCallbacks.onScrollDown();
            }
        }
    }
    
    private boolean isTouchRelease = true;

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                	isTouchRelease = false;
                	boolean moveResultDown = mCallbacks.onDownMotionEvent();
                    if(!moveResultDown)
                		return false;
                	else
                		break;
                case MotionEvent.ACTION_UP:
                	isTouchRelease = true;
                	boolean moveResultUp = mCallbacks.onUpMotionEvent();
                	if(!moveResultUp)
                		return false;
                	else
                		break;
                case MotionEvent.ACTION_MOVE:
                	boolean moveResultMove = mCallbacks.onActionMoveEvent();
                	if(!moveResultMove)
                		return false;
                	else
                		break;
                case MotionEvent.ACTION_CANCEL:
                	isTouchRelease = true;
                	boolean moveResultCancel = mCallbacks.onCancelMotionEvent();
                    if(!moveResultCancel)
                		return false;
                	else
                		break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public static interface Callbacks {
        public void onScrollChanged(int scrollY);
        public boolean onDownMotionEvent();
        public boolean onUpMotionEvent();
        public boolean onCancelMotionEvent();
        public boolean onActionMoveEvent();
        public void onScrollDown();
        public void onScrollUp();
    }
}
