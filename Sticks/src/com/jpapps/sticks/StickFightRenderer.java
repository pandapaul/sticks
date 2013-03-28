package com.jpapps.sticks;

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
	public final static int ENGAGE = 1;
	public final static int DEFEND_HIGH_VS_DEFEND_HIGH = 2;
	public final static int DEFEND_HIGH_VS_DEFEND_LOW = 3;
	public final static int DEFEND_HIGH_VS_ATTACK_HIGH = 4;
	public final static int DEFEND_HIGH_VS_ATTACK_LOW = 5;
	public final static int DEFEND_LOW_VS_DEFEND_LOW = 6;
	public final static int DEFEND_LOW_VS_ATTACK_HIGH = 7;
	public final static int DEFEND_LOW_VS_ATTACK_LOW = 8;
	public final static int ATTACK_HIGH_VS_ATTACK_HIGH = 9;
	public final static int ATTACK_HIGH_VS_ATTACK_LOW = 10;
	public final static int ATTACK_LOW_VS_ATTACK_LOW = 11;
	public final static int DEFEND_LOW_VS_DEFEND_HIGH = 12;
	public final static int ATTACK_LOW_VS_DEFEND_HIGH = 13;
	public final static int ATTACK_HIGH_VS_DEFEND_LOW = 14;
	public final static int ATTACK_LOW_VS_DEFEND_LOW = 15;
	public final static int ATTACK_LOW_VS_ATTACK_HIGH = 16;
	public final static int ATTACK_HIGH_VS_DEFEND_HIGH = 17;
	
	//Sprite sheets
	private SpriteSheet playerSheet;
	
	//Sprite animations
	public final static int[] stickIdleFrames = {1,2,3,4};
	public final static int[] stickRunFrames = {5,6,7,8,9,10,11,12};
	public final static int[] stickDefendHighFrames = {13,14,15,16};
	public final static int[] stickAttackHighFrames = {17,18,19};
	public final static int[] pathRunLeftToMiddle = {0,2,7,12,19,26,33,40};
	public final static int[] pathRunRightToMiddle = {90,88,83,76,69,62,57,50};
	
	private NumberMill stickIdleAnimation;
	private NumberMill stickRunAnimation;
	private NumberMill stickDefendHighAnimation;
	private NumberMill stickAttackHighAnimation;
	
	private AnimatedObject player;
	private AnimatedObject opponent;
	
	private int middle, scaledRunDistance;
	
	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		
		//Get sprite sheets ready
		playerSheet = MainMenuActivity.playerSheet;
		
		//Get sprite animations ready
		stickIdleAnimation = new NumberMill(stickIdleFrames, NumberMill.CYCLE);
		stickRunAnimation = new NumberMill(stickRunFrames, NumberMill.ONCE);
		stickDefendHighAnimation = new NumberMill(stickDefendHighFrames, NumberMill.ONCE);
		stickAttackHighAnimation = new NumberMill(stickAttackHighFrames, NumberMill.ONCE);
		
		//Get animated objects ready
		player = new AnimatedObject(stickIdleAnimation);
		opponent = new AnimatedObject(stickIdleAnimation);
		
		//Initialize the animation
		this.setBattleAnimation(IDLE);
	}
	
	protected Rect calculateDestinationRect(Rect r, float w, float h, int[] topleft) {
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
	
	protected int[] scalePosition(int[] position) {
		float canvasWidth = this.getCanvasWidth();
		float canvasHeight = this.getCanvasHeight();
		int scaledX = (int) Math.round((float)position[0]/100.0 * canvasWidth);
		int scaledY = (int) Math.round((float)position[1]/100.0 * canvasHeight);
		int[] scaledPosition = {scaledX,scaledY};
		return scaledPosition;
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
		Bitmap bitmap;
			
		Log.w("Sticks" , "player.getCurrentFrame() = " + player.getCurrentFrame());
		src = playerSheet.getBox(player.getCurrentFrame());
		dst = calculateDestinationRect(src, 0, this.getCanvasHeight(), scalePosition(player.getCurrentPathPosition()));
		bitmap = playerSheet.getBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
		src = playerSheet.getMirroredBox(opponent.getCurrentFrame());
		dst = calculateDestinationRect(src, 0, this.getCanvasHeight(), scalePosition(opponent.getCurrentPathPosition()));
		bitmap = playerSheet.getMirroredBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
		player.advance();
		opponent.advance();
		
	}
	
	public void setBattleAnimation(int battleAnimation) {
		switch(battleAnimation) {
		case IDLE:
			this.battleAnimation = battleAnimation;
			player.setFramesMill(stickIdleAnimation);
			opponent.setFramesMill(stickIdleAnimation);
			player.noPath();
			opponent.noPath();
			break;
		case ENGAGE:
			this.battleAnimation = battleAnimation;
			player.setFramesMill(stickRunAnimation);
			opponent.setFramesMill(stickRunAnimation);
			player.setPathX(new NumberMill(pathRunLeftToMiddle,NumberMill.ONCE));
			opponent.setPathX(new NumberMill(pathRunRightToMiddle,NumberMill.ONCE));
			break;
		case DEFEND_HIGH_VS_DEFEND_HIGH:
			this.battleAnimation = battleAnimation;
			player.setFramesMill(stickDefendHighAnimation);
			opponent.setFramesMill(stickDefendHighAnimation);
			player.noPath();
			opponent.noPath();
			break;
		case DEFEND_HIGH_VS_ATTACK_HIGH:
			this.battleAnimation = battleAnimation;
			player.setFramesMill(stickDefendHighAnimation);
			opponent.setFramesMill(stickAttackHighAnimation);
			player.noPath();
			opponent.noPath();
			break;
		case ATTACK_HIGH_VS_DEFEND_HIGH:
			this.battleAnimation = battleAnimation;
			player.setFramesMill(stickAttackHighAnimation);
			opponent.setFramesMill(stickDefendHighAnimation);
			player.noPath();
			opponent.noPath();
			break;
		default:
			Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
			break;
		}
	}
}