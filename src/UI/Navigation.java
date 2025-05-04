package UI;

import enums.Difficulty;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
            Navigation.showGamePage(stage, selected);
        });

        exitButton.setOnAction(e -> Platform.exit());
    }

    public static void showGamePage(Stage stage, Difficulty difficulty) {
        GamePage gamePage = new GamePage(stage, difficulty);
        gamePage.show();
    }
}
