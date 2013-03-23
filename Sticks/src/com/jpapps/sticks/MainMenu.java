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

public class MainMenu extends Activity {
	
	//Establish int[] for each sprite sheet with 3 parameters: ID, width, & height.
	private static int[] playerSheetParams = {R.drawable.anim_stick_idle,300,400};
	
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
				spriteSheets.add(s);
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
		new LoadSpriteSheets().execute(playerSheetParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public void tap(View view) {
		switch(view.getId()) {
		case R.id.button_mainmenu_singleplayer:
			Intent i = new Intent(this, com.jpapps.sticks.SinglePlayerGame.class);
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
