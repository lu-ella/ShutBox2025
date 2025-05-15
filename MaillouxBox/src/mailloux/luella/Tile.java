package mailloux.luella;




/** 
 * Gives each tile a number, and controls if they are up or down 
 * Tile can be shut, opened, or reset 
 * @author L . Mailloux
 * @version 1.0
 */

public class Tile {
	private int value;
	private boolean isDown;
	
	
	/**
	 * creates a file that is up by default 
	 * @param v number value of tile 
 	 */
	
	public Tile(int v) {
		value = v;
		isDown = false;
		
	}
	
	
	/**
	 * shuts the tile (marks as down)
	 */
	
	public void shut () {
		isDown = true;
	}
	
	
	/**
	 * returns the number of the tile 
	 * @return the tiles 
	 */
	
	public int getNumber() {
		return value;
	}
	
	
	/** 
	 * checks weather the tile is down 
	 * @return if the tile is up then false, if down then true  
	 */
	
	public boolean isDown() {
		return isDown;	
	}
	
	
	/** 
	 * resets the tiles by marking them up 
	 */
	
	public void reset() {
		isDown = false;
	}
	
	
	/**
	 * opens the tiles 
	 */
	
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
