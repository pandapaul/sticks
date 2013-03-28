package com.jpapps.sticks;

public class AnimatedObject {
	private NumberMill framesMill;
	private NumberMill pathX;
	private NumberMill pathY;
	private int currentFrame;
	private int[] currentPathPosition;
	private int[] defaultPath = {0};
	private NumberMill noPath = new NumberMill(defaultPath,NumberMill.ONCE);
	
	public AnimatedObject(NumberMill framesMill) {
		this(framesMill, null, null);
	}
	
	public AnimatedObject(NumberMill framesMill, NumberMill pathX, NumberMill pathY) {
		this.setFramesMill(framesMill);
		this.setPath(pathX, pathY);
		this.currentFrame = framesMill.getCurrent();
		this.currentPathPosition = new int[2];
		this.currentPathPosition[0] = this.pathX.getCurrent();
		this.currentPathPosition[1] = this.pathY.getCurrent();
	}

	public NumberMill getFramesMill() {
		return framesMill;
	}

	public void setFramesMill(NumberMill frames) {
		this.framesMill = frames;
	}

	public NumberMill getPathX() {
		return pathX;
	}
	
	public NumberMill getPathY() {
		return pathY;
	}

	public void setPath(NumberMill pathX, NumberMill pathY) {
		this.setPathX(pathX);
		this.setPathY(pathY);
	}
	
	public void setPathX(NumberMill pathX) {
		if(pathX==null)
			pathX = noPath;
		this.pathX = pathX;
	}
	
	public void setPathY(NumberMill pathY) {
		if(pathY==null)
			pathY = noPath;
		this.pathY = pathY;
	}
	
	public void advance() {
		currentFrame = framesMill.advance();
		currentPathPosition[0] = pathX.advance();
		currentPathPosition[1] = pathY.advance();
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public int[] getCurrentPathPosition() {
		return currentPathPosition;
	}
	
	public void noPath() {
		pathX = noPath;
		pathY = noPath;
	}

}
