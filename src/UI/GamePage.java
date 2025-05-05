package UI;

import common.Observable;
import enums.Difficulty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import logic.Game;
import logic.GameNode;
import logic.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePage {

    private final Stage stage;
    private final Difficulty difficulty;
    private NodeView[][] nodeViews;
    private Label timerLabel;
    private Timeline timeline;
    private int elapsedSeconds;
    private Label turnLabel;
    private int turnCount;
    private Game game;

    public GamePage(Stage stage, Difficulty difficulty) {
        this.stage = stage;
        this.difficulty = difficulty;
    }

    public Game show() {
        int rows = switch (difficulty) {
            case medium -> 6;
            case hard -> 7;
            default -> 5;
        };
        nodeViews = new NodeView[rows][rows];

        game = new Game(rows, rows);
        game.addObserver(o -> {
            if (nodeViews[0][0] == null) return;
            if(game.GameFinished()){
                Platform.runLater(this::stopTimer);
                System.out.println("You won");
                showWinPopup();
                //todo popup
            }

            boolean allLit = true;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < rows; c++) {
                    nodeViews[r][c].update(o);
                }
            }
            if (game.GameFinished()) {
                Platform.runLater(this::stopTimer);
            }
        });

        game.SeedBoard(difficulty, 1, 1, 1);
        game.init();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
//        gridPane.setHgap(5);
//        gridPane.setVgap(5);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= rows; col++) {
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);

                NodeView nodeView = new NodeView(node);
                nodeView.setOnMouseClicked(e -> {
                    if (!nodeView.isInteractionDisabled()) {
                        node.turn();
                        incrementTurnCount();
                    }
                });

                nodeViews[row - 1][col - 1] = nodeView;

                gridPane.add(nodeView, col - 1, row - 1);
            }
        }

        timerLabel = new Label("⏱ Time: 0s");
        turnLabel = new Label("🔁 Turns: 0");


        Button backButton = new Button("🏠 Back to Home");
        backButton.setOnAction(e -> {
            stopTimer();
            Navigation.showHomePage(stage);
        });

        VBox vbox = new VBox(10, timerLabel, turnLabel, gridPane, backButton);
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

    public void setBoardInteractionDisabled(boolean disabled) {
        for (NodeView[] row : nodeViews) {
            for (NodeView nv : row) {
                nv.setInteractionDisabled(disabled);
            }
        }
    }

    public void startTimer() {
        elapsedSeconds = 0;
        timerLabel.setText("⏱ Time: 0s");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++;
            timerLabel.setText("⏱ Time: " + elapsedSeconds + "s");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void incrementTurnCount() {
        turnCount++;
        turnLabel.setText("🔁 Turns: " + turnCount);
    }

    private void showWinPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Yeah!");
        alert.setHeaderText("🎉 YOU WIN 🎉");
        alert.setContentText("Play Again?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                Navigation.showGamePage(stage, difficulty);
            } else {
                Navigation.showHomePage(stage);
            }
        });
    }
}
