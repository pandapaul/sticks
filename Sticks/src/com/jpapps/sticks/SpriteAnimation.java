package com.jpapps.sticks;

public class SpriteAnimation {
	protected int[] spriteIDList;
	protected int currentSpriteID;
	
	public SpriteAnimation(int[] ids) {
		spriteIDList = ids;
		currentSpriteID = ids[0];
	}
	
	public int advance() {
		int id = currentSpriteID + 1;
		if(id >= spriteIDList.length)
			id = 0;
		currentSpriteID = id;
		return currentSpriteID;
	}
}
