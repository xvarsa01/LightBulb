package UI;

import enums.Difficulty;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Game;
import logic.Randomizer;
import logic.Score;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Navigation {

    public static void showHomePage(Stage stage) {
        Label title = new Label("💡 LightBulb Game 💡");
        title.setStyle("-fx-font-size: 36px; -fx-text-fill: white; -fx-padding: 20px;");
        title.setEffect(new DropShadow(5, Color.WHITE));

        Label scoreLabel = new Label("Easy: " + Score.easy() +
                " | Medium: " + Score.medium() +
                " | Hard: " + Score.hard());
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        Button resetButton = createStyledButton("Reset scores", "orange");
        resetButton.setOnAction(e -> {
            // reset properties and save
            Score.reset();
            // update label
            scoreLabel.setText("Easy: " + Score.easy() +
                    " | Medium: " + Score.medium() +
                    " | Hard: " + Score.hard());
        });

        ToggleGroup difficultyGroup = new ToggleGroup();
        RadioButton easy = createRadioButton("Easy", difficultyGroup);
        RadioButton medium = createRadioButton("Medium", difficultyGroup);
        RadioButton hard = createRadioButton("Hard", difficultyGroup);
        easy.setSelected(true);

        Button startButton = createStyledButton("Start Game");
        Button exitButton = createStyledButton("Exit", "red");

        VBox vbox = new VBox(15, title, scoreLabel, resetButton, easy, medium, hard, startButton, exitButton);
        vbox.setPadding(new Insets(30));
        vbox.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        File file = new File("lib/home-background.jpg");
        String localUrl = file.toURI().toString();
        root.setStyle("-fx-background-image: url('" + localUrl + "'); " +
                "-fx-background-size: cover; " +
                "-fx-background-position: center center;");

        Scene scene = new Scene(root);
        stage.setTitle("LightBulb Game");
        stage.setScene(scene);
        stage.show();

        startButton.setOnAction(e -> {
            Difficulty selected = easy.isSelected() ? Difficulty.easy :
                    medium.isSelected() ? Difficulty.medium :
                            Difficulty.hard;
            showGamePage(stage, selected);
        });

        exitButton.setOnAction(e -> Platform.exit());
    }

    private static RadioButton createRadioButton(String text, ToggleGroup group) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(group);
        rb.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        return rb;
    }

    private static Button createStyledButton(String text) {
        return createStyledButton(text, "green")   ;
    }

    private static Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 18px; -fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-padding: 10 20 10 20; -fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 18px; -fx-background-color: " + color + "; -fx-text-fill: black; " +
                "-fx-padding: 10 20 10 20; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 18px; -fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-padding: 10 20 10 20; -fx-background-radius: 10;"));
        return button;
    }

    public static void showGamePage(Stage stage, Difficulty difficulty) {
        GamePage gamePage = new GamePage(stage, difficulty);
        Game game = gamePage.show();

        gamePage.setBoardInteractionDisabled(true);

        new Thread(() -> {
            gamePage.setBoardInteractionDisabled(false);
            WaitAndTurnNodes(game, difficulty);
            Platform.runLater(gamePage::startTimer);
        }).start();
    }

    private static void WaitAndTurnNodes(Game game, Difficulty difficulty) {
        System.out.println("3 !");
        sleep(1000);
        System.out.println("2 !");
        sleep(1000);
        System.out.println("1 !");
        sleep(1000);

        int affectedNodesPercentage, rotatedNodesAtSameTime;

        switch (difficulty) {
            case medium -> {
                affectedNodesPercentage = 90;
                rotatedNodesAtSameTime = 2;
            }
            case hard -> {
                affectedNodesPercentage = 100;
                rotatedNodesAtSameTime = 3;
            }
            default -> {
                affectedNodesPercentage = 80;
                rotatedNodesAtSameTime = 1;
            }
        }

        //todo add some time to rotate
        Randomizer randomizer = new Randomizer(game);
        randomizer.randomlyTurnSomeNodes(affectedNodesPercentage / 100f, 0, rotatedNodesAtSameTime);
        System.out.println("Go !");
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
