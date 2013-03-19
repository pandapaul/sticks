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
		
		//State variables
		public final static int RUNNING = 1;
		public final static int PAUSED = 2;
		int state = RUNNING;
		
		//Animation-related variables
		public final static int PLAYER_IDLE_ANIM_FRAMES = 3;
		
		private long sleepTime;
		private long delay = 180; //in milliseconds
		private int frame = 1;
		private Bitmap playerIdle;
		private Bitmap playerIdleRight;
		
		
		public RenderingThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
			mSurfaceHolder = surfaceHolder;
			mContext = context;
			mHandler = handler;
			Resources res = mContext.getResources();
	        playerIdle = BitmapFactory.decodeResource(res, R.drawable.anim_stick_idle);
	        
	        Matrix m = new Matrix();
	        m.preScale(-1, 1);
	        playerIdleRight = Bitmap.createBitmap(playerIdle, 0, 0, playerIdle.getWidth(), playerIdle.getHeight(), m, false);
		}
		
		private void doDraw(Canvas canvas) {
			//wipe the canvas
			canvas.drawColor(0xffffffff);
            Rect dst, src;
            int playerIdleWidth = playerIdle.getWidth();
            int playerIdleHeight = playerIdle.getHeight();
            int playerIdleFrameWidth = playerIdleWidth/PLAYER_IDLE_ANIM_FRAMES;
            
            //This is temporary/test animation code. Should switch over to using sprites in conjunction with a game engine. But first, sleep.
            
            switch (frame) {
            case 1:
            	src = new Rect(0,0,playerIdleFrameWidth,playerIdleHeight);
            	dst = new Rect(0, 0, mCanvasHeight*playerIdleFrameWidth/playerIdleHeight, mCanvasHeight);
                canvas.drawBitmap(playerIdle, src, dst, null);
                dst = new Rect(mCanvasWidth-(mCanvasHeight*playerIdleFrameWidth/playerIdleHeight), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdleRight, src, dst, null);
                break;
            case 2:
            	src = new Rect(playerIdleFrameWidth,0,playerIdleFrameWidth*2,playerIdleHeight);
            	dst = new Rect(0, 0, mCanvasHeight*playerIdleFrameWidth/playerIdleHeight, mCanvasHeight);
                canvas.drawBitmap(playerIdle, src, dst, null);
                dst = new Rect(mCanvasWidth-(mCanvasHeight*playerIdleFrameWidth/playerIdleHeight), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdleRight, src, dst, null);
                break;
            case 3:
            	dst = new Rect(0, 0, mCanvasHeight*playerIdle3.getWidth()/playerIdle3.getHeight(), mCanvasHeight);
                canvas.drawBitmap(playerIdle3, null, dst, null);
                dst = new Rect(mCanvasWidth-(mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight()), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdle3Right, null, dst, null);
                break;
            case 4:
            	dst = new Rect(0, 0, mCanvasHeight*playerIdle2.getWidth()/playerIdle2.getHeight(), mCanvasHeight);
                canvas.drawBitmap(playerIdle2, null, dst, null);
                dst = new Rect(mCanvasWidth-(mCanvasHeight*playerIdle1.getWidth()/playerIdle1.getHeight()), 0, mCanvasWidth, mCanvasHeight);
                canvas.drawBitmap(playerIdle2Right, null, dst, null);
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