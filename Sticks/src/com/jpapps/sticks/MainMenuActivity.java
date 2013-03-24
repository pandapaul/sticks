package com.jpapps.sticks;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;

public class MainMenuActivity extends Activity {
	
	//Public indexes for sprite sheets
	public final static int PLAYER_SHEET_INDEX = 0;
	
	//Public indexes for sprite animations
	public final static int PLAYER_IDLE_ANIMATION_INDEX = 0;
	
	//int[] for each sprite sheet with 3 parameters: ID, rows, & cols.
	private final static int[] playerSheetParams = {R.drawable.anim_stick_idle,1,3};
	
	//int[] for each sprite animation with parameters: {firstFrame, ... , lastFrame, type}
	private final static int[] playerIdleAnimationParams = {1,1,1,1,2,2,2,2,3,3,3,3, NumberMill.CYCLE};
	
	private class LoadSpriteAnimations extends AsyncTask<int[], Integer, Boolean> {	
		
		//Get a reference to SticksApplication so that we can keep track of all the stuff we're gonna load
		protected SticksApplication sa = (SticksApplication)getApplicationContext();
		
		@Override
		protected Boolean doInBackground(int[]... spriteAnimationParams) {
			boolean complete = false;
			int listCount = spriteAnimationParams.length;
			ArrayList<NumberMill> spriteAnimations = new ArrayList<NumberMill>();
			NumberMill anim;
			for (int i=0;i<listCount;i++) {
				int[] animFrames = new int[spriteAnimationParams[i].length-1];
				for(int j=0;j<animFrames.length;j++) {
					animFrames[j] = spriteAnimationParams[i][j];
				}
				anim = new NumberMill(animFrames, spriteAnimationParams[i][spriteAnimationParams.length-1]);
				spriteAnimations.add(PLAYER_IDLE_ANIMATION_INDEX,anim);
				if(isCancelled()) return complete;
			}
			sa.setSpriteAnimations(spriteAnimations);
			complete = true;
			return complete;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result = true) {
				new LoadSpriteSheets().execute(playerSheetParams);
			}
	    }
	}
	
	private class LoadSpriteSheets extends AsyncTask<int[], Integer, Boolean> {	
		
		//Get a reference to SticksApplication so that we can keep track of all the stuff we're gonna load
		SticksApplication sa = (SticksApplication)getApplicationContext();

		protected Resources res = getResources();
		protected Bitmap b;
		protected SpriteSheet s;
		
		@Override
		protected Boolean doInBackground(int[]... spriteSheetParams) {
			boolean complete = false;
			int listCount = spriteSheetParams.length;
			ArrayList<SpriteSheet> spriteSheets = new ArrayList<SpriteSheet>();
			for (int i=0;i<listCount;i++) {
				b = BitmapFactory.decodeResource(res, spriteSheetParams[i][0]);
				s = new SpriteSheet(b, spriteSheetParams[i][1], spriteSheetParams[i][2]);
				spriteSheets.add(PLAYER_SHEET_INDEX, s);
				if(isCancelled()) return complete;
			}
			sa.setSpriteSheets(spriteSheets);
			complete = true;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return complete;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result = true) {
				setContentView(R.layout.activity_main_menu);
			}
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setWindowAnimations(android.R.anim.slide_in_left);
		setContentView(R.layout.activity_load_screen);
		
		//Load 'em up using an AsyncTask
		new LoadSpriteAnimations().execute(playerIdleAnimationParams);
		//new LoadSpriteSheets().execute(playerSheetParams);
	}
	
	public void tap(View view) {
		switch(view.getId()) {
		case R.id.button_mainmenu_singleplayer:
			Intent i = new Intent(this, com.jpapps.sticks.SinglePlayerGameActivity.class);
			startActivity(i);
			break;
		case R.id.button_mainmenu_multiplayer:
			/*
			Intent i = new Intent(this, com.jpapps.sticks.MultiplayerGame.class);
			startActivity(i);
			*/
			break;
		}
	}

}
