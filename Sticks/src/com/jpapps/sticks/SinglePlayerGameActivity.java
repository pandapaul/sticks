package com.jpapps.sticks;

import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlayerGameActivity extends Activity {

	private FightSurfaceView fightSurfaceView;
	private SticksLogicEngine logicEngine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setWindowAnimations(android.R.anim.slide_in_left);
	    setContentView(R.layout.fight_layout);
	    fightSurfaceView = (FightSurfaceView) findViewById(R.id.single_player_game_surface);
	    
	    logicEngine = new SticksLogicEngine();
        
	    //Give the textviews the right font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ampersand.ttf");
        TextView playerNameTextView = (TextView) findViewById(R.id.ui_playername);
        playerNameTextView.setTypeface(font,1);
        TextView opponentNameTextView = (TextView) findViewById(R.id.ui_opponentname);
        opponentNameTextView.setTypeface(font,1);
        TextView defendTextView = (TextView) findViewById(R.id.ui_defendlabel);
        defendTextView.setTypeface(font,1);
        TextView attackTextView = (TextView) findViewById(R.id.ui_attacklabel);
        attackTextView.setTypeface(font,1);
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.w(this.getClass().getName(), "SIS called");
    }
    
    protected int randOpponentSelection() {
    	double rando = new Random().nextDouble();
    	if(rando < 0.25 ) {
    		return StickFightRenderer.DEFEND_HIGH;
    	} else if(rando < 0.5) {
    		return StickFightRenderer.ATTACK_HIGH;
    	} else if(rando < 0.75) {
    		return StickFightRenderer.DEFEND_LOW;
    	} else {
    		return StickFightRenderer.ATTACK_LOW;
    	}
    }
    
    public void updateHealthBars(int playerHealth, int opponentHealth) {
		ImageView playerBar1 = (ImageView) findViewById(R.id.ui_healthbar_left_1);
		ImageView playerBar2 = (ImageView) findViewById(R.id.ui_healthbar_left_2);
		ImageView playerBar3 = (ImageView) findViewById(R.id.ui_healthbar_left_3);
		ImageView opponentBar1 = (ImageView) findViewById(R.id.ui_healthbar_right_1);
		ImageView opponentBar2 = (ImageView) findViewById(R.id.ui_healthbar_right_2);
		ImageView opponentBar3 = (ImageView) findViewById(R.id.ui_healthbar_right_3);
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
    
    public void selectMove(View view) {
    	
    	//Will eventually add something here that collapses or hides the buttons
    	
    	int opponentChoice = randOpponentSelection();
    	int playerChoice = 0;
    	int result = 0;
		switch(view.getId()) {
		case R.id.button_defendhigh:
			playerChoice = StickFightRenderer.DEFEND_HIGH;
			break;
		case R.id.button_attackhigh:
			playerChoice = StickFightRenderer.ATTACK_HIGH;
			break;
		case R.id.button_defendlow:
			playerChoice = StickFightRenderer.DEFEND_LOW;
			break;
		case R.id.button_attacklow:
			playerChoice = StickFightRenderer.ATTACK_LOW;
			break;
		default:
			// Do what now?
			break;
		}
		result = logicEngine.processTurn(playerChoice, opponentChoice);
		//Toast.makeText(this, "Player Chose: " + playerChoice + "\nOpponent Chose: " + opponentChoice + "\nResult is " + result, Toast.LENGTH_LONG).show();
		fightSurfaceView.getRenderer().engage(playerChoice, opponentChoice, result);
	}

}