package UI;

import enums.Difficulty;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Game;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Navigation {

    public static void showHomePage(Stage stage) {
        Label title = new Label("💡 LightBulb Game 💡");
        title.setStyle("-fx-font-size: 24px; -fx-padding: 10px;");

        ToggleGroup difficultyGroup = new ToggleGroup();
        RadioButton easy = new RadioButton("Easy");
        easy.setToggleGroup(difficultyGroup);
        easy.setSelected(true);

        RadioButton medium = new RadioButton("Medium");
        medium.setToggleGroup(difficultyGroup);

        RadioButton hard = new RadioButton("Hard");
        hard.setToggleGroup(difficultyGroup);

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit");

        VBox vbox = new VBox(10, title, easy, medium, hard, startButton, exitButton);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        root.setTop(new Label("🏠 Home"));

        Scene scene = new Scene(root, 400, 400);
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

    public static void showGamePage(Stage stage, Difficulty difficulty) {
        GamePage gamePage = new GamePage(stage, difficulty);
        Game game = gamePage.show();

        gamePage.setBoardInteractionDisabled(true);

        new Thread(() -> {
            WaitAndTurnNodes(game, difficulty);
            Platform.runLater(() -> {
                gamePage.setBoardInteractionDisabled(false);
                gamePage.startTimer();
            });
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

        game.randomlyTurnSomeNodes(affectedNodesPercentage / 100f, 500, rotatedNodesAtSameTime);
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
