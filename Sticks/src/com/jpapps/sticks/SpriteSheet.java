package com.jpapps.sticks;

import android.graphics.Bitmap;

public class SpriteSheet {
	protected Bitmap bitmap;
	protected int rows, columns, spriteWidth, spriteHeight;
	
	public SpriteSheet(Bitmap b, int w, int h) {
		this.bitmap = b;
		this.spriteWidth = w;
		this.spriteHeight = h;
		this.rows = bitmap.getHeight()/spriteHeight;
		this.columns = bitmap.getWidth()/spriteWidth;
	}
}
