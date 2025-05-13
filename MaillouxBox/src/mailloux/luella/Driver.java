package mailloux.luella;
import java.util.ArrayList;
import java.util.Scanner;
public class Driver {
   public static void main(String[] args) {
       int NUM_TILES = 9;
       int player1 = 0;
       int player2 = 0;

       for (int round = 1; round <= 3; round++) {
           System.out.println("Round " + round);
           
           for (int player = 1; player <= 2; player++) {
               System.out.println("Player " + player + "'s turn.");
               
                   ArrayList<Tile> tiles = new ArrayList<>();
                   
                   for (int i = 0; i < NUM_TILES; i++) {
                       tiles.add(new Tile(i + 1));
                   }
                   Scanner choice = new Scanner(System.in);
                   Die die1 = new Die();
                   Die die2 = new Die();
                   boolean done = false;
                   while (true) {

                       for (Tile t : tiles) {
                           System.out.println(t);
                       }

                       done = true;
                       for (Tile t : tiles) {
                           if (!t.isDown()) {
                               done = false;
                               break;
                           }
                       }
                       if (done) {             
                    	   System.out.println("All tiles are down.");
                    	   break;
                       }
                       
                       boolean threeTiles = false;
                       for (int i = 6; i < 9; i++) {
                           if (!tiles.get(i).isDown()) {
                               threeTiles = true;
                               break;
                           }
                       }
                       // Roll the dice
                       int roll1 = die1.roll();
                       int roll2 = die2.roll();
                       int totalRoll;
                       if (threeTiles) {
                           totalRoll = roll1 + roll2;
                           System.out.println("You rolled a " + roll1 + " and a " + roll2 + " for a total of " + totalRoll);
                       } else {
                           totalRoll = roll1;
                           System.out.println("You rolled a " + roll1);
                       }
  
                       while (true) {
                           System.out.print("What tiles would you like to shut? Separate your answers with spaces: ");
                           String input = choice.nextLine().trim();
                           if (input.equals("-1")) {
                              
                               int score = 0;
                               for (Tile t : tiles) {
                                   if (!t.isDown()) {
                                       score += t.getNumber();
                                   }
                               }
                               System.out.println("Player " + player + "'s score: " + score);
                               if (player ==1) {
                            	   player1 += score;
                               }
                               else {
                            	   player2 += score;
                               }
                               done = true;
                               break; 
                           }

                           String[] numbers = input.split(" ");
                           ArrayList<Integer> selected = new ArrayList<>();
                           int sum = 0;
                           boolean isValid = true;
                           for (String number : numbers) {
                               int tileNum = 0;
                               boolean valid = true;
                               try {
                                   tileNum = Integer.parseInt(number);
                               } catch (NumberFormatException e) {
                                   System.out.println("Invalid input: " + number);
                                   isValid = false;
                                   valid = false;
                               }
                               if (valid && (tileNum < 1 || tileNum > 9)) {
                                   System.out.println("Invalid tile number: " + tileNum);
                                   isValid = false;
                                   valid = false;
                               }
                               if (valid) {
                                   Tile tile = tiles.get(tileNum - 1);
                                   if (tile.isDown()) {
                                       System.out.println("Tile " + tileNum + " is already down");
                                       isValid = false;
                                   } else {
                                       selected.add(tileNum);
                                       sum += tileNum;
                                   }
                               }
                           }
                       
                           if (isValid && sum == totalRoll) {
                               for (int selectedTile : selected) {
                                   tiles.get(selectedTile - 1).shut();
                               }
                               System.out.println("Tiles have been shut");
                               break; 
                           } else {
                               System.out.println("Invalid number combination. Try again.");
                           } // invalid combo
                           
                       } // shutting tiles 

                       if (done) break;
                   } // while true 
               
           } //players
           
       } // rounds
       System.out.println(player1);
       System.out.println(player2);
       if (player1 > player2) {
    	   System.out.println("Player 2 wins!");
       }
       else {
    	   System.out.println("Player 1 wins!");
       } // winning player
        
   } // main
   
} // driver
