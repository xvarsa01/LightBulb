package UI;

import javafx.scene.layout.*;
import logger.GameLogger;
import enums.Difficulty;
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

    private Label countdownLabel;
    private InfoPanel infoPanel;
    private PausePanel pauseOverlay;
    private WinPanel winOverlay;

    private final MoveHistory moveHistory = new MoveHistory();

    private boolean isRandomizing = true;

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

        game.init();
        String selectedColor = getRandomColor();

        GridPane gridPane = createGameGrid(rows, selectedColor);
        StackPane stackPane = setupGameStack(gridPane);

        BorderPane root = new BorderPane();
        setBackgroundImage(root);
        root.setCenter(stackPane);

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

            if (!isRandomizing && game.GameFinished() && timeline != null) {
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

    private StackPane setupGameStack(GridPane gridPane) {
        timerLabel = new Label("⏱ Time: 0s");
        timerLabel.setStyle("-fx-text-fill: white;");

        turnLabel = new Label("🔁 Turns: 0");
        turnLabel.setStyle("-fx-text-fill: white;");

        countdownLabel = new Label();
        countdownLabel.setStyle("-fx-text-fill: white; -fx-font-size: 36px;");
        countdownLabel.setVisible(false);

        HBox buttonRowTop = new HBox(20,
                timerLabel,
                turnLabel,
                createStyledButton("Hint", "silver", this::toggleInfoPanel),
                createStyledButton("⏸ Pause", "silver", this::showPauseMenu)
        );
        buttonRowTop.setPadding(new Insets(10));
        buttonRowTop.setStyle("-fx-alignment: center;");

        HBox buttonRowBottom = new HBox(20,
                createStyledButton("⬅ Undo", "silver", this::handleUndo),
                createStyledButton("🏠 Home", "gray", () -> Navigation.showHomePage(stage)),
                createStyledButton("➡ Redo", "silver", this::handleRedo)
        );
        buttonRowBottom.setPadding(new Insets(10));
        buttonRowBottom.setStyle("-fx-alignment: center;");

        VBox mainVBox = new VBox(10, buttonRowTop, gridPane, buttonRowBottom);
        mainVBox.setPadding(new Insets(20));
        mainVBox.setStyle("-fx-alignment: center;");
        mainVBox.setMouseTransparent(false); // Important

        setupPauseOverlay(); // make sure this sets up a full overlay
        setupWinOverlay();   // make sure this sets up a full overlay

        return new StackPane(mainVBox, pauseOverlay, winOverlay, countdownLabel);
    }

    private void toggleInfoPanel() {
        if (infoPanel != null) {
            if (infoPanel.isVisible()) {
                infoPanel.hide();
            } else {
                infoPanel.show();
            }
        }
    }

    private void setupPauseOverlay() {
        pauseOverlay = new PausePanel(
            this::hidePauseMenu,
            () -> {
                stopTimer();
                Navigation.showHomePage(stage);
            }
        );
    }

    private void setupWinOverlay() {
        winOverlay = new WinPanel(
                () -> Navigation.showGamePage(stage, difficulty),
                () -> Navigation.showHomePage(stage)
        );
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

    private void disableBoardInteractionAndButtons(boolean disabled) {
        setBoardInteractionDisabled(disabled);

    }

    private void showPauseMenu() {
        pauseOverlay.setVisible(true);
        if (infoPanel != null) infoPanel.hide();
        disableBoardInteractionAndButtons(true);
        stopTimer();
    }

    private void hidePauseMenu() {
        pauseOverlay.setVisible(false);
        disableBoardInteractionAndButtons(false);
        continueTimer();
    }

    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        elapsedSeconds = 0;
        timerLabel.setText("⏱ Time: 0s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!isRandomizing){
                elapsedSeconds++;
                timerLabel.setText("⏱ Time: " + elapsedSeconds + "s");
            }

        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private void continueTimer(){
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
        stopTimer();
        if (infoPanel != null) infoPanel.hide();
        setBoardInteractionDisabled(true);

        winOverlay.setVisible(true);
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

    public static Button createStyledButton(String text, String color, Runnable action) {
        Button button = new Button(text);
        String baseStyle = "-fx-font-size: 18px; -fx-background-color: " + color +
                "; -fx-text-fill: white; -fx-padding: 10 20 10 20; -fx-background-radius: 10;";

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(baseStyle.replace("white", "black")));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnAction(e -> action.run());

        return button;
    }

    public void startCountdownAndRandomize(Game game, Difficulty difficulty) {
        countdownLabel.setVisible(true);
        int[] count = {3};

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (count[0] > 0) {
                countdownLabel.setText(String.valueOf(count[0]));
                count[0]--;
            } else if (count[0] == 0) {
                countdownLabel.setText("Go!");
                count[0]--;
            } else {
                countdownLabel.setVisible(false);
                runRandomizer(game, difficulty);
            }
        }));

        timeline.setCycleCount(count.length + 4); // 3..2..1..Go..(hide)
        timeline.play();
    }

    private void runRandomizer(Game game, Difficulty difficulty) {
        setBoardInteractionDisabled(false);

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

        Randomizer randomizer = new Randomizer(game);

        new Thread(() -> {
            randomizer.randomlyTurnSomeNodes(affectedNodesPercentage / 100f, 500, rotatedNodesAtSameTime);

            Platform.runLater(() -> {
                isRandomizing = false;
                startTimer();
            });
        }).start();
    }
}
