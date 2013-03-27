package com.jpapps.sticks;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
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
	public final static int ENGAGE = 16;
	
	//Player variables
	private int[] playerPosition = {0,0};
	
	//Opponent variables
	private int[] opponentPosition = {0,0};
	
	//Sprite sheets
	private ArrayList<SpriteSheet> spriteSheets;
	private SpriteSheet playerSheet;
	
	//Sprite animations
	public final static int[] playerIdleFrames = {1,2,3,4};
	public final static int[] playerRunFrames = {5,6,7,8,9,10,11,12,13};
	public final static int[] playerDefendHighFrames = {5,14,15,16,17};
	private int playerAnimationFrame = 1;
	private NumberMill playerIdleAnimation;
	private NumberMill playerRunAnimation;
	private NumberMill playerDefendHighAnimation;
	
	private int middle, scaledRunDistance;
	
	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		battleAnimation = IDLE;
		
		//Get access to globals
		SticksApplication sa = (SticksApplication) context.getApplicationContext();
		
		//Get sprite sheets ready for use`
		spriteSheets = sa.getSpriteSheets();
		playerSheet = MainMenuActivity.playerSheet;
		
		//Get sprite animations ready for use
		playerIdleAnimation = new NumberMill(playerIdleFrames, NumberMill.CYCLE);
		playerRunAnimation = new NumberMill(playerRunFrames, NumberMill.ONCE);
		playerDefendHighAnimation = new NumberMill(playerDefendHighFrames, NumberMill.ONCE);

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
		if(battleAnimation == ENGAGE) {
			
			middle = (int) Math.round((float)this.getCanvasWidth()/2.0);
			scaledRunDistance = Math.round((float)(middle - playerPosition[0]) / (float)(playerRunFrames.length+1));
			
			if(!(playerRunAnimation.getCurrent()==playerRunFrames[playerRunFrames.length-1])) {
				//Players should be running to the middle
				playerPosition[0] += scaledRunDistance;
				opponentPosition[0] -= scaledRunDistance;
			}
			else {
				battleAnimation = DEFEND_HIGH_VS_DEFEND_HIGH;
			}
		}
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//Wipe the canvas to white
		canvas.drawColor(Color.WHITE);
		
		Rect src;
		Rect dst;
		Bitmap bitmap;
			
		switch(battleAnimation) {
		case IDLE:
			playerAnimationFrame = playerIdleAnimation.getCurrent();
			playerIdleAnimation.advance();
			break;
		case ENGAGE:
			playerAnimationFrame = playerRunAnimation.getCurrent();
			playerRunAnimation.advance();
			break;
		case DEFEND_HIGH_VS_DEFEND_HIGH:
			playerAnimationFrame = playerDefendHighAnimation.getCurrent();
			playerDefendHighAnimation.advance();
			break;
		default:
			Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
			break;
		}
		
		src = playerSheet.getBox(playerAnimationFrame);
		dst = calculateDestinationRect(src, 0, this.getCanvasHeight(), playerPosition);
		bitmap = playerSheet.getBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		src = playerSheet.getMirroredBox(playerAnimationFrame);
		
		//Just having the opponent's position mirror the player's position for now
		opponentPosition[0] = canvas.getWidth() - playerPosition[0] - dst.width();
		opponentPosition[1] = playerPosition[1];
		dst = calculateDestinationRect(src, 0, this.getCanvasHeight(),opponentPosition);
		bitmap = playerSheet.getMirroredBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
	}
	
	public void setBattleAnimation(int battleAnimation) {
		this.battleAnimation = battleAnimation;
	}
}