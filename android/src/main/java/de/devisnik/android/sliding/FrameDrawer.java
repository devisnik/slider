package de.devisnik.android.sliding;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import de.devisnik.android.sliding.tile.Tile;
import de.devisnik.android.sliding.tile.TileStore;
import de.devisnik.sliding.FrameFactory;
import de.devisnik.sliding.IRobotFrame;
import de.devisnik.sliding.Point;

public class FrameDrawer {

	private static final String TAG = "FrameDrawer";
	private final IRobotFrame itsFrame;
	private final Replayer itsReplayer;
	private final TileStore itsTileStore;
	private final FPSCounter mCounter = new FPSCounter();
	private final Rect mFpsDisplayBackArea = new Rect(0, 40, 50, 50);
	private final Paint mBackPaint;
	private final Paint mTextPaint;

	public FrameDrawer(final Settings settings, final int width, final int height, final boolean preview) {
		Point frameSize = settings.getFrameSize(width, height);
		itsFrame = createFrame(frameSize);
		itsTileStore = new TileStore(width, height, settings, itsFrame);
		itsReplayer = new Replayer(itsFrame, itsTileStore, settings.getSpeed(), preview);
		mTextPaint = createPaint(Color.WHITE);
		mTextPaint.setTextSize(24);
		mBackPaint = createPaint(Color.BLACK);
	}

	private Paint createPaint(final int color) {
		Paint paint = new Paint();
		paint.setColor(color);
		return paint;
	}

	private IRobotFrame createFrame(final Point frameSize) {
		return FrameFactory.createRobot(frameSize.x, frameSize.y, new ARandom());
	}

	public void start() {
		Logger.d(TAG, "starte replayer");
		itsReplayer.start();
	}

	public void stop() {
		Logger.d(TAG, "stoppe replayer");
		itsReplayer.pause();
	}

	public void handleTap() {
		Logger.d(TAG, "handle tap");
		itsReplayer.onClick();
	}

	public Rect getDirtyRegion() {
		Rect dirtyRect = new Rect();
		for (Tile tile : itsTileStore)
			if (tile.isDirty())
				dirtyRect.union(tile.getDirtyRegion());
		return dirtyRect;
	}

	public void draw(final Canvas canvas, final Rect dirtyRegion) {
		if (Logger.isDebugEnabled())
			mCounter.inc();
		canvas.drawColor(0xff000000);
		for (Tile tile : itsTileStore)
			tile.draw(canvas);
	}

	private void drawFpsInfo(final Canvas canvas, final int x, final int y) {
		mFpsDisplayBackArea.set(x, y - 20, x + 90, y);
		canvas.drawRect(mFpsDisplayBackArea, mBackPaint);
		canvas.drawText("FPS: " + mCounter.getFPS(), x, y, mTextPaint);
	}

	public void draw(final SurfaceHolder holder) {
		Canvas canvas = null;
		try {
			Rect dirtyRegion = getDirtyRegion();
			canvas = holder.lockCanvas(dirtyRegion);
			if (canvas == null)
				return;
			draw(canvas, dirtyRegion);
			if (Logger.isDebugEnabled())
				drawFpsInfo(canvas, dirtyRegion.centerX(), dirtyRegion.centerY());
		} finally {
			if (canvas != null)
				holder.unlockCanvasAndPost(canvas);
		}
	}

}
