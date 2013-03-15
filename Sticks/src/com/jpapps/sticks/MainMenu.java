package com.jpapps.sticks;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
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
		case R.id.button_mainmenu_multiplayer:
			/*
			Intent i = new Intent(this, com.jpapps.sticks.MultiplayerGame.class);
			startActivity(i);
			*/
		}
	}

}
