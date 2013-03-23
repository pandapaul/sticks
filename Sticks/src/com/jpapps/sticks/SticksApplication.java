package com.jpapps.sticks;

import java.util.ArrayList;

import android.app.Application;

public class SticksApplication extends Application {
	private ArrayList<SpriteSheet> spriteSheets;
	
	public ArrayList<SpriteSheet> getSpriteSheets() {
		return spriteSheets;
	}
	
	public void setSpriteSheets(ArrayList<SpriteSheet> sheets) {
		spriteSheets = sheets;
	}
}
