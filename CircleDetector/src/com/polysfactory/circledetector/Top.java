package com.polysfactory.circledetector;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Top extends Activity {
	private static final String TAG = "AirDraw";

	private SensorManager mSensorManager;

	private MapView mapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mapView = (MapView) findViewById(R.id.mapview);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mSensorEventListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorEventListener, mSensorManager
		        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}

	public void updateDisplay(boolean result) {
		TextView textView = (TextView) findViewById(R.id.result);
		int res = 0;
		if (result) {
			res = R.raw.maru;
			textView.setText(R.string.is_circle);
		} else {
			res = R.raw.batsu;
			textView.setText(R.string.not_circle);
		}
		MediaPlayer mediaPlayer = MediaPlayer.create(this, res);
		if (mediaPlayer != null) {
			mediaPlayer.start();
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}

	Handler mHandler = new Handler();

	private ShakeSensorEventListener mSensorEventListener = new ShakeSensorEventListener();

	class Point {
		public float x;

		public float y;

		public float z;

		public double length = -1;

		public Point(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public double getLength() {
			if (length == -1) {
				length = Math.pow(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2), 0.5F);
			}
			return length;
		}
	}

	class CircleRecognizer {
		private Vector<Point> points;

		private double mean = -1;

		public CircleRecognizer(Vector<Point> points) {
			this.points = points;
		}

		/**
		 * 平均を求める
		 * 
		 * @return
		 */
		public double getMeanOfLength() {
			if (mean == -1) {
				double totalLength = 0;
				for (Point point : points) {
					totalLength += point.getLength();
				}
				mean = totalLength / (double) points.size();
			}
			return mean;
		}

		/**
		 * 分散を求める
		 * 
		 * @return 分散を求める
		 */
		public double getVarianceOfLength() {
			double variance = 0;
			double mean = getMeanOfLength();
			for (Point point : points) {
				variance += Math.pow(point.getLength() - mean, 2);
			}
			return variance / (double) points.size();
		}

		public boolean isCircle() {
			boolean result = true;
			double var = getVarianceOfLength();
			int size = points.size();
			Log.i(TAG, "total size=" + size);
			Log.i(TAG, "mean=" + mean);
			Log.i(TAG, "variance=" + var);
			if (var > 4) {
				Log.i(TAG, "the variance of point's length is too big.");
				result &= false;
			}
			int[] counts = new int[8];
			int min = (int) (((double) size / 8F) * 0.8);
			for (Point point : points) {
				if (point.x > 0 && point.y > 0 && point.x > point.y)
					counts[0]++;
				else if (point.x > 0 && point.y > 0 && point.x < point.y)
					counts[1]++;
				else if (point.x > 0 && point.y < 0 && point.x > -point.y)
					counts[2]++;
				else if (point.x > 0 && point.y < 0 && point.x < -point.y)
					counts[3]++;
				else if (point.x < 0 && point.y > 0 && point.x > -point.y)
					counts[4]++;
				else if (point.x < 0 && point.y > 0 && point.x < -point.y)
					counts[5]++;
				else if (point.x < 0 && point.y < 0 && point.x > point.y)
					counts[6]++;
				else if (point.x < 0 && point.y < 0 && point.x < point.y)
					counts[7]++;
			}
			int ok = 0;
			for (int i = 0; i < 8; i++) {
				Log.i(TAG, "count[" + i + "]=" + counts[i]);
				if (counts[i] >= min) {
					ok++;
				}
			}
			if (ok >= 6) {
				result &= true;
			} else {
				Log.d(TAG, "ok area count is small:" + ok);
				result &= false;
			}
			if (result) {
				Log.d(TAG, "is Circle!!");
				return true;
			} else {
				Log.d(TAG, "NG!!");
				return false;
			}
		}
	}

	class ShakeSensorEventListener implements SensorEventListener {

		private static final float FILTERING_VALUE = 0.1f;

		private float lowX, lowY, lowZ;

		private volatile boolean mNowShaking = false;

		private long lastSlow;

		private float mSpeedThreshold = 4;

		private static final int INTERVAL = 200 * 1000 * 1000;

		private Vector<Point> points = new Vector<Point>();

		@Override
		public void onSensorChanged(SensorEvent event) {
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			// ローパスフィルタ処理
			lowX = x * FILTERING_VALUE + lowX * (1.0f - FILTERING_VALUE);
			lowY = y * FILTERING_VALUE + lowY * (1.0f - FILTERING_VALUE);
			lowZ = z * FILTERING_VALUE + lowZ * (1.0f - FILTERING_VALUE);
			// ハイパスフィルタ処理
			float hx = x - lowX;
			float hy = y - lowY;
			float hz = z - lowZ;

			if (mNowShaking) {
				final Point p = new Point(hx, hy, hz);
				points.add(p);
				// Log.d(TAG, "#(x,y,z)=" + "(" + hx + "," + hy + "," + hz +
				// ") len=" + p.getLength());
				// mapView.paintPoint(p);
				mapView.paintPoint(points);
			}

			if (Math.abs(hx) + Math.abs(hy) + Math.abs(hz) < mSpeedThreshold) {
				// Log.d(TAG, "interval time:" + (event.timestamp - lastSlow));
				if (event.timestamp - lastSlow > INTERVAL) {
					// 再生後初回の停止時刻より一定時間が経過していたら
					lastSlow = Long.MAX_VALUE;
					// 今の運動が円運動だったかどうかを判定
					if (points.size() > 20) {
						CircleRecognizer recognizer = new CircleRecognizer(points);
						final boolean result = recognizer.isCircle();
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								updateDisplay(result);
							}
						});
					} else {
						Log.i(TAG, "the number of points is too small:" + points.size());
					}
					points.clear();
					mNowShaking = false;
				} else {
					// 再生後初回の停止時刻記録
					if (mNowShaking && lastSlow > event.timestamp) {
						lastSlow = event.timestamp;
					}
				}
			} else {
				Log.i(TAG, "over threashold");
				// 閾値ごえ
				lastSlow = Long.MAX_VALUE;
				if (!mNowShaking) {
					mNowShaking = true;
					// 蓄積されたデータをクリア
					mapView.reset();
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	public void pause(View view) {
		mSensorManager.unregisterListener(mSensorEventListener);
	}

	public void resume(View view) {
		mSensorManager.registerListener(mSensorEventListener, mSensorManager
		        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}
}