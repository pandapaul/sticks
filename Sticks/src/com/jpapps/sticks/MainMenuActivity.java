package com.jpapps.sticks;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class MainMenuActivity extends Activity implements AudioManager.OnAudioFocusChangeListener {
	
	//Public indexes for sprite sheets
	public final static int PLAYER_SHEET_INDEX = 0;
	public static SpriteSheet playerSheet;
	
	//int[] for each sprite sheet with 3 parameters: ID, rows, & columns.
	//private final static int[] playerSheetParams = {R.drawable.spritesheet_stick,2,6};
	
	//Music stuff
	protected MediaPlayer mp1;
	protected MediaPlayer mp2;
	protected AudioManager audioManager;
	
	private class LoadSpriteSheets extends AsyncTask<SpriteSheet, Integer, Boolean> {	
		
		protected Resources res = getResources();
		
		@Override
		protected Boolean doInBackground(SpriteSheet... spriteSheets) {
			boolean complete = false;
			
			if(playerSheet == null)
				playerSheet = new SpriteSheet(res, R.drawable.spritesheet_stick, 3, 6);
			
			mp1 = MediaPlayer.create(getApplicationContext(), R.raw.sticks_main_introriff);
			mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp2.start();
				}});
			mp2 = MediaPlayer.create(getApplicationContext(), R.raw.sticks_main_loop);
			mp2.setLooping(true);
			
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
				showMenu();				
			}
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setWindowAnimations(android.R.anim.slide_in_left);
		
		if(playerSheet == null || mp1 == null || mp2 == null) {
			setContentView(R.layout.activity_load_screen);
			new LoadSpriteSheets().execute(playerSheet);
		} else {
			showMenu();
		}
	}
	
	protected void showMenu() {
		setContentView(R.layout.activity_main_menu);
		mp1.start();
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

	@Override
	public void onAudioFocusChange(int focusChange) {
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mp1 != null) mp1.release();
		if(mp2 != null) mp2.release();
	}

}
