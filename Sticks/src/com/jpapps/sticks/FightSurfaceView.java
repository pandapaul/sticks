package com.jpapps.sticks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class FightSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	private StickFightRenderer fightRenderer;
	private Context mContext;
	private Thread renderThread;
	
	public FightSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		// register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        fightRenderer = new StickFightRenderer(holder, mContext, 70);
        //setFocusable(true); // make sure we get key events
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		fightRenderer.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(fightRenderer.getRenderState() == StickFightRenderer.PAUSED) {
			//Restart the StickFightRenderer since it's paused 
			fightRenderer = new StickFightRenderer(holder, mContext, 180);
		}
		renderThread = new Thread(fightRenderer, "Sticks.FightRendererThread");
		renderThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		fightRenderer.pause();
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