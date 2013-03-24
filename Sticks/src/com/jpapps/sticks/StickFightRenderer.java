package com.jpapps.sticks;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class StickFightRenderer extends SurfaceRenderer {
	
	//Battle animation variables
	private int battleAnimation;
	public final static int IDLE = 0;
	public final static int DEFEND_HIGH_VS_DEFEND_HIGH = 1;
	public final static int DEFEND_HIGH_VS_DEFEND_LOW = 2;
	public final static int DEFEND_HIGH_VS_ATTACK_HIGH = 3;
	public final static int DEFEND_HIGH_VS_ATTACK_LOW = 4;
	public final static int DEFEND_LOW_VS_DEFEND_LOW = 5;
	public final static int DEFEND_LOW_VS_ATTACK_HIGH = 6;
	public final static int DEFEND_LOW_VS_ATTACK_LOW = 7;
	public final static int ATTACK_HIGH_VS_ATTACK_HIGH = 8;
	public final static int ATTACK_HIGH_VS_ATTACK_LOW = 9;
	public final static int ATTACK_LOW_VS_ATTACK_LOW = 10;
	public final static int DEFEND_LOW_VS_DEFEND_HIGH = 11;
	public final static int ATTACK_LOW_VS_DEFEND_HIGH = 12;
	public final static int ATTACK_HIGH_VS_DEFEND_LOW = 13;
	public final static int ATTACK_LOW_VS_DEFEND_LOW = 14;
	public final static int ATTACK_LOW_VS_ATTACK_HIGH = 15;
	
	//Player variables
	private int[] playerPosition = {0,0};
	
	//Opponent variables
	private int[] opponentPosition = {0,0};
	
	//Sprite sheets
	private ArrayList<SpriteSheet> spriteSheets;
	private SpriteSheet playerSheet;
	
	//Sprite animations
	private ArrayList<NumberMill> spriteAnimations;
	private NumberMill playerIdleAnimation;
	
	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		battleAnimation = IDLE;
		
		//Get access to globals
		SticksApplication sa = (SticksApplication) context.getApplicationContext();
		
		//Get sprite sheets ready for use`
		spriteSheets = sa.getSpriteSheets();
		playerSheet = spriteSheets.get(MainMenuActivity.PLAYER_SHEET_INDEX);
		
		//Get sprite animations ready for use
		spriteAnimations = sa.getSpriteAnimations();
		playerIdleAnimation = spriteAnimations.get(MainMenuActivity.PLAYER_IDLE_ANIMATION_INDEX);
	}
	
	private Rect calculateDestinationRect(Rect r, float w, float h, int[] topleft) {
		if(w<=0) {
			w = (float)r.width()/(float)r.height()*h;
		}
		if(h<=0) {
			h = (float)r.height()/(float)r.width()*w;
		}
		Rect s = new Rect(r);
		s.set(topleft[0], topleft[1], topleft[0]+(int)w, topleft[1]+(int)h);
		return s;
	}
	
	@Override
	protected void update() {
		
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//Wipe the canvas to white
		canvas.drawColor(Color.WHITE);
		
		Rect src;
		Rect dst;
		
		switch(battleAnimation) {
		case IDLE:
			src = playerSheet.getBox(playerIdleAnimation.advance());
			dst = calculateDestinationRect(src, 0, this.getCanvasHeight(), playerPosition);
			canvas.drawBitmap(playerSheet.getBitmap(), src, dst, null);
			break;
		case DEFEND_HIGH_VS_DEFEND_HIGH:
			
			break;
		}
	}
	
	public void setBattleAnimation(int battleAnimation) {
		this.battleAnimation = battleAnimation;
	}
}