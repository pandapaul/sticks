package com.jpapps.sticks;

import com.jpapps.sticks.GameSurfaceView.GameThread;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SinglePlayerGame extends Activity {

	private GameSurfaceView mGameSurfaceView;
	private GameThread mGameThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_single_player_game);
	    mGameSurfaceView = (GameSurfaceView) findViewById(R.id.single_player_game_surface);
        mGameThread = mGameSurfaceView.getThread();
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        mGameSurfaceView.getThread().pause(); // pause game when Activity pauses
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        mGameThread.saveState(outState);
        Log.w(this.getClass().getName(), "SIS called");
    }

}