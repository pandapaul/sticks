package com.jpapps.sticks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class StickFightRenderer extends SurfaceRenderer {
	
	//Battle variables
	private int playerChoice;
	private int opponentChoice;
	public final static int IDLE = 0;
	//public final static int ENGAGE = 1;
	public final static int DEFEND_HIGH = 2;
	public final static int DEFEND_LOW = 3;
	public final static int ATTACK_HIGH = 4;
	public final static int ATTACK_LOW = 5;
	
	//Sprite sheets
	private SpriteSheet playerSheet;
	
	//Sprite animations
	public final static int[] stickIdleFrames = {1,1,2,2,3,3,4,4};
	public final static int[] stickRunFrames = {5,6,7,8,9,10,11,12};
	public final static int[] stickDefendHighFrames = {13,14,15,16};
	public final static int[] stickAttackHighFrames = {17,18,19};
	public final static int[] pathRunLeftToMiddle = {0,2,7,12,19,26,32,35};
	public final static int[] pathRunRightToMiddle = {75,73,68,63,56,49,43,40};
	
	private NumberMill stickIdleAnimation;
	private NumberMill stickRunAnimation;
	private NumberMill stickDefendHighAnimation;
	private NumberMill stickAttackHighAnimation;
	
	private AnimatedObject player;
	private AnimatedObject opponent;
	
	private boolean engaging = false;
	
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
		player.noPath();
		int[] n = {75};
		opponent.setPathX(new NumberMill(n,NumberMill.ONCE));
		opponent.setPathY(null);
		
		playerChoice = IDLE;
		opponentChoice = IDLE;
	}
	
	protected Rect calculateDestinationRect(Rect r, float w, float h, int[] coords) {
		if(w<=0) {
			w = (float)r.width()/(float)r.height()*h;
		}
		if(h<=0) {
			h = (float)r.height()/(float)r.width()*w;
		}
		Rect s = new Rect(r);
		int canvasHeight = this.getCanvasHeight();
		s.set(coords[0], canvasHeight - (int)h - coords[1], coords[0]+(int)w, canvasHeight - coords[1]);
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
		if(engaging && player.getCurrentFrame() == stickRunFrames[stickRunFrames.length-1]) {
			engaging = false;
			//Sticks are fully engaged. Move on to next animation.
			switch(playerChoice) {
			case DEFEND_HIGH:
				player.setFramesMill(stickDefendHighAnimation);
				//player.noPath();
				break;
			case ATTACK_HIGH:
				player.setFramesMill(stickAttackHighAnimation);
				//player.noPath();
				break;
			default:
				Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
				break;
			}
			switch(opponentChoice) {
			case DEFEND_HIGH:
				opponent.setFramesMill(stickDefendHighAnimation);
				//opponent.noPath();
				break;
			case ATTACK_HIGH:
				opponent.setFramesMill(stickAttackHighAnimation);
				//opponent.noPath();
				break;
			default:
				Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
				break;
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
		int scaledStickWidth = (int) Math.round(0.25*(float)this.getCanvasWidth());
			
		//Log.w("Sticks" , "player.getCurrentFrame() = " + player.getCurrentFrame());
		src = playerSheet.getBox(player.getCurrentFrame());
		dst = calculateDestinationRect(src, scaledStickWidth, 0, scalePosition(player.getCurrentPathPosition()));
		bitmap = playerSheet.getBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
		src = playerSheet.getMirroredBox(opponent.getCurrentFrame());
		dst = calculateDestinationRect(src, scaledStickWidth, 0, scalePosition(opponent.getCurrentPathPosition()));
		bitmap = playerSheet.getMirroredBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
		player.advance();
		opponent.advance();
		
	}
	
	protected void engage() {
		engaging = true;
		player.setFramesMill(stickRunAnimation);
		opponent.setFramesMill(stickRunAnimation);
		player.setPathX(new NumberMill(pathRunLeftToMiddle,NumberMill.ONCE));
		opponent.setPathX(new NumberMill(pathRunRightToMiddle,NumberMill.ONCE));
		player.restart();
		opponent.restart();
	}
	
	public void setChoices(int playerChoice, int opponentChoice) {
		switch(playerChoice) {
		case DEFEND_HIGH:
			this.playerChoice = playerChoice;
			engage();
			break;
		case ATTACK_HIGH:
			this.playerChoice = playerChoice;
			engage();
			break;
		default:
			Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
			break;
		}
		switch(opponentChoice) {
		case DEFEND_HIGH:
			this.opponentChoice = opponentChoice;
			engage();
			break;
		case ATTACK_HIGH:
			this.opponentChoice = opponentChoice;
			engage();
			break;
		default:
			Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
			break;
		}
	}
}