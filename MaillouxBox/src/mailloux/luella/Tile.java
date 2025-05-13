package mailloux.luella;

public class Tile {
	private int value;
	private boolean isDown;
	
	public Tile(int v) {
		value = v;
		isDown = false;
		
	}
	
	public void shut () {
		isDown = true;
	}
	
	public int getNumber() {
		return value;
	}
	
	
	public boolean isDown() {
		return isDown;	
	}
	
	public void reset() {
		isDown = false;
	}
	
	public void open() {
		isDown = false;
	}
	
	
	@Override
	public String toString() {
		String state = "";
		if (isDown) {
			state = "D";
		}
		else {
			state = "U";
		}
		return String.format("%d: %s", value,state);
	}


}
