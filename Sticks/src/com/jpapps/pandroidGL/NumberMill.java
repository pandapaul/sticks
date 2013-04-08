package com.jpapps.pandroidGL;

public class NumberMill {
	
	protected int[] numbers;
	protected int currentNumberIndex;
	protected int behavior;
	protected boolean reverse = false;
	protected boolean finished;
	
	/**
	 * When set as a NumberMill's behavior, numbers will advance forwards then backwards.<br>
	 * e.g. For numbers {1,2,3,4}, we'd get 1,2,3,4,3,2,1,2,... 
	 */
	public final static int CYCLE = 0;
	
	/**
	 * When set as a NumberMill's behavior, numbers will advance forwards then restart.<br>
	 * e.g. For numbers {1,2,3,4}, we'd get 1,2,3,4,1,2,3,4,1,... 
	 */
	public final static int LOOP = 1;
	
	/**
	 * When set as a NumberMill's behavior, numbers will advance forwards then revert to the first value.<br>
	 * e.g. For numbers {1,2,3,4}, we'd get 1,2,3,4,1,1,1,...
	 */
	public final static int ONCE = 2;
	
	/**
	 * When set as a NumberMill's behavior, numbers will advance forwards then stop and persist.<br>
	 * e.g. For numbers {1,2,3,4}, we'd get 1,2,3,4,4,4,4,...
	 */
	public final static int ONCE_PERSIST = 3;
	
	public NumberMill(int[] ids, int b) {
		numbers = ids;
		currentNumberIndex = 0;
		behavior = b;
		finished = false;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void restart() {
		currentNumberIndex = 0;
		finished = false;
	}
	
	public void advance() {
		if(currentNumberIndex == -1) {
			currentNumberIndex = 0;
		}
		else {
			switch(behavior) {
			case CYCLE:
				if(reverse) {
					if(currentNumberIndex <= 0) {
						currentNumberIndex = 0;
						reverse = false;
					}
					else {
						currentNumberIndex--;
					}
				}
				else {
					if(currentNumberIndex >= numbers.length-1) {
						currentNumberIndex--;
						reverse = true;
					}
					else {
						currentNumberIndex++;
					}
				}
				break;
			case LOOP:
				if(currentNumberIndex >= numbers.length-1) {
					currentNumberIndex=0;
				}
				else {
					currentNumberIndex++;
				};
				break;
			case ONCE_PERSIST:
				if(!finished)
					currentNumberIndex++;
				if(currentNumberIndex==numbers.length-1)
					finished = true;
				break;
			case ONCE:
				if(currentNumberIndex == numbers.length-1) {
					currentNumberIndex = 0;
					finished = true;
				}
				if(!finished)
					currentNumberIndex++;
				break;
			default:
				break;
			}
		}
	}
	
	public int getCurrent() {
		return numbers[currentNumberIndex];
	}
	
	public int getBehavior() {
		return behavior;
	}
}
