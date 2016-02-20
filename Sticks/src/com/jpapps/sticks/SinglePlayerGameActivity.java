package com.jpapps.sticks;

import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SinglePlayerGameActivity extends Activity {

	private FightSurfaceView fightSurfaceView;
	private SticksLogicEngine logicEngine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setWindowAnimations(android.R.anim.slide_in_left);
	    setContentView(R.layout.fight_layout);
	    fightSurfaceView = (FightSurfaceView) findViewById(R.id.single_player_game_surface);
	    //Pass this activity to rendering thread so that it can modify extra UI components like health bars
	    fightSurfaceView.getRenderer().setActivity(this);
	    
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
    
    public void selectMove(View view) {
    	
    	//Will eventually add something here that collapses or hides the buttons
    	
    	StickFightRenderer fightRenderer = fightSurfaceView.getRenderer();
    	
    	if(fightRenderer.getBattleState() == StickFightRenderer.WAITING || fightRenderer.getBattleState() == StickFightRenderer.ATTACK_FINISHED) {
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
			fightRenderer.engage(playerChoice, opponentChoice, result);
    	}
    	
	}

}