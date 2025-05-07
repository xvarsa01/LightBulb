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

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = game.node(new Position(r, c));
                int needed = node.turnsRemainingToCorrectRotation();
                int actual = node.getUserRotatedCounter();

                Label label = new Label("Need: " + needed + "\nTurns: " + actual);
                labels[r - 1][c - 1] = label;
                gridPane.add(label, c - 1, r - 1);
            }
        }

        Scene scene = new Scene(gridPane);
        stage = new Stage();
        stage.setTitle("Game Info Panel");
        stage.setScene(scene);
    }


    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public void refresh(Game game) {
        int rows = game.rows();
        int cols = game.cols();

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = game.node(new Position(r, c));
                int needed = node.turnsRemainingToCorrectRotation();
                int actual = node.getUserRotatedCounter();
                labels[r - 1][c - 1].setText("Need: " + needed + "\nTurns: " + actual);
            }
        }
    }

}
