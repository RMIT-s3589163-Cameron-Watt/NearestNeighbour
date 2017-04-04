package nearestNeigh;

public enum Direction {
	LEFT, 
	RIGHT;
	
	public Direction getOtherDirection() {
		if (this == LEFT)
			return RIGHT;
		else return LEFT;
	}
}
