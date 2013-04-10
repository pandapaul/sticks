package com.jpapps.sticks;

import com.jpapps.pandroidGL.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;

public class StickFightRenderer extends SurfaceRenderer {
	
	//Battle state variables
	private int battleState;
	private int result;
	public final static int WAITING = -1;
	public final static int FIGHTING = 0;
	public final static int PLAYER_DAMAGED = 1;
	public final static int OPPONENT_DAMAGED = 2;
	public final static int NONE_DAMAGED = 3;
	public final static int BOTH_DAMAGED = 4;
	public final static int PLAYER_PARRY = 5;
	public final static int OPPONENT_PARRY = 6;
	public final static int ATTACK_FINISHED = 7;
	public final static int VICTORY = 8;
	public final static int TIE = 9;
	public final static int DEFEAT = 10;
	private boolean engaging = false;
	
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
	private SpriteSheet bloodSheet;
	
	//Sprite animation frames
	public final static int[] framesStickIdle = {1,1,2,2,3,4,};
	public final static int[] framesStickRun = {4,5,6,7,8,9,10,11,12};
	public final static int[] framesStickDefendHigh = {13,14,15,16,17};
	public final static int[] framesStickAttackHigh = {18,19,20,21,22};
	public final static int[] framesStickDefendLow = {23,24,25,26,27};
	public final static int[] framesStickAttackLow = {28,29,30,31,32};
	public final static int[] framesBlood = {1,2,3,4,5,6,7,8};
	
	//Sprite animation paths as changes in position (in % of canvas)
	public final static int[] pathRunLeftToMiddle = {0,0,2,2,4,4,4,6,4,4};
	public final static int[] pathRunRightToMiddle = {0,0,-2,-2,-4,-4,-4,-6,-4,-4};
	public final static int[] pathAttackFromLeft = {0,1,1,2,6};
	public final static int[] pathAttackFromRight = {0,-1,-1,-2,-6};
	public final static int[] pathDefendFromLeft = {0,1,2,4};
	public final static int[] pathDefendFromRight = {0,-1,-2,-4};
	public final static int[] pathBloodFall = {0,0,0,-3,-5,-5,-10,0,0};
	
	//Drawing dimensions (in % of canvas)
	private final static int stickWidth = 25;
	private final static int stickHeight = 80;
	private final static int bloodWidth = 12;
	private final static int bloodHeight = 30;
	
	/*
	//Coords for points of impact in relation to stick man
	private final static int[] attackingHigh_HitHigh = {(int) (0.35*stickWidth), (int)(0.29*stickHeight)};
	private final static int[] attackingHigh_HitLow = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	private final static int[] attackingLow_HitHigh = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	private final static int[] attackingLow_HitLow = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	private final static int[] defendingHigh_HitHigh = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	private final static int[] defendingHigh_HitLow = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	private final static int[] defendingLow_HitHigh = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	private final static int[] defendingLow_HitLow = {(int) (0.25*stickWidth), (int)(0.675*stickHeight)};
	
	//Point of impact states
	public final static int IMPACT_AH_H = 1;
	public final static int IMPACT_AH_L = 2;
	public final static int IMPACT_AL_H = 3;
	public final static int IMPACT_AL_L = 4;
	public final static int IMPACT_DH_H = 5;
	public final static int IMPACT_DH_L = 6;
	public final static int IMPACT_DL_H = 7;
	public final static int IMPACT_DL_L = 8;
	private int playerImpact;
	private int opponentImpact;
	*/
	
	//Game objects
	private StickMan player;
	private StickMan opponent;
	private AnimatedObject playerBlood;
	private AnimatedObject opponentBlood;
	
	//Game activity for access to UI component
	private Activity activity;
	
	//Runnable for performing actions on UI thread
	Runnable UIRunnable;
	
	public StickFightRenderer(SurfaceHolder surfaceHolder, Context context, int time) {
		super(surfaceHolder, context, time);
		
		//Get sprite sheets ready
		playerSheet = MainMenuActivity.playerSheet;
		bloodSheet = MainMenuActivity.bloodSheet;
				
		//Get animated objects ready
		player = new StickMan(new NumberMill(framesStickIdle, NumberMill.CYCLE), stickWidth, stickHeight, 0,0);
		opponent = new StickMan(new NumberMill(framesStickIdle, NumberMill.CYCLE), stickWidth, stickHeight, 75,0);
		
		playerBlood = new AnimatedObject(new NumberMill(framesBlood, NumberMill.ONCE_PERSIST), bloodWidth, bloodHeight, 0,0, null, new NumberMill(pathBloodFall, NumberMill.ONCE_PERSIST));
		opponentBlood = new AnimatedObject(new NumberMill(framesBlood, NumberMill.ONCE_PERSIST), bloodWidth, bloodHeight, 75,0, null, new NumberMill(pathBloodFall, NumberMill.ONCE_PERSIST));
		
		//Initialize states
		playerChoice = IDLE;
		opponentChoice = IDLE;
		battleState = WAITING;
		result = FIGHTING;
	}
	
	protected int[] scaleToCanvas(int[] arenaCoords) {
		float arenaX = arenaCoords[0];
		float arenaY = arenaCoords[1];
		int canvasX = Math.round((arenaX/100)*this.getCanvasWidth());
		int canvasY = Math.round((arenaY/100)*this.getCanvasHeight());
		int[] canvasCoords = {canvasX, canvasY};
		return canvasCoords;
	}
	
	protected Rect calculateDestinationRect(AnimatedObject ao) {
		int[] pos = {ao.getX(), ao.getY()};
		int[] dim = {ao.getWidth(), ao.getHeight()};
		pos[1] = 100-dim[1]-pos[1];
		int[] topLeft = scaleToCanvas(pos);
		dim = scaleToCanvas(dim);
		int[] bottomRight = {topLeft[0] + dim[0], topLeft[1] + dim[1]};
		dim = scaleToCanvas(dim);
		Rect s = new Rect(topLeft[0],topLeft[1],bottomRight[0],bottomRight[1]);
		return s;
	}
	
	public void updateHealthBars(final int playerHealth, final int opponentHealth, final Context context) {
		
		UIRunnable = new Runnable() {
			@Override
			public void run() {
				
				//Get access to health bars
				
				ImageView playerBar1 = (ImageView) activity.findViewById(R.id.ui_healthbar_left_1);
				ImageView playerBar2 = (ImageView) activity.findViewById(R.id.ui_healthbar_left_2);
				ImageView playerBar3 = (ImageView) activity.findViewById(R.id.ui_healthbar_left_3);
				ImageView opponentBar1 = (ImageView) activity.findViewById(R.id.ui_healthbar_right_1);
				ImageView opponentBar2 = (ImageView) activity.findViewById(R.id.ui_healthbar_right_2);
				ImageView opponentBar3 = (ImageView) activity.findViewById(R.id.ui_healthbar_right_3);
				switch (playerHealth) {
				case 6:
					playerBar1.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar2.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar3.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar1.setVisibility(View.VISIBLE);
					playerBar2.setVisibility(View.VISIBLE);
					playerBar3.setVisibility(View.VISIBLE);
					break;
				case 5:
					playerBar1.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar2.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar3.setImageResource(R.drawable.ui_healthbar_half);
					playerBar1.setVisibility(View.VISIBLE);
					playerBar2.setVisibility(View.VISIBLE);
					playerBar3.setVisibility(View.VISIBLE);
					break;
				case 4:
					playerBar1.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar2.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar3.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar1.setVisibility(View.VISIBLE);
					playerBar2.setVisibility(View.VISIBLE);
					playerBar3.setVisibility(View.INVISIBLE);
					break;
				case 3:
					playerBar1.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar2.setImageResource(R.drawable.ui_healthbar_half);
					playerBar3.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar1.setVisibility(View.VISIBLE);
					playerBar2.setVisibility(View.VISIBLE);
					playerBar3.setVisibility(View.INVISIBLE);
					break;
				case 2:
					playerBar1.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar2.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar3.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar1.setVisibility(View.VISIBLE);
					playerBar2.setVisibility(View.INVISIBLE);
					playerBar3.setVisibility(View.INVISIBLE);
					break;
				case 1:
					playerBar1.setImageResource(R.drawable.ui_healthbar_half);
					playerBar2.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar3.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar1.setVisibility(View.VISIBLE);
					playerBar2.setVisibility(View.INVISIBLE);
					playerBar3.setVisibility(View.INVISIBLE);
					break;
				default:
					playerBar1.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar2.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar3.setImageResource(R.drawable.ui_healthbar_unit);
					playerBar1.setVisibility(View.INVISIBLE);
					playerBar2.setVisibility(View.INVISIBLE);
					playerBar3.setVisibility(View.INVISIBLE);
					break;
				}
				switch (opponentHealth) {
				case 6:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar1.setVisibility(View.VISIBLE);
					opponentBar2.setVisibility(View.VISIBLE);
					opponentBar3.setVisibility(View.VISIBLE);
					break;
				case 5:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_half);
					opponentBar1.setVisibility(View.VISIBLE);
					opponentBar2.setVisibility(View.VISIBLE);
					opponentBar3.setVisibility(View.VISIBLE);
					break;
				case 4:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar1.setVisibility(View.VISIBLE);
					opponentBar2.setVisibility(View.VISIBLE);
					opponentBar3.setVisibility(View.INVISIBLE);
					break;
				case 3:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_half);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar1.setVisibility(View.VISIBLE);
					opponentBar2.setVisibility(View.VISIBLE);
					opponentBar3.setVisibility(View.INVISIBLE);
					break;
				case 2:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar1.setVisibility(View.VISIBLE);
					opponentBar2.setVisibility(View.INVISIBLE);
					opponentBar3.setVisibility(View.INVISIBLE);
					break;
				case 1:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_half);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar1.setVisibility(View.VISIBLE);
					opponentBar2.setVisibility(View.INVISIBLE);
					opponentBar3.setVisibility(View.INVISIBLE);
					break;
				default:
					opponentBar1.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar2.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar3.setImageResource(R.drawable.ui_healthbar_unit);
					opponentBar1.setVisibility(View.INVISIBLE);
					opponentBar2.setVisibility(View.INVISIBLE);
					opponentBar3.setVisibility(View.INVISIBLE);
					break;
				}
			}
		};
		
		//Run the stuff on the UI thread, becase we gotta be able to modify the views
		//...really not a fan of this...
		if(activity!=null)
			activity.runOnUiThread(UIRunnable);
		
	}
		
	@Override
	protected void update() {
		if(battleState == VICTORY) {
			
		}
		else if(battleState == TIE) {
			
		}
		else if(battleState == DEFEAT) {
			
		}
		else if(battleState == FIGHTING) {
			if(player.getFramesMill().isFinished()) {
				if(engaging) {
					//Sticks are fully engaged. Move on to attack and defend animations.
					engaging = false;
					switch(playerChoice) {
					case DEFEND_HIGH:
						player.setFramesMill(new NumberMill(framesStickDefendHigh, NumberMill.ONCE_PERSIST));
						player.setPathX(new NumberMill(pathDefendFromLeft, NumberMill.ONCE));
						break;
					case DEFEND_LOW:
						player.setFramesMill(new NumberMill(framesStickDefendLow, NumberMill.ONCE_PERSIST));
						player.setPathX(new NumberMill(pathDefendFromLeft, NumberMill.ONCE));
						break;
					case ATTACK_HIGH:
						player.setFramesMill(new NumberMill(framesStickAttackHigh, NumberMill.ONCE_PERSIST));
						player.setPathX(new NumberMill(pathAttackFromLeft, NumberMill.ONCE));
						break;
					case ATTACK_LOW:
						player.setFramesMill(new NumberMill(framesStickAttackLow, NumberMill.ONCE_PERSIST));
						player.setPathX(new NumberMill(pathAttackFromLeft, NumberMill.ONCE));
						break;
					default:
						Log.w("Sticks","StickFightRenderer received an unknown battleAnimation state.");
						break;
					}
					switch(opponentChoice) {
					case DEFEND_HIGH:
						opponent.setFramesMill(new NumberMill(framesStickDefendHigh, NumberMill.ONCE_PERSIST));
						opponent.setPathX(new NumberMill(pathDefendFromRight, NumberMill.ONCE));
						break;
					case DEFEND_LOW:
						opponent.setFramesMill(new NumberMill(framesStickDefendLow, NumberMill.ONCE_PERSIST));
						opponent.setPathX(new NumberMill(pathDefendFromRight, NumberMill.ONCE));
						break;
					case ATTACK_HIGH:
						opponent.setFramesMill(new NumberMill(framesStickAttackHigh, NumberMill.ONCE_PERSIST));
						opponent.setPathX(new NumberMill(pathAttackFromRight, NumberMill.ONCE));
						break;
					case ATTACK_LOW:
						opponent.setFramesMill(new NumberMill(framesStickAttackLow, NumberMill.ONCE_PERSIST));
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
		else if(battleState != ATTACK_FINISHED){			
			//Reset the blood animations
			playerBlood.reset();
			opponentBlood.reset();
			//Damage and bleed whomever got hit
			switch (battleState) {
			case PLAYER_DAMAGED:
				player.setHealth(player.getHealth()-2);
				player.setBleeding(true);
				playerBlood.setPosition(player.getX(),player.getY()+player.getHeight()/3);
				battleState = ATTACK_FINISHED;
				break;
			case OPPONENT_DAMAGED:
				opponent.setHealth(opponent.getHealth()-2);
				opponent.setBleeding(true);
				opponentBlood.setPosition(opponent.getX()+opponent.getWidth()/2,opponent.getY()+opponent.getHeight()/3);
				battleState = ATTACK_FINISHED;
				break;
			case BOTH_DAMAGED:
				player.setHealth(player.getHealth()-1);
				player.setBleeding(true);
				playerBlood.setPosition(player.getX(),player.getY()+player.getHeight()/3);
				opponent.setHealth(opponent.getHealth()-1);
				opponent.setBleeding(true);
				opponentBlood.setPosition(opponent.getX()+opponent.getWidth()/2,opponent.getY()+opponent.getHeight()/3);
				battleState = ATTACK_FINISHED;
				break;
			case PLAYER_PARRY:
				opponent.setHealth(opponent.getHealth()-2);
				opponent.setBleeding(true);
				opponentBlood.setPosition(opponent.getX()+opponent.getWidth()/2,opponent.getY()+opponent.getHeight()/3);
				battleState = ATTACK_FINISHED;
				break;
			case OPPONENT_PARRY:
				player.setHealth(player.getHealth()-2);
				player.setBleeding(true);
				playerBlood.setPosition(player.getX(),player.getY()+player.getHeight()/3);
				battleState = ATTACK_FINISHED;
				break;
			case NONE_DAMAGED:
				battleState = ATTACK_FINISHED;
				break;
			default:
				break;
			}
			this.updateHealthBars(player.getHealth(), opponent.getHealth(), this.getContext());
		}
		else if(battleState == ATTACK_FINISHED) {
			if(player.getHealth() <= 0) {
				if(opponent.getHealth() <= 0)
					this.tie();
				else
					this.defeat();
			}
			else if(opponent.getHealth() <= 0)
				this.victory();
			else if(playerBlood.getFramesMill().isFinished() && opponentBlood.getFramesMill().isFinished()) {
				
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
		
		//Log.w("Sticks" , "player.getCurrentFrame() = " + player.getCurrentFrame());
		src = playerSheet.getBox(player.getCurrentFrame());
		dst = calculateDestinationRect(player);
		//Log.w("Sticks", dst.toShortString());
		bitmap = playerSheet.getBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
		src = playerSheet.getMirroredBox(opponent.getCurrentFrame());
		dst = calculateDestinationRect(opponent);
		bitmap = playerSheet.getMirroredBitmap();
		canvas.drawBitmap(bitmap, src, dst, null);
		
		if(player.isBleeding()) {
			src = bloodSheet.getBox(playerBlood.getCurrentFrame());
			dst = this.calculateDestinationRect(playerBlood);
			bitmap = bloodSheet.getBitmap();
			canvas.drawBitmap(bitmap, src, dst, null);
			playerBlood.advance();
		}
		
		if(opponent.isBleeding()) {
			src = bloodSheet.getBox(opponentBlood.getCurrentFrame());
			dst = this.calculateDestinationRect(opponentBlood);
			bitmap = bloodSheet.getBitmap();
			canvas.drawBitmap(bitmap, src, dst, null);
			opponentBlood.advance();
		}
		
		player.advance();
		opponent.advance();
		
	}
	
	protected void defeat() {
		player.setWidth(0);
		this.battleState = DEFEAT;
	}
	
	protected void tie() {
		player.setWidth(0);
		opponent.setWidth(0);
		this.battleState = TIE;
	}

	protected void victory() {
		opponent.setWidth(0);
		this.battleState = VICTORY;
	}
	
	protected void engage(int playerChoice, int opponentChoice, int result) {
		engaging = true;
		this.playerChoice = playerChoice;
		this.opponentChoice = opponentChoice;
		this.battleState = FIGHTING;
		this.result = result;
		
		player.reset();
		opponent.reset();
		
		player.setFramesMill(new NumberMill(framesStickRun, NumberMill.ONCE_PERSIST));
		opponent.setFramesMill(new NumberMill(framesStickRun, NumberMill.ONCE_PERSIST));
		player.setPathX(new NumberMill(pathRunLeftToMiddle,NumberMill.ONCE));
		opponent.setPathX(new NumberMill(pathRunRightToMiddle,NumberMill.ONCE));
	}
	
	public int getBattleState() {
		return battleState;
	}

	public void setBattleState(int battleState) {
		this.battleState = battleState;
	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}	
}