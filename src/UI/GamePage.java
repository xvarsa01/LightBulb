package UI;

import common.Observable;
import enums.Difficulty;
import logic.Game;
import logic.GameNode;
import logic.Position;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GamePage {

    private final Stage stage;
    private final Difficulty difficulty;
    private NodeView[][] nodeViews;

    public GamePage(Stage stage, Difficulty difficulty) {
        this.stage = stage;
        this.difficulty = difficulty;
    }

    public Game show() {
        int rows;
        switch (difficulty) {
            case medium -> {
                rows = 6;
            }
            case hard -> {
                rows = 7;
            }
            default -> {
                rows = 5;
            }
        }
        nodeViews = new NodeView[rows][rows];

        Game game = new Game(rows, rows);
        game.addObserver(new Observable.Observer() {
            @Override
            public void update(Observable o) {
                // This will only be called after relightBoard() finishes
                if (nodeViews[0][0] == null){
                    return;
                }

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < rows; c++) {
                            nodeViews[r][c].update(o);
                    }
                }
            }
        });
        game.SeedBoard(difficulty, 1, 1, 1);
        game.init();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= rows; col++) {
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);

                NodeView nodeView = new NodeView(node);
                nodeViews[row-1][col-1] = nodeView;

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
        return game;
    }
}
