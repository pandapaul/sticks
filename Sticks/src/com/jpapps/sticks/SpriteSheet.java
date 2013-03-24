package com.jpapps.sticks;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

/**
 * Class for establishing the various properties of a sprite sheet and performing operations on it.
 */
public class SpriteSheet {
	protected String name;
	protected Bitmap bitmap, mirroredBitmap;
	protected int rows, cols, spriteWidth, spriteHeight;
	//protected ArrayList<SpriteAnimation> spriteAnimations;
	protected ArrayList<Rect> boxes = new ArrayList<Rect>();
	
	/**
	 * Creates a new SpriteSheet from bitmap b and sets things up based on individual sprite dimensions. 
	 * <br>Note that all sprites are assumed to be the <u>same size</u>.
	 * @param b is the bitmap for the whole sheet
	 * @param w is individual sprite width
	 * @param h is individual sprite height
	 */
	public SpriteSheet(Bitmap b, int r, int c) {
		this.bitmap = b;
		Matrix m = new Matrix();
        m.preScale(-1, 1);
        mirroredBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
		this.rows = r;
		this.cols = c;
		this.spriteWidth = b.getWidth()/c;
		this.spriteHeight = b.getHeight()/r;
		//Log.w("Sticks", "Bitmap width: " + bitmap.getWidth() + ".  Bitmap height: " + bitmap.getHeight());
		//Log.w("Sticks", "spritesheet has "+ rows + " rows and " + cols + " cols.");
		//Calculate the boxes
		int x1=0;
		int y1=0;
		int x2=spriteWidth;
		int y2=spriteHeight;
		for(int i=0;i<rows;i++) {
			y1=i*spriteHeight;
			y2=y1+spriteHeight;
			for(int j=0;j<cols;j++) {
				x1=j*spriteWidth;
				x2=x1+spriteWidth;
				boxes.add(new Rect(x1,y1,x2,y2));
				//Log.w("Sticks", "Made a new box with " + x1 + ", " + y1 + ", " + x2 + ", " + y2);
			}
		}
	}
	
	/*
	public SpriteAnimation defineAnimation(int[] ids) {
		SpriteAnimation newAnim = new SpriteAnimation(ids);
		spriteAnimations.add(newAnim);
		return newAnim;
	}*/
	
	/*
	public ArrayList<SpriteAnimation> getAnimations() {
		return spriteAnimations;
	}*/
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public Bitmap getMirroredBitmap() {
		return mirroredBitmap;
	}
	
	public Rect getBox(int id) {
		return boxes.get(id-1);
	}
	
	public Rect getMirroredBox(int id) {
		int y = id/(cols+1);
		int x = id%(cols+1);
		int newx = cols - x;
		int newid = newx + y*(cols-1);
		return boxes.get(newid);
	}
}