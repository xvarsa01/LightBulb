package UI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Game;
import logic.GameNode;
import logic.Position;

public class InfoPanel {
    private final Stage stage;
    private final Label[][] labels;


    public InfoPanel(Game game) {
        int rows = game.rows();
        int cols = game.cols();

        labels = new Label[rows][cols];
        GridPane gridPane = new GridPane();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameNode node = game.node(new Position(r + 1, c + 1));

                Label label = new Label(formatLabelText(node));
                labels[r][c] = label;
                gridPane.add(label, c, r);
            }
        }

        Scene scene = new Scene(gridPane);
        stage = new Stage();
        stage.setTitle("Game Info Panel");
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
                labels[r - 1][c - 1].setText(formatLabelText(node));
            }
        }
    }

    private String formatLabelText(GameNode node) {
        return "Need: " + node.turnsRemainingToCorrectRotation() +
                "\nTurns: " + node.getUserRotatedCounter();
    }
}
