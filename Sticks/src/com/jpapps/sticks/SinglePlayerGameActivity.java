package com.jpapps.sticks;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SinglePlayerGameActivity extends Activity {

	private FightSurfaceView fightSurfaceView;
	private StickFightRenderer fightRenderer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().setWindowAnimations(android.R.anim.slide_in_left);
	    setContentView(R.layout.activity_single_player_game);
	    fightSurfaceView = (FightSurfaceView) findViewById(R.id.single_player_game_surface);
	    fightRenderer = fightSurfaceView.getRenderer();
        
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
    
    public void tap(View view) {
		switch(view.getId()) {
		case R.id.button_defendhigh:
			//This should eventually be changed to process some game logic then decide the outcome.
			//Just using it to test out animations right now.
			fightSurfaceView.getRenderer().setBattleAnimation(StickFightRenderer.ENGAGE);
			break;
		default:
			// What button did you push this time?
			break;
		}
	}

}