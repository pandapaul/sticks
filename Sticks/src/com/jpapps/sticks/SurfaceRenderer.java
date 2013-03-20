package com.jpapps.sticks;

import java.util.ArrayList;
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
import android.view.SurfaceHolder;

/**
 * Surface animation rendering class that implements Runnable.
 */
public class SurfaceRenderer implements Runnable {

	//SurfaceView related variables
	protected SurfaceHolder mSurfaceHolder;
	protected Context mContext;
	protected int mCanvasWidth;
	protected int mCanvasHeight;
	
	//State variables
	public final static int RUNNING = 1;
	public final static int PAUSED = 2;
	protected int state;
	
	//Timing variables
	protected long desiredSleepTime;
	protected long actualSleepTime;
	
	/**
	 * A list of all sprite animations that can be drawn by this SurfaceRenderer.
	 */
	protected ArrayList<SpriteAnimation> spriteAnimations;
	
	/**
	 * Constructs a new SurfaceRenderer that will animate a canvas within the specified SurfaceHolder using resources from the provided context.
	 * 
	 * @param surfaceHolder holds the surface that will be animated.
	 * @param context should provide the FrameRenderer with application context.
	 * @param delay is the intended number of milliseconds between frame drawings.
	 */
	public SurfaceRenderer(SurfaceHolder surfaceHolder, Context context, int delay) {
		mSurfaceHolder = surfaceHolder;
		mContext = context;
		state = RUNNING;
		Resources res = mContext.getResources();
		
        playerIdle = BitmapFactory.decodeResource(res, R.drawable.anim_stick_idle);
        
        /* PRESERVING FOR HORIZONTAL MIRRORING EXAMPLE
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        playerIdleRight = Bitmap.createBitmap(playerIdle, 0, 0, playerIdle.getWidth(), playerIdle.getHeight(), m, false);
        */
	}
	
	/**
	 * Wipes the canvas then draws the appropriate objects.
	 * @param canvas is where all drawing will be done.
	 */
	protected void doDraw(Canvas canvas) {
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
        	src = new Rect(playerIdleFrameWidth*2,0,playerIdleFrameWidth*3,playerIdleHeight);
        	dst = new Rect(0, 0, mCanvasHeight*playerIdleFrameWidth/playerIdleHeight, mCanvasHeight);
            canvas.drawBitmap(playerIdle, src, dst, null);
            dst = new Rect(mCanvasWidth-(mCanvasHeight*playerIdleFrameWidth/playerIdleHeight), 0, mCanvasWidth, mCanvasHeight);
            canvas.drawBitmap(playerIdleRight, src, dst, null);
            break;
        case 4:
        	src = new Rect(playerIdleFrameWidth,0,playerIdleFrameWidth*2,playerIdleHeight);
        	dst = new Rect(0, 0, mCanvasHeight*playerIdleFrameWidth/playerIdleHeight, mCanvasHeight);
            canvas.drawBitmap(playerIdle, src, dst, null);
            dst = new Rect(mCanvasWidth-(mCanvasHeight*playerIdleFrameWidth/playerIdleHeight), 0, mCanvasWidth, mCanvasHeight);
            canvas.drawBitmap(playerIdleRight, src, dst, null);
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
                	synchronized(mSurfaceHolder) {
                		mSurfaceHolder.unlockCanvasAndPost(c);
                	}
                }
            }
            actualSleepTime = desiredSleepTime - ((System.nanoTime()-beforeTime)/1000000L);
            try {
            	if(actualSleepTime > 0)
            		Thread.sleep(actualSleepTime);
            } catch (InterruptedException e) {
            	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }

	public void pause() {
		synchronized (mSurfaceHolder) {
			state = PAUSED;
        }
	}

}
