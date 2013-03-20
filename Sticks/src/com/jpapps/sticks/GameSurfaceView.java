package com.jpapps.sticks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	private SpriteRenderer mFrameRenderer;
	private Context mContext;
	private Thread renderThread;
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		// register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        mFrameRenderer = new SpriteRenderer(holder, mContext, 180);
        setFocusable(true); // make sure we get key events
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mFrameRenderer.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(mFrameRenderer.state == SpriteRenderer.PAUSED) {
			mFrameRenderer = new SpriteRenderer(holder, mContext, 180);
		}
		renderThread = new Thread(mFrameRenderer);
		renderThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		mFrameRenderer.pause();
        while (retry) {
            try {
                renderThread.join();
                //Log.w(this.getClass().getName(), "Surface Destroyed.  Thread Joined successfully.");
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
}