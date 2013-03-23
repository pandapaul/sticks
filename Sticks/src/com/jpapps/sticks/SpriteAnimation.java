package com.jpapps.sticks;

public class SpriteAnimation {
	
	protected int[] spriteIDList;
	protected int currentSpriteIndex;
	protected int type;
	protected boolean reverse = false;
	public final static int CYCLE = 0;
	public final static int LOOP = 1;
	public final static int ONCE = 2;
	
	public SpriteAnimation(int[] ids, int t) {
		spriteIDList = ids;
		currentSpriteIndex = -1;
		type = t;
	}
	
	public int advance() {
		switch(type) {
		case CYCLE:
			if(reverse) {
				if(currentSpriteIndex <= 0) {
					currentSpriteIndex = 0;
					reverse = false;
				}
				else {
					currentSpriteIndex--;
				}
			}
			else {
				if(currentSpriteIndex >= spriteIDList.length-1) {
					currentSpriteIndex--;
					reverse = true;
				}
				else {
					currentSpriteIndex++;
				}
			}
			break;
		case LOOP:
			if(currentSpriteIndex >= spriteIDList.length-1) {
				currentSpriteIndex=0;
			}
			else {
				currentSpriteIndex++;
			};
			break;
		case ONCE:
			if(currentSpriteIndex < spriteIDList.length-1)
				currentSpriteIndex++;
			break;
		default:
			break;
		}
		return spriteIDList[currentSpriteIndex];
	}
}
