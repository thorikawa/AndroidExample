package com.polysfactory.circledetector;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.polysfactory.circledetector.Top.Point;

public class MapView extends SurfaceView implements SurfaceHolder.Callback {

	private static final int WIDTH = 480;

	private static final int HEIGHT = 480;

	private SurfaceHolder mHolder;

	private Paint black;

	private Paint white;

	private Paint blue;

	private static final String TAG = "MapView";

	private Context mContext;

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		getHolder().addCallback(this);
		setFocusable(true);
		black = new Paint();
		black.setColor(Color.BLACK);
		white = new Paint();
		white.setColor(Color.WHITE);
		blue = new Paint();
		blue.setColor(Color.BLUE);
		blue.setTextSize(24);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		System.out.println("surfaceChanged");
		reset();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated");
		mHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed");
	}

	public synchronized void reset() {
		System.out.println("reset");
		if (mHolder != null) {
			Canvas canvas = mHolder.lockCanvas();
			if (canvas != null) {
				drawBack(canvas);
				mHolder.unlockCanvasAndPost(canvas);
			} else {
				Log.w(TAG, "canvas is null.");
			}
		} else {
			Log.w(TAG, "surface holder is null.");
		}
	}

	public synchronized void paintPoint(Vector<Point> points) {
		System.out.println("pointpaint");
		if (mHolder != null) {
			Canvas canvas = mHolder.lockCanvas();
			if (canvas != null) {
				drawBack(canvas);
				for (Point point : points) {
					canvas.drawCircle(point.x * 20 + WIDTH / 2, -(point.y * 20) + HEIGHT / 2, 5,
					        black);
				}
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	private void drawBack(Canvas canvas) {
		canvas.drawPaint(white);
		canvas.drawLine(0, 0, WIDTH, HEIGHT, blue);
		canvas.drawLine(0, WIDTH, HEIGHT, 0, blue);
		canvas.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2, blue);
		canvas.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT, blue);
		canvas.drawText("x", WIDTH - 20, HEIGHT / 2 - 20, blue);
		canvas.drawText("y", WIDTH / 2 + 20, 40, blue);
		canvas.drawText(mContext.getString(R.string.map_title), 20, 20, blue);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(WIDTH, HEIGHT);
		setMeasuredDimension(WIDTH, HEIGHT);
	}
}
