package com.jpapps.sticks;

import java.util.ArrayList;

import android.app.Application;

public class SticksApplication extends Application {
	private ArrayList<SpriteSheet> spriteSheets;
	private ArrayList<SpriteAnimation> spriteAnimations;
	
	public ArrayList<SpriteSheet> getSpriteSheets() {
		return spriteSheets;
	}
	
	public void setSpriteSheets(ArrayList<SpriteSheet> sheets) {
		this.spriteSheets = sheets;
	}

	public ArrayList<SpriteAnimation> getSpriteAnimations() {
		return spriteAnimations;
	}

	public void setSpriteAnimations(ArrayList<SpriteAnimation> spriteAnimations) {
		this.spriteAnimations = spriteAnimations;
	}
}
