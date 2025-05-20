/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Main game UI page including game board and controls.
 */

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

/**
 * GamePage handles the rendering and logic for the main game screen.
 * It manages the game board, UI controls, overlays, and user interactions.
 */
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

    /**
     * Constructs a new GamePage with a specific stage and difficulty level.
     *
     * @param stage      the JavaFX stage to display the game on
     * @param difficulty the selected difficulty level
     */
    public GamePage(Stage stage, Difficulty difficulty) {
        this.stage = stage;
        this.difficulty = difficulty;
    }

    /**
     * Initializes and displays the game scene.
     *
     * @return the initialized Game instance
     */
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

        String selectedColor = getRandomColor();
        infoPanel = new InfoPanel(game, selectedColor);

        game.init();

        GridPane gridPane = createGameGrid(rows, selectedColor);
        StackPane stackPane = setupGameStack(gridPane);

        BorderPane root = new BorderPane();
        setBackgroundImage(root);
        root.setCenter(stackPane);

        stage.setScene(new Scene(root));
        stage.show();
        return game;
    }

    /**
     * Chooses a random color for node highlighting.
     *
     * @return the name of a random color
     */
    private static String getRandomColor() {
        //todo
        String[] colors = {"azure", "brown", "darkBlue", "darkRed", "green", "lime", "pink", "purple", "yellow"};
//        String[] colors = {"lime"};
        return colors[new Random().nextInt(colors.length)];
    }

    /**
     * Sets up an observer to track game updates and detect game completion.
     *
     * @param rows the number of rows in the grid
     */
    private void setupGameObserver(int rows) {
        game.addObserver(o -> {
            if (nodeViews[0][0] == null) return;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < rows; c++) {
                    nodeViews[r][c].update(o);
                }
            }

            if (!isRandomizing && game.GameFinished()) {
                Platform.runLater(this::stopTimer);
                Score.increment(difficulty);
                showWinPopup();
            }
        });
    }

    /**
     * Generates a valid map with an acceptable number of bulbs based on difficulty.
     *
     * @param rows the grid size
     */
    private void generateValidMap(int rows) {
        MapGenerator generator = new MapGenerator();
        generator.generateMap(game, rows, rows);
        while (!isNiceMap(game, rows)) {
            game.clearMap();
            generator.generateMap(game, rows, rows);
        }
    }

    /**
     * Validates if a generated map has an appropriate bulb count.
     *
     * @param game the game instance
     * @param rows number of rows in the map
     * @return true if the map is valid, false otherwise
     */
    private boolean isNiceMap(Game game, int rows) {
        int bulbs = game.bulbNodesCount();
        return switch (rows) {
            case 5 -> bulbs >= 5 && bulbs <= 7;
            case 6 -> bulbs >= 7 && bulbs <= 9;
            case 7 -> bulbs >= 9 && bulbs <= 11;
            default -> true;
        };
    }

    /**
     * Creates the game board grid with interactive node views.
     *
     * @param rows  grid size
     * @param color the node highlight color
     * @return the constructed GridPane
     */
    private GridPane createGameGrid(int rows, String color) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= rows; col++) {
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);
                NodeView nodeView = new NodeView(node, color);

                nodeView.setOnMouseClicked(e -> handleMouseClick(nodeView, node));

                nodeViews[row - 1][col - 1] = nodeView;
                gridPane.add(nodeView, col - 1, row - 1);
            }
        }
        return gridPane;
    }

    /**
     * Handles mouse click actions on a node view.
     *
     * @param nodeView the clicked NodeView
     * @param node     the associated GameNode
     */
    private void handleMouseClick(NodeView nodeView, GameNode node) {
        if (nodeView.isInteractionDisabled()) {
            System.err.println("Interaction is disabled");
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

    /**
     * Creates and returns the main StackPane containing the UI and overlays.
     *
     * @param gridPane the game board grid
     * @return the main StackPane
     */
    private StackPane setupGameStack(GridPane gridPane) {
        gridPane.setStyle("-fx-alignment: center;");

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
                createStyledButton("🏠 Home", "gray", () -> {
                    Navigation.showHomePage(stage);
                    if(infoPanel != null) {
                        infoPanel.hide();
                    }
                }),
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


    /**
     * Toggles the visibility of the InfoPanel.
     */
    private void toggleInfoPanel() {
        if (infoPanel != null) {
            if (infoPanel.isVisible()) {
                infoPanel.hide();
            } else {
                infoPanel.show();
            }
        }
    }


    /**
     * Initializes the pause overlay panel.
     */
    private void setupPauseOverlay() {
        pauseOverlay = new PausePanel(
            this::hidePauseMenu,
            () -> {
                stopTimer();
                Navigation.showHomePage(stage);
            }
        );
    }


    /**
     * Initializes the win overlay panel.
     */
    private void setupWinOverlay() {
        winOverlay = new WinPanel(
                () -> Navigation.showGamePage(stage, difficulty),
                () -> Navigation.showHomePage(stage)
        );
    }


    /**
     * Sets the background image for the root layout.
     *
     * @param root the root layout pane
     */
    private void setBackgroundImage(BorderPane root) {
        File file = new File("lib/home-background.jpg");
        String localUrl = file.toURI().toString();
        root.setStyle("-fx-background-image: url('" + localUrl + "'); " +
                "-fx-background-size: cover; -fx-background-position: center center;");
    }

    /**
     * Enables or disables interaction for the entire board.
     *
     * @param disabled true to disable, false to enable
     */
    public void setBoardInteractionDisabled(boolean disabled) {
        for (NodeView[] row : nodeViews) {
            for (NodeView nv : row) {
                nv.setInteractionDisabled(disabled);
            }
        }
    }

    /**
     * Displays the pause overlay and disables the game board.
     */
    private void showPauseMenu() {
        pauseOverlay.setVisible(true);
        if (infoPanel != null) infoPanel.hide();
        setBoardInteractionDisabled(true);
        stopTimer();
    }

    /**
     * Hides the pause overlay and resumes the game.
     */
    private void hidePauseMenu() {
        pauseOverlay.setVisible(false);
        setBoardInteractionDisabled(false);
        if(timeline.getStatus() != Timeline.Status.RUNNING) {
            continueTimer();
        }
    }

    /**
     * Starts the game timer from zero.
     */
    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        elapsedSeconds = 0;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                elapsedSeconds++;
                timerLabel.setText("⏱ Time: " + elapsedSeconds + "s");
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Continues the game timer from the last recorded time.
     */
    private void continueTimer(){
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++;
            timerLabel.setText("⏱ Time: " + elapsedSeconds + "s");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Stops the game timer.
     */
    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Increments the player's turn counter and updates the UI label.
     */
    public void incrementTurnCount() {
        turnCount++;
        turnLabel.setText("🔁 Turns: " + turnCount);
    }

    /**
     * Displays the win overlay when the game is completed.
     */
    private void showWinPopup() {
        stopTimer();
        if (infoPanel != null) infoPanel.hide();
        setBoardInteractionDisabled(true);

        winOverlay.setVisible(true);
    }

    /**
     * Handles undo functionality using move history.
     */
    private void handleUndo() {
        MoveRecord record = moveHistory.undo();
        if (record != null) {
            GameNode node = game.node(record.position());
            // restore previous rotation
            while (node.getActualRotation() != record.previousRotation()) {
                node.turn(false);
            }
        }
    }

    /**
     * Handles redo functionality using move history.
     */
    private void handleRedo() {
        MoveRecord record = moveHistory.redo();
        if (record != null) {
            GameNode node = game.node(record.position());
            node.turn(false);
        }
    }

    /**
     * Creates a styled button with the specified text, color, and action.
     *
     * @param text   the button label
     * @param color  the background color
     * @param action the action to execute on click
     * @return the created Button
     */
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

    /**
     * Starts a countdown animation and randomizes the game board afterward.
     *
     * @param game       the game instance
     * @param difficulty the game difficulty
     */
    public void startCountdownAndRandomize(Game game, Difficulty difficulty) {
        countdownLabel.setVisible(true);
        int[] count = {3};

        Timeline countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
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

        countdown.setCycleCount(count.length + 4); // 3..2..1..Go..(hide)
        countdown.play();
    }

    /**
     * Randomizes the game board by turning a percentage of nodes based on difficulty.
     *
     * @param game       the game instance
     * @param difficulty the game difficulty
     */
    private void runRandomizer(Game game, Difficulty difficulty) {

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
            randomizer.randomlyTurnSomeNodes(affectedNodesPercentage / 100f, 0, rotatedNodesAtSameTime);

            Platform.runLater(() -> {
                isRandomizing = false;
                setBoardInteractionDisabled(false);
                startTimer();
            });
        }).start();
    }
}
