package com.jpapps.sticks;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	class RenderingThread extends Thread {
		
		private SurfaceHolder mSurfaceHolder;
		private Context mContext;
		private Handler mHandler;
		private int mCanvasWidth;
		private int mCanvasHeight;
		private Bitmap playerIdle1;
		private Bitmap playerIdle2;
		private Bitmap playerIdle3;
		private Bitmap playerIdle1Right;
		private Bitmap playerIdle2Right;
		private Bitmap playerIdle3Right;
		
		private long sleepTime;
		private long delay = 180; //in milliseconds
		private int frame = 1;
		
		public final static int RUNNING = 1;
		public final static int PAUSED = 2;
		int state = RUNNING;
		
		public RenderingThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
			mSurfaceHolder = surfaceHolder;
			mContext = context;
			mHandler = handler;
			Resources res = mContext.getResources();
	        playerIdle1 = BitmapFactory.decodeResource(res, R.drawable.anim_stick_idle_1);
	        playerIdle2 = BitmapFactory.decodeResource(res, R.drawable.anim_stick_idle_2);
	        playerIdle3 = BitmapFactory.decodeResource(res, R.drawable.anim_stick_idle_3);
	        
	        Matrix m = new Matrix();
	        m.preScale(-1, 1);
	        playerIdle1Right = Bitmap.createBitmap(playerIdle1, 0, 0, playerIdle1.getWidth(), playerIdle1.getHeight(), m, false);
	        playerIdle2Right = Bitmap.createBitmap(playerIdle2, 0, 0, playerIdle2.getWidth(), playerIdle2.getHeight(), m, false);
	        playerIdle3Right = Bitmap.createBitmap(playerIdle3, 0, 0, playerIdle3.getWidth(), playerIdle3.getHeight(), m, false);
		}
		
		private void doDraw(Canvas canvas) {
			//wipe the canvas
			canvas.drawColor(0xffffffff);
            Rect r;
            
            //This is temporary/test animation code. Should switch over to using sprites in conjunction with a game engine. But first, sleep.
            
            switch (frame) {
            case 1:
            	r = new Rect(0, 0, mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight(), mCanvasHeight);
                canvas.drawBitmap(playerIdle1, null, r, null);
                r = new Rect(mCanvasWidth-(mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight()), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdle1Right, null, r, null);
                break;
            case 2:
            	r = new Rect(0, 0, mCanvasHeight*playerIdle2.getWidth()/playerIdle2.getHeight(), mCanvasHeight);
                canvas.drawBitmap(playerIdle2, null, r, null);
                r = new Rect(mCanvasWidth-(mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight()), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdle2Right, null, r, null);
                break;
            case 3:
            	r = new Rect(0, 0, mCanvasHeight*playerIdle3.getWidth()/playerIdle3.getHeight(), mCanvasHeight);
                canvas.drawBitmap(playerIdle3, null, r, null);
                r = new Rect(mCanvasWidth-(mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight()), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdle3Right, null, r, null);
                break;
            case 4:
            	r = new Rect(0, 0, mCanvasHeight*playerIdle2.getWidth()/playerIdle2.getHeight(), mCanvasHeight);
                canvas.drawBitmap(playerIdle2, null, r, null);
                r = new Rect(mCanvasWidth-(mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight()), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdle2Right, null, r, null);
                break;
            }
            
            if(frame > 3) {
            	frame = 0;
            }
            frame++;

        }
		
        public void setSurfaceSize(int width, int height) {
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
            }
        }
		
		@Override
        public void run() {
            while (state == RUNNING) {
            	long beforeTime = System.nanoTime();
            	
                Canvas c = null;
                try {
                	synchronized (mSurfaceHolder) {
                		c = mSurfaceHolder.lockCanvas(null);
                		if(c!=null) {
                    		doDraw(c);
                		}
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                this.sleepTime = delay - ((System.nanoTime()-beforeTime)/1000000L);
                try {
                	if(sleepTime > 0)
                		this.sleep(sleepTime);
                } catch (InterruptedException e) {
                	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        }
		
		public Bundle saveState(Bundle outState) {
			// Save all important variables into the bundle
			return outState;
		}

		public void pause() {
			synchronized (mSurfaceHolder) {
				state = PAUSED;
            }
		}

		public void unpause() {
			synchronized (mSurfaceHolder) {
				state = RUNNING;
			}
		}
		
	}
	
	private RenderingThread thread;
	private Context mContext;
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		// register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        thread = new RenderingThread(holder, mContext, new Handler());
        setFocusable(true); // make sure we get key events
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(thread.state == RenderingThread.PAUSED) {
			thread = new RenderingThread(holder, mContext, new Handler());
		}
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.pause();
        while (retry) {
            try {
                thread.join();
                //Log.w(this.getClass().getName(), "Surface Destroyed.  Thread Joined successfully.");
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

	public RenderingThread getThread() {
		return thread;
	}
	
}