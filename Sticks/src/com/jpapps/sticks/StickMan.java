package com.jpapps.sticks;

import com.jpapps.pandroidGL.*;

public class StickMan extends AnimatedObject{
	
	private int health;
	private boolean bleeding;

	public StickMan(NumberMill framesMill, int width, int height) {
		this(framesMill, width, height, 0,0);
	}
	
	public StickMan(NumberMill framesMill, int width, int height, int x, int y) {
		this(framesMill, width, height, x, y, null, null);
	}
	
	public StickMan(NumberMill framesMill, int width, int height, int x, int y, NumberMill pathX, NumberMill pathY) {
		super(framesMill, width, height, x, y, pathX, pathY);
		this.setHealth(6);
		this.setBleeding(false);
	}
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public boolean isBleeding() {
		return bleeding;
	}
	public void setBleeding(boolean bleeding) {
		this.bleeding = bleeding;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.setBleeding(false);
	}
	
}
