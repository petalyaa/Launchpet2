package org.pet.launchpet2.layout;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class GifMovieViewer extends View {
	
	private long mMoviestart;
	
	private Movie mMovie;

	public GifMovieViewer(Context context) {
		super(context);
	}

	public GifMovieViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setMovie(InputStream is) {
		mMovie = Movie.decodeStream(is);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mMovie == null)
			return;
		canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        final long now = SystemClock.uptimeMillis();
        if (mMoviestart == 0) { 
            mMoviestart = now;
        }
        final int relTime = (int)((now - mMoviestart) % mMovie.duration());
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 10, 10);
        this.invalidate();
	}

}
