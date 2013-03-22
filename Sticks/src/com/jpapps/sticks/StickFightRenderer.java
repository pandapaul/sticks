package com.jpapps.sticks;

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
	private SpriteSheet playerSheet;

	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		playerState = IDLE;
		Resources res = context.getResources();
		Bitmap playerBitmap = BitmapFactory.decodeResource(res, R.drawable.anim_stick_idle);
		playerSheet = new SpriteSheet(playerBitmap, 300, 400);
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//Wipe the canvas to white
		canvas.drawColor(Color.WHITE);
		switch(playerState) {
		case IDLE:
			
			break;
		}
	}
}