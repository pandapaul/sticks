package com.jpapps.sticks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class MainMenuActivity extends Activity {
	
	//Public indexes for sprite sheets
	public final static int PLAYER_SHEET_INDEX = 0;
	public static SpriteSheet playerSheet;
	
	//int[] for each sprite sheet with 3 parameters: ID, rows, & columns.
	//private final static int[] playerSheetParams = {R.drawable.spritesheet_stick,2,6};
	
	private class LoadSpriteSheets extends AsyncTask<SpriteSheet, Integer, Boolean> {	
		
		protected Resources res = getResources();
		
		@Override
		protected Boolean doInBackground(SpriteSheet... spriteSheets) {
			boolean complete = false;
			playerSheet = new SpriteSheet(res, R.drawable.spritesheet_stick, 3, 6);
			complete = true;
			try {
				//Using this for now just to make sure that the load screen doesn't flash by too quickly
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
		
		if(playerSheet == null) {
			setContentView(R.layout.activity_load_screen);
			new LoadSpriteSheets().execute(playerSheet);
		} else {
			setContentView(R.layout.activity_main_menu);
		}
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
