package com.jpapps.sticks;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class StickFightRenderer extends SurfaceRenderer {
	
	//Player state variables
	private int playerState;
	private final static int IDLE = 0;
	private final static int DEFENDING_HIGH = 1;
	private final static int DEFENDING_LOW = 2;
	private final static int ATTACKING_HIGH = 3;
	private final static int ATTACKING_LOW = 4;
	
	//Sprite sheets
	private ArrayList<SpriteSheet> spriteSheets;
	private SpriteSheet playerSheet;
	private static int[] IDLE_ANIMATION_FRAMES = {1,2,3};
	private SpriteAnimation idleAnimation;

	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		playerState = IDLE;
		
		//Get all the sprites ready for use
		SticksApplication sa = (SticksApplication) context.getApplicationContext();
		spriteSheets = sa.getSpriteSheets();
		playerSheet = spriteSheets.get(0);
		idleAnimation = playerSheet.defineAnimation(IDLE_ANIMATION_FRAMES);
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//Wipe the canvas to white
		canvas.drawColor(Color.WHITE);
		
		switch(playerState) {
		case IDLE:
			//canvas.drawBitmap(bitmap, source, dest, null);
			break;
		}
	}
}