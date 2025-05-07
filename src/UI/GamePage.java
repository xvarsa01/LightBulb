package UI;

import javafx.scene.layout.HBox;
import logger.GameLogger;
import enums.Difficulty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import logger.MoveHistory;
import logger.MoveRecord;
import logic.*;
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

import java.io.File;

import java.util.Random;

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
    private InfoPanel infoPanel;

    private final MoveHistory moveHistory = new MoveHistory();

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

        setupGameObserver(rows);
        generateValidMap(rows);

        infoPanel = new InfoPanel(game);
        infoPanel.show();

        game.init();
        String selectedColor = getRandomColor();

        GridPane gridPane = createGameGrid(rows, selectedColor);
        VBox vbox = setupVBox(gridPane);

        BorderPane root = new BorderPane();
        setBackgroundImage(root);
        root.setCenter(vbox);
        root.setTop(new Label("🎮 Game Page"));

        stage.setScene(new Scene(root));
        stage.show();
        return game;
    }

    private static String getRandomColor() {
        String[] colors = {"azure", "brown", "darkBlue", "darkRed", "green", "lime", "pink", "purple", "yellow"};
        return colors[new Random().nextInt(colors.length)];
    }

    private void setupGameObserver(int rows) {
        game.addObserver(o -> {
            if (nodeViews[0][0] == null) return;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < rows; c++) {
                    nodeViews[r][c].update(o);
                }
            }

            if (game.GameFinished() && timeline != null) {
                Platform.runLater(this::stopTimer);
                Score.increment(difficulty);
                showWinPopup();
            }
        });
    }

    private void generateValidMap(int rows) {
        MapGenerator generator = new MapGenerator();
        generator.generateMap(game, rows, rows);
        while (!isNiceMap(game, rows)) {
            game.clearMap();
            generator.generateMap(game, rows, rows);
        }
    }

    private boolean isNiceMap(Game game, int rows) {
        int bulbs = game.bulbNodesCount();
        return switch (rows) {
            case 5 -> bulbs >= 5 && bulbs <= 7;
            case 6 -> bulbs >= 7 && bulbs <= 9;
            case 7 -> bulbs >= 9 && bulbs <= 11;
            default -> true;
        };
    }

    private GridPane createGameGrid(int rows, String color) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= rows; col++) {
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);
                NodeView nodeView = new NodeView(node, color);

                nodeView.setOnMouseClicked(e -> {
                    handleMouseClick(nodeView, node);
                });

                nodeViews[row - 1][col - 1] = nodeView;
                gridPane.add(nodeView, col - 1, row - 1);
            }
        }
        return gridPane;
    }

    private void handleMouseClick(NodeView nodeView, GameNode node) {
        if (nodeView.isInteractionDisabled()) {
            return;
        }
        moveHistory.addMove(new MoveRecord(
                node.position,
                node.nodeType,
                node.isLighted(),
                node.getActualRotation()
        ));
        node.turn(true);

        incrementTurnCount();
        if (infoPanel != null) {
            infoPanel.refresh(game);
        }
        GameLogger.log(node.toString());
    }

    private VBox setupVBox(GridPane gridPane) {
        timerLabel = new Label("⏱ Time: 0s");
        timerLabel.setStyle("-fx-text-fill: white;");

        turnLabel = new Label("🔁 Turns: 0");
        turnLabel.setStyle("-fx-text-fill: white;");

        HBox buttonRow = new HBox(20,
                createStyledButton("⬅ Undo", "silver", this::handleUndo),
                createStyledButton("🏠 Home", "gray", () -> Navigation.showHomePage(stage)),
                createStyledButton("➡ Redo", "silver", this::handleRedo)
        );

        buttonRow.setPadding(new Insets(10));
        buttonRow.setStyle("-fx-alignment: center;");

        VBox vbox = new VBox(10, timerLabel, turnLabel, gridPane, buttonRow);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-alignment: center;");

        return vbox;
    }

    private void setBackgroundImage(BorderPane root) {
        File file = new File("lib/home-background.jpg");
        String localUrl = file.toURI().toString();
        root.setStyle("-fx-background-image: url('" + localUrl + "'); " +
                "-fx-background-size: cover; -fx-background-position: center center;");
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
        alert.setContentText("Play next game?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            infoPanel.hide();
            if (response == yesButton) {
                Navigation.showGamePage(stage, difficulty);
            } else {
                Navigation.showHomePage(stage);
            }
        });
    }

    private void handleUndo() {
        MoveRecord record = moveHistory.undo();
        if (record != null) {
            GameNode node = game.node(record.position);
            // restore previous rotation
            while (node.getActualRotation() != record.previousRotation) {
                node.turn(false);
            }
        }
    }

    private void handleRedo() {
        MoveRecord record = moveHistory.redo();
        if (record != null) {
            GameNode node = game.node(record.position);
            node.turn(false);
        }
    }

    private static Button createStyledButton(String text, String color, Runnable action) {
        Button button = new Button(text);
        String baseStyle = "-fx-font-size: 18px; -fx-background-color: " + color +
                "; -fx-text-fill: white; -fx-padding: 10 20 10 20; -fx-background-radius: 10;";

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(baseStyle.replace("white", "black")));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnAction(e -> action.run());

        return button;
    }

}
