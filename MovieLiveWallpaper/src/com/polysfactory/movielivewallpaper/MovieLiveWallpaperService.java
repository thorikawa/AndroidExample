package com.polysfactory.movielivewallpaper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class MovieLiveWallpaperService extends WallpaperService {
	private static final String TAG = "MovieLiveWallpaperService";

	@Override
	public Engine onCreateEngine() {
		return new MovieLiveWallpeperEngine();
	}

	class MySurfaceHolder implements SurfaceHolder {

		private SurfaceHolder surfaceHolder;

		public MySurfaceHolder(SurfaceHolder surfaceHolder) {
			this.surfaceHolder = surfaceHolder;
		}

		@Override
		public void addCallback(Callback callback) {
			surfaceHolder.addCallback(callback);
		}

		@Override
		public Surface getSurface() {
			return surfaceHolder.getSurface();
		}

		@Override
		public Rect getSurfaceFrame() {
			return surfaceHolder.getSurfaceFrame();
		}

		@Override
		public boolean isCreating() {
			return surfaceHolder.isCreating();
		}

		@Override
		public Canvas lockCanvas() {
			return surfaceHolder.lockCanvas();
		}

		@Override
		public Canvas lockCanvas(Rect dirty) {
			return surfaceHolder.lockCanvas(dirty);
		}

		@Override
		public void removeCallback(Callback callback) {
			surfaceHolder.removeCallback(callback);
		}

		@Override
		public void setFixedSize(int width, int height) {
			surfaceHolder.setFixedSize(width, height);
		}

		@Override
		public void setFormat(int format) {
			surfaceHolder.setFormat(format);
		}

		@Override
		public void setKeepScreenOn(boolean screenOn) {
			return;
		}

		@Override
		public void setSizeFromLayout() {
			surfaceHolder.setSizeFromLayout();
		}

		@Override
		public void setType(int type) {
			surfaceHolder.setType(type);
		}

		@Override
		public void unlockCanvasAndPost(Canvas canvas) {
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	class MovieLiveWallpeperEngine extends Engine {

		private MediaPlayer mp;

		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mp = new MediaPlayer();
			mp.setDisplay(new MySurfaceHolder(holder));
			mp.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			try {
				mp.setDataSource(MovieLiveWallpaperService.this, Uri
				        .parse("content://media/external/video/media/1"));
			} catch (Exception e) {
				Log.e(TAG, "error");
			}
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.stop();
				}
			});
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (mp != null) {
				mp.stop();
				mp.release();
			}
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible) {
				play();
			}
		}

		private void play() {
			if (mp.isPlaying()) {
				mp.stop();
			}
			try {
				mp.prepareAsync();
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "error");
			} catch (SecurityException e) {
				Log.e(TAG, "error");
			} catch (IllegalStateException e) {
				Log.e(TAG, "error");
			}
		}
	}
}