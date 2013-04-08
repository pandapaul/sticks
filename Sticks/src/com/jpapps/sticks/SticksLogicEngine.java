package com.jpapps.sticks;

import java.util.Random;

public class SticksLogicEngine {
	
	private static final double chanceToParry = 0.2;
	
	private int movesPerTurn;
	
	public SticksLogicEngine() {
		movesPerTurn = 1;
	}
	public SticksLogicEngine(int m) {
		movesPerTurn = m;
	}
	
	public int processTurn(int playerMoveChoice, int opponentMoveChoice) {
		
		int result = 0;
		
		boolean parry = false;
		
		double rando = new Random().nextDouble();
		if(rando < chanceToParry) {
			parry = true;
		}
		
		switch (playerMoveChoice) {
		
			case StickFightRenderer.DEFEND_HIGH:
				switch (opponentMoveChoice) {
					case StickFightRenderer.DEFEND_HIGH:
						result = StickFightRenderer.NONE_DAMAGED;
						break;
					case StickFightRenderer.DEFEND_LOW:
						result = StickFightRenderer.NONE_DAMAGED;
						break;
					case StickFightRenderer.ATTACK_HIGH:
						if(parry) {
							result = StickFightRenderer.PLAYER_PARRY;
						}
						else {
							result = StickFightRenderer.NONE_DAMAGED;
						}
						break;
					case StickFightRenderer.ATTACK_LOW:
						result = StickFightRenderer.PLAYER_DAMAGED;
						break;
				}
				break;
				
			case StickFightRenderer.DEFEND_LOW:
				switch (opponentMoveChoice) {
					case StickFightRenderer.DEFEND_HIGH:
						result = StickFightRenderer.NONE_DAMAGED;
						break;
					case StickFightRenderer.DEFEND_LOW:
						result = StickFightRenderer.NONE_DAMAGED;
						break;
					case StickFightRenderer.ATTACK_HIGH:
						result = StickFightRenderer.PLAYER_DAMAGED;
						break;
					case StickFightRenderer.ATTACK_LOW:
						if(parry) {
							result = StickFightRenderer.PLAYER_PARRY;
						}
						else {
							result = StickFightRenderer.NONE_DAMAGED;
						}
						break;
				}
				break;
				
			case StickFightRenderer.ATTACK_HIGH:
				switch (opponentMoveChoice) {
					case StickFightRenderer.DEFEND_HIGH:
						if(parry) {
							result = StickFightRenderer.OPPONENT_PARRY;
						}
						else {
							result = StickFightRenderer.NONE_DAMAGED;
						}
						break;
					case StickFightRenderer.DEFEND_LOW:
						result = StickFightRenderer.OPPONENT_DAMAGED;
						break;
					case StickFightRenderer.ATTACK_HIGH:
						result = StickFightRenderer.BOTH_DAMAGED;
						break;
					case StickFightRenderer.ATTACK_LOW:
						result = StickFightRenderer.BOTH_DAMAGED;
						break;
					}
				break;
			case StickFightRenderer.ATTACK_LOW:
				switch (opponentMoveChoice) {
					case StickFightRenderer.DEFEND_HIGH:
						result = StickFightRenderer.OPPONENT_DAMAGED;
						break;
					case StickFightRenderer.DEFEND_LOW:
						if(parry) {
							result = StickFightRenderer.OPPONENT_PARRY;
						}
						else {
							result = StickFightRenderer.NONE_DAMAGED;
						}
						break;
					case StickFightRenderer.ATTACK_HIGH:
						result = StickFightRenderer.BOTH_DAMAGED;
						break;
					case StickFightRenderer.ATTACK_LOW:
						result = StickFightRenderer.BOTH_DAMAGED;
						break;
				}
				break;
		}
		return result;
	}
	
	public int[] processTurns(int[] playerMoveChoice, int[] opponentMoveChoice) {
		int[] result = new int[playerMoveChoice.length];
		for (int i=0; i<result.length; i++) {
			result[i] = processTurn(playerMoveChoice[i], opponentMoveChoice[i]);
		}
		return result;
	}
	
}
