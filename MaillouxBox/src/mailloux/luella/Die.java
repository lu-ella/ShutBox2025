package mailloux.luella;

import java.util.Random;

/**
 * Class to represent a single Die
 * 
 * @author L. Mailloux
 */

public class Die {

	Random rand = new Random(); // class wide variable
	private int value;
	private int numSides;
	

	/**
	 * Constructor of a die default to 6 sides
	 */
	public Die() {
		numSides = 6;
		value = roll();
	}

	/**
	 * constructor for die with known sides
	 * 
	 * @param n number of roles
	 */

	public Die(int n) {
		numSides = n;
		value = roll();
	}

	/**
	 * randomly assigns a new value to the die
	 * 
	 * @return the value on top of the die
	 */
	public int roll() {
		// Random rand = new Random();
		value = rand.nextInt(numSides) + 1;
		return value;

		/**
		 * randomly assigns a new value to the die
		 * 
		 * @return value on top of die
		 */
	}

	public int getValue() {
		return value;

	}

	public void setSides(int n) {
		numSides = n;

	}

	/**
	 * Create a string representation of the object die
	 */
	@Override
	public String toString() {
		return "Die has " + numSides + " sides and the current value is " + value;
	}

}
