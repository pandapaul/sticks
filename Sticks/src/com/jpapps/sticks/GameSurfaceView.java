package com.jpapps.sticks;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	class GameThread extends Thread {
		
		private boolean mRun = false;
		private SurfaceHolder mSurfaceHolder;
		private Context mContext;
		private Handler mHandler;
		private int mCanvasWidth;
		private int mCanvasHeight;
		
		public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
			mSurfaceHolder = surfaceHolder;
			mContext = context;
			mHandler = handler;
		}
		
		private void doDraw(Canvas canvas) {
			//wipe the canvas
            canvas.drawColor(0xff003300);
            
        }
		
        public void setSurfaceSize(int width, int height) {
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
            }
        }
		
		@Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        //if (mMode == STATE_RUNNING) updatePhysics();
                        doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
		
		public void setRunning(boolean run) {
			mRun = run;
		}

		public void pause() {
			synchronized (mSurfaceHolder) {
                //if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
            }
		}

		public Bundle saveState(Bundle outState) {
			// Save all important variables into the bundle
			return outState;
		}
		
	}
	
	private GameThread thread;
	private Context mContext;
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        thread = new GameThread(holder, context, new Handler());

        setFocusable(true); // make sure we get key events
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
	
	@Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
    }

	public GameThread getThread() {
		return thread;
	}
	
}