package com.jpapps.sticks;

import com.jpapps.pandroidGL.*;

public class StickMan extends AnimatedObject{
	
	private int health;

	public StickMan(NumberMill framesMill) {
		super(framesMill);
		this.health = 6;
	}
	public StickMan(NumberMill framesMill, NumberMill pathX, NumberMill pathY) {
		super(framesMill, pathX, pathY);
		this.health = 6;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	
	
}
