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


/**
 * 2 player game with 3 rounds where tiles are closed based on a number you roll with a dice
 * Shut the box game 
 * @author L. Mailloux
 * @version 1.0
 */
public class GUIDriver extends Application {
	
    int NUM_TILES = 9;
    ArrayList<Tile> tiles = new ArrayList<>();
    ArrayList<Integer> selectedTiles = new ArrayList<>();
    ArrayList<Button> tileButtons = new ArrayList<>();
    Stage primaryStage;
    int player = 1;
    int player1 = 0;
    int player2 = 0;
    int round = 1;
    Button roll;
    Label rollText = new Label("Click roll to start player 1's turn");
    Label scoring = new Label("Player 1: 0 | Player 2: 0");
    Label roundText = new Label("Round 1");
    Button lockIn = new Button("Lock in");

    Die die1 = new Die();
    Die die2 = new Die();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        for (int i = 0; i < NUM_TILES; i++) {
            tiles.add(new Tile(i + 1));
        }

        Font bigFont = Font.font("Times New Roman", FontWeight.BOLD, 24);
        Label title = new Label("Shut the Box");
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
                    if (!selectedTiles.contains(index + 1)) {
                        selectedTiles.add(index + 1);
                        tileButton.setStyle("-fx-background-color: lightblue;");
                    } else {
                        selectedTiles.remove(Integer.valueOf(index + 1));
                        tileButton.setStyle("");
                    }
                    lockIn.setDisable(selectedTiles.isEmpty());
                }
            });

            tileButtons.add(tileButton);
            hBox.getChildren().add(tileButton);
        }

        roll = new Button("Roll");
        roll.setOnAction(e -> {
            boolean threeTiles = false;
            for (int i = 6; i < 9; i++) {
                if (!tiles.get(i).isDown()) {
                    threeTiles = true;
                    break;
                }
            }

            int roll1 = die1.roll();
            int roll2 = die2.roll();
            int totalRoll = roll1;

            if (threeTiles) {
                totalRoll = roll1 + roll2;
                rollText.setText("You rolled a " + roll1 + " and a " + roll2 + " for a total of " + totalRoll);
            } else {
                rollText.setText("You rolled a " + roll1);
            }

            for (int i = 0; i < NUM_TILES; i++) {
                if (!tiles.get(i).isDown()) {
                    tileButtons.get(i).setDisable(false);
                    tileButtons.get(i).setStyle("");
                }
            }

            roll.setDisable(true);
        });

        //lock in
        lockIn.setOnAction(e -> {
            lockIn.setDisable(false);
            int totalRoll = die1.getValue();
            boolean threeTiles = false;

            for (int i = 6; i < 9; i++) {
                if (!tiles.get(i).isDown()) {
                    threeTiles = true;
                    break;
                }
            }

            if (threeTiles) {
                totalRoll += die2.getValue();
            }

            int sum = 0;
            boolean valid = true;
            for (int tileNum : selectedTiles) {
                if (tileNum < 1 || tileNum > 9 || tiles.get(tileNum - 1).isDown()) {
                    valid = false;
                    break;
                }
                sum += tileNum;
            }

            if (valid && sum == totalRoll) {
                for (int tileNum : selectedTiles) {
                    tiles.get(tileNum - 1).shut();
                    tileButtons.get(tileNum - 1).setStyle("-fx-background-color: gray;");
                    tileButtons.get(tileNum - 1).setDisable(true);
                }
                selectedTiles.clear();
                roll.setDisable(false);
                rollText.setText("Tiles shut");
                boolean allDown = true;
                for (Tile tile : tiles) {
                    if (!tile.isDown()) {
                        allDown = false;
                        break;
                    }
                }
                if (allDown) {
                    rollText.setText("You shut all tiles!");
                    lockIn.setDisable(true);
                    int score = 0;
                    for (Tile tile : tiles) {
                        if (!tile.isDown()) {
                            score += tile.getNumber();
                        }
                    }
                    if (player == 1) {
                        player1 += score;
                    } else {
                        player2 += score;

                    }
                    scoring.setText("Player 1: " + player1 + " | Player 2: " + player2);
                    round++;
                   roundText.setText("Round: " + round);
                }
                
                
            } else {
                rollText.setText("Invalid combo. Try again. You rolled a total of " + totalRoll);
            }

            selectedTiles.clear();
        });
        //lock in 
        

        Button endTurn = new Button("End turn");
        endTurn.setOnAction(e -> { // end turn 
        	endTurn();
        });// end turn end

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        lockIn.setDisable(true);
        vBox.getChildren().addAll(title, hBox, roll, rollText, lockIn, endTurn, scoring, roundText);
        vBox.setStyle("-fx-background-color: azure;");

        Scene scene = new Scene(vBox, 600, 400);
        primaryStage = stage;
        primaryStage.setScene(scene);
        stage.setTitle("Shut the Box");
        stage.setScene(scene);
        stage.show();
    }
    
    
    /**
     * Ends the players turn when they click the endturn button
     * Advances round and adds up score
     */
    public void endTurn() {
        lockIn.setDisable(true);
        int score = 0;
        for (Tile tile : tiles) {
            if (!tile.isDown()) {
                score += tile.getNumber();
            }
        }

        if (player == 1) {
            player1 += score;
        } else {
            player2 += score;
        }

        scoring.setText("Player 1: " + player1 + " | Player 2: " + player2);

        if (round >= 3 && player == 2) {
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
            resultLabel.setTextFill(Color.DEEPSKYBLUE);

            VBox layout = new VBox(20, resultLabel);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-background-color: azure;");

            Scene winnerScene = new Scene(layout, 600, 400);
            primaryStage.setScene(winnerScene);
            return;
        }

        if (player == 1) {
            player = 2;
        } else {
            player = 1;
            round++;
        }

        roundText.setText("Round: " + round);

        for (int i = 0; i < NUM_TILES; i++) {
            Button tileButton = tileButtons.get(i);
            tileButton.setDisable(true);
            tileButton.setStyle("");
        }

        for (int i = 0; i < NUM_TILES; i++) {
            tiles.get(i).open();
        }

        for (int tileNum : selectedTiles) {
            tileButtons.get(tileNum - 1).setStyle("-fx-background-color: lightgray;");
        }
        selectedTiles.clear();

        rollText.setText("Score: " + score + ". Click roll to start player " + player + "'s turn.");
        roll.setDisable(false);	
    }
}
