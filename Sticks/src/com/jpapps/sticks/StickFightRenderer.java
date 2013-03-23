package com.jpapps.sticks;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class StickFightRenderer extends SurfaceRenderer {
	
	//Player state variables
	private int playerState;
	private final static int IDLE = 0;
	private final static int DEFENDING_HIGH = 1;
	private final static int DEFENDING_LOW = 2;
	private final static int ATTACKING_HIGH = 3;
	private final static int ATTACKING_LOW = 4;
	private int[] playerPosition = {0,0};
	private Rect playerRect;
	
	//Sprite sheets
	private ArrayList<SpriteSheet> spriteSheets;
	private SpriteSheet playerSheet;
	
	//Sprite animations
	private ArrayList<SpriteAnimation> spriteAnimations;
	private SpriteAnimation playerIdleAnimation;

	private SpriteAnimation playerMovement;
	
	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		playerState = IDLE;
		
		//Get access to globals
		SticksApplication sa = (SticksApplication) context.getApplicationContext();
		
		//Get sprite sheets ready for use
		spriteSheets = sa.getSpriteSheets();
		playerSheet = spriteSheets.get(MainMenu.PLAYER_SHEET_INDEX);
		
		//Get sprite animations ready for use
		spriteAnimations = sa.getSpriteAnimations();
		playerIdleAnimation = spriteAnimations.get(MainMenu.PLAYER_IDLE_ANIMATION_INDEX);
		
		int[] move = {0,30,60,90,120,150,180,210};
		
		playerMovement = new SpriteAnimation(move, SpriteAnimation.CYCLE);
	}
	
	private Rect calculateDestinationRect(Rect r, float w, float h, int[] topleft) {
		if(w<=0) {
			Log.w("Sticks", "r.height() = " + r.height() + " h = " + h);
			w = (float)r.width()/(float)r.height()*h;
			Log.w("Sticks", "w = " + w);
		}
		if(h<=0) {
			h = (float)r.height()/(float)r.width()*w;
		}
		Rect s = new Rect(r);
		s.set(topleft[0], topleft[1], topleft[0]+(int)w, topleft[1]+(int)h);
		return s;
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//Wipe the canvas to white
		canvas.drawColor(Color.WHITE);
		
		switch(playerState) {
		case IDLE:
			playerPosition[0] = playerMovement.advance();
			Rect src = playerSheet.getBox(playerIdleAnimation.advance());
			Rect dst = calculateDestinationRect(src, 0, this.getCanvasHeight(), playerPosition);
			canvas.drawBitmap(playerSheet.getBitmap(), src, dst, null);
			break;
		}
	}
}