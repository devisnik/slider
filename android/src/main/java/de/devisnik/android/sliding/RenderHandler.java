package de.devisnik.android.sliding;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceHolder;

public class RenderHandler extends Handler {
	private static final int MAX_FPS = 60;
	private static final int TARGET_DURATION = 1000 / MAX_FPS;
	private static final int ACTION_REDRAW = 1;
	private static final String TAG = "RenderHandler";
	long lastDraw = 0;
	private final SurfaceHolder mHolder;
	private final FrameDrawer mFrameDrawer;

	public RenderHandler(final Looper looper, final FrameDrawer frameDrawer, final SurfaceHolder holder) {
		super(looper);
		mFrameDrawer = frameDrawer;
		mHolder = holder;
	}

	@Override
	public void handleMessage(final Message msg) {
		if (msg.what == ACTION_REDRAW)
			redraw();
		else
			Logger.e(TAG, "unable to handle message: " + msg);
	}

	private void redraw() {

		long startTime = SystemClock.uptimeMillis();
		Canvas c = null;
		try {
			lastDraw = startTime;
			Rect dirtyRegion = mFrameDrawer.getDirtyRegion();
			c = mHolder.lockCanvas(dirtyRegion);
			if (c != null)
				mFrameDrawer.draw(c, dirtyRegion);
			// if (Logger.isDebugEnabled())
			// mFrameDrawer.drawFpsInfo(c, dirtyRegion.centerX(), dirtyRegion.centerY());
		} finally {
			// do this in a finally so that if an exception is thrown
			// during the above, we don't leave the Surface in an
			// inconsistent state
			if (c != null)
				mHolder.unlockCanvasAndPost(c);
			long drawDuration = SystemClock.uptimeMillis() - startTime;
			long delay = Math.max(1, TARGET_DURATION - drawDuration);
			sendMessageDelayed(obtainMessage(ACTION_REDRAW), delay);
		}
	}

	public void postDraw() {
		sendMessage(obtainMessage(ACTION_REDRAW));
	}
}
