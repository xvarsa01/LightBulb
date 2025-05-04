package UI;

import enums.Difficulty;
import logic.Game;
import logic.GameNode;
import logic.Position;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GamePage {

    private final Stage stage;
    private final Difficulty difficulty;

    public GamePage(Stage stage, Difficulty difficulty) {
        this.stage = stage;
        this.difficulty = difficulty;
    }

    public void show() {
        int rows, affectedNodesPercentage, rotatedNodesAtSameTime;

        switch (difficulty) {
            case medium -> {
                rows = 6;
                affectedNodesPercentage = 75;
                rotatedNodesAtSameTime = 2;
            }
            case hard -> {
                rows = 7;
                affectedNodesPercentage = 100;
                rotatedNodesAtSameTime = 3;
            }
            default -> {
                rows = 5;
                affectedNodesPercentage = 50;
                rotatedNodesAtSameTime = 1;
            }
        }

        Game game = new Game(rows, rows);
        game.SeedBoard(difficulty, 1, 1, 1);
        game.init();
        game.randomlyTurnSomeNodes(affectedNodesPercentage / 100f, 0, rotatedNodesAtSameTime);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= rows; col++) {
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);

                NodeView nodeView = new NodeView(node, () -> {
                    game.update(null);
                    if (game.GameFinished()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "🎉 All bulbs are lit! You win!");
                        alert.showAndWait();
                    }
                });

                gridPane.add(nodeView, col - 1, row - 1);
            }
        }

        Button backButton = new Button("🏠 Back to Home");
        backButton.setOnAction(e -> Navigation.showHomePage(stage));

        VBox vbox = new VBox(10, gridPane, backButton);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        root.setTop(new Label("🎮 Game Page"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
