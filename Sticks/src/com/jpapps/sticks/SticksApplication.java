package com.jpapps.sticks;

import java.util.ArrayList;

import android.app.Application;

public class SticksApplication extends Application {
	private ArrayList<SpriteSheet> spriteSheets;
	private ArrayList<NumberMill> spriteAnimations;
	
	public ArrayList<SpriteSheet> getSpriteSheets() {
		return spriteSheets;
	}
	
	public void setSpriteSheets(ArrayList<SpriteSheet> sheets) {
		this.spriteSheets = sheets;
	}

	public ArrayList<NumberMill> getSpriteAnimations() {
		return spriteAnimations;
	}

	public void setSpriteAnimations(ArrayList<NumberMill> spriteAnimations) {
		this.spriteAnimations = spriteAnimations;
	}
}
