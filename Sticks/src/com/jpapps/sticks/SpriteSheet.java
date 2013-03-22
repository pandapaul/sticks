package com.jpapps.sticks;

import java.util.ArrayList;

import android.graphics.Bitmap;

/**
 * Class for establishing the various properties of a sprite sheet and performing operations on it.
 */
public class SpriteSheet {
	protected Bitmap bitmap;
	protected int rows, columns, spriteWidth, spriteHeight;
	protected ArrayList<SpriteAnimation> spriteAnimations;
	
	/**
	 * Creates a new SpriteSheet from bitmap b and sets things up based on individual sprite dimensions. 
	 * <br>Note that all sprites are assumed to be the <u>same size</u>.
	 * @param b is the bitmap for the whole sheet
	 * @param w is individual sprite width
	 * @param h is individual sprite height
	 */
	public SpriteSheet(Bitmap b, int w, int h) {
		this.bitmap = b;
		this.spriteWidth = w;
		this.spriteHeight = h;
		this.rows = bitmap.getHeight()/spriteHeight;
		this.columns = bitmap.getWidth()/spriteWidth;
	}
}