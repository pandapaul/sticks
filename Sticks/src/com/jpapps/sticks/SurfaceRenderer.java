package com.jpapps.sticks;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Surface animation rendering class that implements Runnable. Should be extended to override doDraw().
 */
public class SurfaceRenderer implements Runnable {

	//SurfaceView related variables
	private SurfaceHolder mSurfaceHolder;
	private int canvasWidth;
	private int canvasHeight;
	
	//State variables
	public final static int RUNNING = 1;
	public final static int PAUSED = 2;
	private int renderState;
	
	//Timing variables
	private long desiredSleepTime;
	private long adjustedSleepTime;
	
	//Resource variables
	private Context context;
	
	/**
	 * Constructs a new SurfaceRenderer that will animate a canvas within the specified SurfaceHolder using resources from the provided context.
	 * 
	 * @param surfaceHolder holds the surface that will be animated.
	 * @param context should provide the FrameRenderer with application context.
	 * @param time is the desired number of milliseconds between drawings.
	 */
	public SurfaceRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		mSurfaceHolder = surfaceHolder;
		this.context = context;
		desiredSleepTime = time;
		renderState = RUNNING;
	}
	
	/**
	 * Should be overridden to perform all drawing.
	 * @param canvas is where all drawing will be done.
	 */
	protected void doDraw(Canvas canvas) {
		
    }
	
    public void setSurfaceSize(int width, int height) {
        synchronized (mSurfaceHolder) {
            setCanvasWidth(width);
            setCanvasHeight(height);
        }
    }
    
    /**
     * Pauses the SurfaceRenderer.
     */
	public void pause() {
		synchronized (mSurfaceHolder) {
			renderState = PAUSED;
        }
	}
	
    /**
     * While the SurfaceRenderer is running, the following cycle will be repeated every desiredSleepTime milliseconds:<br>
     *   1.Lock the canvas<br>
     *   2.Draw to the canvas with doDraw()<br>
     *   3.Unlock and post the updated canvas.
     */
	@Override
    public void run() {
        while (renderState == RUNNING) {
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
            adjustedSleepTime = desiredSleepTime - ((System.nanoTime()-beforeTime)/1000000L);
            try {
            	if(adjustedSleepTime > 0)
            		Thread.sleep(adjustedSleepTime);
            } catch (InterruptedException e) {
            	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }

	public int getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(int canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(int canvasHeight) {
		this.canvasHeight = canvasHeight;
	}
	
	public int getRenderState() {
		return renderState;
	}

	public void setRenderState(int renderState) {
		this.renderState = renderState;
	}
}
