package com.jpapps.sticks;

import com.jpapps.sticks.GameSurfaceView.RenderingThread;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SinglePlayerGame extends Activity {

	private GameSurfaceView mGameSurfaceView;
	private RenderingThread mRenderingThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_single_player_game);
	    mGameSurfaceView = (GameSurfaceView) findViewById(R.id.single_player_game_surface);
        mRenderingThread = mGameSurfaceView.getThread();
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        mGameSurfaceView.getThread().pause(); // pause animation when Activity pauses
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        mRenderingThread.saveState(outState);
        Log.w(this.getClass().getName(), "SIS called");
    }

}