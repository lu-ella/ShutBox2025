package mailloux.luella;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GUIDriver extends Application {

    private static final int NUM_TILES = 9;
    ArrayList<Tile> tiles = new ArrayList<>();
    ArrayList<Integer> selectedTiles = new ArrayList<>();
    ArrayList<Button> tileButtons = new ArrayList<>();
    private Stage primaryStage;
    private int player = 1;
    private int player1 = 0;
    private int player2 = 0;
    private int round = 1;
    private Button roll;
    private Label rollText = new Label("Click roll to start player 1's turn");
    private Label scoring = new Label("Player 1: 0 | Player 2: 0");
    private Label roundText = new Label("Round 1");
    private Button lockIn = new Button("Lock in");

    Die die1 = new Die();
    Die die2 = new Die();
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
    	

        for (int i = 0; i < NUM_TILES; i++) {
            tiles.add(new Tile(i + 1));
        }

        Font bigFont = Font.font("Times New Roman", FontWeight.BOLD, 24);
		//vBox.setStyle("-fx-background-color: lightblue;");
        Label title = new Label("Shut the box");
        title.setFont(bigFont);
        title.setTextFill(Color.DEEPSKYBLUE);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < NUM_TILES; i++) {
            Button tileButton = new Button(String.valueOf(i + 1));
            tileButton.setDisable(true);
            tileButton.setStyle("-fx-background-color: lightblue;");


            int index = i;
            tileButton.setOnAction(e -> {
            	if (!tiles.get(index).isDown()) {
            		if(!selectedTiles.contains(index +1)) {
            			selectedTiles.add(index +1);
            			tileButton.setStyle("-fx-background-color: lightblue;");
            		}
            		else {
            			selectedTiles.remove(Integer.valueOf(index +1));
            			tileButton.setStyle("");
            		}
            		lockIn.setDisable(selectedTiles.isEmpty());
            	}

            });

            tileButtons.add(tileButton);
            hBox.getChildren().add(tileButton);
        }


        roll = new Button("Roll");
        roll.setOnAction(e -> rollDice());

        lockIn.setOnAction(e -> lockIn());
        
        Button endTurn = new Button ("End turn");
        endTurn.setOnAction(e -> endTurn());
 

        // Add rollText to the VBox
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        lockIn.setDisable(true);
        vBox.getChildren().addAll(title, hBox, roll,rollText, lockIn, endTurn, scoring, roundText); 

        Scene scene = new Scene(vBox, 600, 400);
        primaryStage = stage;
        primaryStage.setScene(scene);
        stage.setTitle("Shut the Box");
        stage.setScene(scene);
        stage.show();
    }

    private void rollDice() {
        boolean threeTiles = false;
        for (int i = 6; i < 9; i++) {
            if (!tiles.get(i).isDown()) {
                threeTiles = true;
                break;
            } // !tiles down
        } // for
        int roll1 = die1.roll();
        int roll2 = die2.roll();
        int totalRoll;
        if (threeTiles) {
            totalRoll = roll1 + roll2;
            rollText.setText("You rolled a " + roll1 + " and a " + roll2 + " for a total of " + totalRoll);
        } else {
            totalRoll = roll1;
            rollText.setText("You rolled a " + roll1);
        }
        for (int i =0; i < NUM_TILES; i++) {
        	if (!tiles.get(i).isDown()) {
        		tileButtons.get(i).setDisable(false);
                tileButtons.get(i).setStyle(""); 
        	}
        }
        
        roll.setDisable(true);

    } // rollDice
    
    private void lockIn() {
    	lockIn.setDisable(false);
    	int totalRoll = die1.getValue();
    	boolean threeTiles = false;
    	
    	for (int i=6; i < 9; i++) {
    		if (!tiles.get(i).isDown()) {
    			threeTiles = true;
    			break;
    		}
    	}
    	
    	if (threeTiles) {
    		totalRoll += die2.getValue();
    	}
    	int sum =0;
    	boolean isValid = true;
    	
    	for (int tileNum : selectedTiles) {
    		if (tileNum < 1 || tileNum > 9) {
    			rollText.setText("Invalid number:" + tileNum);
    			isValid = false;
    			break;
    		}
    		Tile tile = tiles.get(tileNum -1);
    		if (tile.isDown()) {
    			rollText.setText(tileNum + " is closed");
    			isValid = false;
    			break;
    		}
    		sum += tileNum;
    	}
    	if (isValid && sum == totalRoll) {
    		for (int tileNum : selectedTiles) {
    			tiles.get(tileNum - 1).shut();
    			Button tileButton = tileButtons.get(tileNum - 1);
    			tileButton.setStyle("-fx-background-color: gray;"); 
    			tileButton.setDisable(true);
    		}
    		selectedTiles.clear();
    		rollText.setText("Tiles have been shut");
        	roll.setDisable(false);

    	}
    	else {
    		rollText.setText("Invalid combo. try again. You rolled a total of " + totalRoll);
    		for (int tileNum : selectedTiles) {
    			Button tileButton = tileButtons.get(tileNum- 1);
    			if (!tiles.get(tileNum -1).isDown()) {
    				tileButton.setStyle(" ");
    			}
    		}
    		
    	}
    	selectedTiles.clear();

    	
    } // lock in 
    
    private void endTurn() {
    	lockIn.setDisable(true);
    	int score = 0;
    	for (Tile tile : tiles) {
    		if (!tile.isDown()) {
    			score += tile.getNumber();
    		}
    	}
    	
    	if (player == 1) {
    		player1 += score;
    	}
    	else {
    		player2 += score;
    	}
    	
    	scoring.setText("Player 1: " + player1 + " | Player 2: " + player2);
    	
    	if (round >= 3 && player ==2) {
    		winner();
    		roll.setDisable(true);
    		lockIn.setDisable(true);
    		return;
    	}
    	
    	if (player == 1) {
    		player = 2;
    	}
    	else {
    		player =1;
    		round ++;
    	}
    	roundText.setText("Round: " + round);
    	
    	  for (int i = 0; i < NUM_TILES; i++) {
   	       Button tileButton = tileButtons.get(i);
   	       tileButton.setDisable(true);
   	       tileButton.setStyle("");  
    	  }
    	  for (int i = 0; i < NUM_TILES; i++) {
    		    tiles.get(i).open(); // Custom method to open the tile
    		}

    	  for (int tileNum : selectedTiles) {
    		tileButtons.get(tileNum - 1).setStyle("-fx-background-color: lightgray;");
    	}
    	selectedTiles.clear();
    	
    	rollText.setText("Score: " + score + ". Click roll to start player " + player +  "'s turn." );
    	roll.setDisable(false);
    		
    }
    
    private void reset() {
        for (Button tileButton : tileButtons) {
            tileButton.setDisable(true); 
            tileButton.setStyle("");      
        }
        selectedTiles.clear();
        rollText.setText("Roll dice");
    }

    
    private void winner() {
        String result;
        if (player1 < player2) {
            result = "Player 1 wins!";
        } else if (player1 > player2) {
            result = "Player 2 wins!";
        } else {
            result = "It's a tie!";
        }

        Label resultLabel = new Label(result);
        resultLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));

        VBox layout = new VBox(20, resultLabel);
        layout.setAlignment(Pos.CENTER);

        Scene winnerScene = new Scene(layout, 600, 400);
        primaryStage.setScene(winnerScene);
    }
   
}