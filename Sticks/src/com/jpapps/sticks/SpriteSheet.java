package com.jpapps.sticks;

import java.util.ArrayList;

import android.graphics.Bitmap;

/**
 * Class for establishing the various properties of a sprite sheet and performing operations on it.
 * 
 */
public class SpriteSheet {
	protected Bitmap bitmap;
	protected int rows, columns, spriteWidth, spriteHeight;
	protected ArrayList<SpriteAnimation> spriteAnimations;
	
	public SpriteSheet(Bitmap b, int w, int h) {
		this.bitmap = b;
		this.spriteWidth = w;
		this.spriteHeight = h;
		this.rows = bitmap.getHeight()/spriteHeight;
		this.columns = bitmap.getWidth()/spriteWidth;
	}
	
}