package com.jpapps.sticks;

import com.jpapps.pandroidGL.*;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StickFightRenderer extends SurfaceRenderer {
	
	//Battle state variables
	private int battleState;
	private int result;
	public final static int FIGHTING = 0;
	public final static int PLAYER_DAMAGED = 1;
	public final static int OPPONENT_DAMAGED = 2;
	public final static int NONE_DAMAGED = 3;
	public final static int BOTH_DAMAGED = 4;
	public final static int FINISHED = 5;
	
	//Move choice variables
	private int playerChoice;
	private int opponentChoice;
	public final static int IDLE = 0;
	public final static int DEFEND_HIGH = 1;
	public final static int DEFEND_LOW = 2;
	public final static int ATTACK_HIGH = 3;
	public final static int ATTACK_LOW = 4;
	
	//Sprite sheets
	private SpriteSheet playerSheet;
	
	//Sprite animations
	public final static int[] framesStickIdle = {1,1,2,2,3,4,};
	public final static int[] framesStickRun = {4,5,6,7,8,9,10,11,12};
	public final static int[] framesStickDefendHigh = {13,14,15,16,17};
	public final static int[] framesStickAttackHigh = {18,19,20,21,22};
	public final static int[] framesStickDefendLow = {23,24,25,26,27};
	public final static int[] framesStickAttackLow = {28,29,30,31,32};
	public final static int[] pathRunLeftToMiddle = {0,0,2,4,8,12,16,22,26,30};
	public final static int[] pathRunRightToMiddle = {75,75,73,71,67,63,59,53,49,45};
	public final static int[] pathAttackFromLeft = {30,30,31,32,35};
	public final static int[] pathAttackFromRight = {45,45,44,43,40};
	public final static int[] pathDefendFromLeft = {30,30,31,33};
	public final static int[] pathDefendFromRight = {45,45,44,42};
	
	private final static double stickWidth = 0.25; //Percent of canvas width
	
	private StickMan player;
	private StickMan opponent;
	
	private boolean engaging = false;
	
	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		
		//Get sprite sheets ready
		playerSheet = MainMenuActivity.playerSheet;
				
		//Get animated objects ready
		player = new StickMan(new NumberMill(framesStickIdle, NumberMill.CYCLE));
		opponent = new StickMan(new NumberMill(framesStickIdle, NumberMill.CYCLE));
		player.noPath();
		int[] n = {75};
		opponent.setPathX(new NumberMill(n,NumberMill.ONCE));
		opponent.setPathY(null);
		
		//Initialize states
		playerChoice = IDLE;
		opponentChoice = IDLE;
		battleState = FIGHTING;
		result = FIGHTING;
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
		if(battleState == FIGHTING) {
			if(player.getFramesMill().isFinished()) {
				if(engaging) {
					//Sticks are fully engaged. Move on to attack and defend animations.
					engaging = false;
					switch(playerChoice) {
					case DEFEND_HIGH:
						player.setFramesMill(new NumberMill(framesStickDefendHigh, NumberMill.ONCE));
						player.setPathX(new NumberMill(pathDefendFromLeft, NumberMill.ONCE));
						break;
					case DEFEND_LOW:
						player.setFramesMill(new NumberMill(framesStickDefendLow, NumberMill.ONCE));
						player.setPathX(new NumberMill(pathDefendFromLeft, NumberMill.ONCE));
						break;
					case ATTACK_HIGH:
						player.setFramesMill(new NumberMill(framesStickAttackHigh, NumberMill.ONCE));
						player.setPathX(new NumberMill(pathAttackFromLeft, NumberMill.ONCE));
						break;
					case ATTACK_LOW:
						player.setFramesMill(new NumberMill(framesStickAttackLow, NumberMill.ONCE));
						player.setPathX(new NumberMill(pathAttackFromLeft, NumberMill.ONCE));
						break;
					default:
						Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
						break;
					}
					switch(opponentChoice) {
					case DEFEND_HIGH:
						opponent.setFramesMill(new NumberMill(framesStickDefendHigh, NumberMill.ONCE));
						opponent.setPathX(new NumberMill(pathDefendFromRight, NumberMill.ONCE));
						break;
					case DEFEND_LOW:
						opponent.setFramesMill(new NumberMill(framesStickDefendLow, NumberMill.ONCE));
						opponent.setPathX(new NumberMill(pathDefendFromRight, NumberMill.ONCE));
						break;
					case ATTACK_HIGH:
						opponent.setFramesMill(new NumberMill(framesStickAttackHigh, NumberMill.ONCE));
						opponent.setPathX(new NumberMill(pathAttackFromRight, NumberMill.ONCE));
						break;
					case ATTACK_LOW:
						opponent.setFramesMill(new NumberMill(framesStickAttackLow, NumberMill.ONCE));
						opponent.setPathX(new NumberMill(pathAttackFromRight, NumberMill.ONCE));
						break;
					default:
						Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
						break;
					}
				}
				else {
					//The battle state is fighting, the animations are finished, and they were doing something other than engaging.
					battleState = result;
				}
			}
			//The battle state is fighting, but the animations aren't finished yet.
		}
		else if(battleState != FINISHED){
			//The battle state is something other than fighting
			switch (battleState) {
			case PLAYER_DAMAGED:
				player.setHealth(player.getHealth()-1);
				battleState = FINISHED;
				break;
			case OPPONENT_DAMAGED:
				opponent.setHealth(opponent.getHealth()-1);
				battleState = FINISHED;
				break;
			case BOTH_DAMAGED:
				player.setHealth(player.getHealth()-1);
				opponent.setHealth(opponent.getHealth()-1);
				battleState = FINISHED;
				break;
			case NONE_DAMAGED:
				battleState = FINISHED;
				break;
			default:
				break;
			}
			//Temporary thing for tracking player health until I can get the health bars worked out
			Log.w("Sticks", "Player Health = " + player.getHealth() + " Opponent Health = " + opponent.getHealth());
		}
		else {
			//The battle state is finished
		}
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//Wipe the canvas to white
		canvas.drawColor(Color.WHITE);
		
		Rect src;
		Rect dst;
		Bitmap bitmap;
		int scaledStickWidth = (int) Math.round(stickWidth*(float)this.getCanvasWidth());
			
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
	
	protected synchronized void engage(int playerChoice, int opponentChoice, int result) {
		engaging = true;
		this.playerChoice = playerChoice;
		this.opponentChoice = opponentChoice;
		this.battleState = FIGHTING;
		this.result = result;
		player.setFramesMill(new NumberMill(framesStickRun, NumberMill.ONCE));
		opponent.setFramesMill(new NumberMill(framesStickRun, NumberMill.ONCE));
		player.setPathX(new NumberMill(pathRunLeftToMiddle,NumberMill.ONCE));
		opponent.setPathX(new NumberMill(pathRunRightToMiddle,NumberMill.ONCE));
		player.reset();
		opponent.reset();
	}
	
	public int getBattleState() {
		return battleState;
	}

	public void setBattleState(int battleState) {
		this.battleState = battleState;
	}

}