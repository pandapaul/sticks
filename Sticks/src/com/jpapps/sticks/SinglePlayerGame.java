package com.jpapps.sticks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SinglePlayerGame extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_single_player_game);
	}
	
	protected class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
		
		
		
		public GameSurfaceView(Context context) {
			super(context);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			
		}
		
	}

}
