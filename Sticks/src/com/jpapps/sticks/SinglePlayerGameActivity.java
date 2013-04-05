package com.jpapps.sticks;

import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SinglePlayerGameActivity extends Activity {

	private FightSurfaceView fightSurfaceView;
	private SticksLogicEngine logicEngine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setWindowAnimations(android.R.anim.slide_in_left);
	    setContentView(R.layout.activity_single_player_game);
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
		fightSurfaceView.getRenderer().engage(playerChoice, opponentChoice, result);
	}

}