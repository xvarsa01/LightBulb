package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logic.Game;
import logic.GameNode;
import logic.Position;

public class InfoPanel {
    private final Stage stage;
    private final Label[][] labels;
    private final NodeView[][] nodeViews;

    public InfoPanel(Game game, String selectedColor) {
        game.addObserver(o -> refresh(game));

        int rows = game.rows();
        int cols = game.cols();

        labels = new Label[rows][cols];
        nodeViews = new NodeView[rows][cols];

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setStyle("-fx-background-color: black;");

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameNode node = game.node(new Position(r + 1, c + 1));

                NodeView nodeView = new NodeView(node, selectedColor);
                nodeView.setDisable(true);
                nodeView.setPrefSize(50, 50);
                nodeView.setStyle("-fx-border-color: white; -fx-background-color: gray;");
                nodeView.update(node);

                nodeViews[r][c] = nodeView;

                Label infoLabel = new Label(formatLabelText(node));
                infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");

                labels[r][c] = infoLabel;

                VBox cell = new VBox(2, nodeView, infoLabel);
                cell.setAlignment(Pos.CENTER);
                cell.setPadding(new Insets(2));
                VBox.setVgrow(nodeView, Priority.NEVER);

                gridPane.add(cell, c, r);
            }
        }

        Scene scene = new Scene(gridPane);
        stage = new Stage();
        stage.setTitle("Info Panel");
        stage.setScene(scene);
    }

    public boolean isVisible() {
        return stage.isShowing();
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public void refresh(Game game) {
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                GameNode node = game.node(new Position(r, c));
                nodeViews[r - 1][c - 1].update(node);
                labels[r - 1][c - 1].setText(formatLabelText(node));
            }
        }
    }

    private String formatLabelText(GameNode node) {
        return "Need: " + node.turnsRemainingToCorrectRotation() +
            "\nTurns: " + node.getUserRotatedCounter();
    }
}
