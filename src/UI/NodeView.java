package UI;

import common.Observable;
import logic.GameNode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class NodeView extends StackPane implements Observable.Observer {

    private final GameNode node;
    private int changedModel = 0;

    public NodeView(final GameNode node) {
        this.node = node;
        setPrefSize(60, 60);
        updateView();

        setOnMouseClicked(e -> {
            node.turn();
        });
    }

    public void update(Observable observable) {
        changedModel++;
        updateView();
    }

    public int numberUpdates() {
        return changedModel;
    }

    public void clearChanged() {
        // optional: implement if needed
    }

    public void updateView() {
        getChildren().clear();

        double w = getPrefWidth() > 0 ? getPrefWidth() : 60;
        double h = getPrefHeight() > 0 ? getPrefHeight() : 60;
        double midX = w / 2;
        double midY = h / 2;

        Color lineColor = node.light() ? Color.RED : Color.BLACK;

        if (node.north()) addLine(midX, 0, midX, midY, lineColor);
        if (node.east()) addLine(w, midY, midX, midY, lineColor);
        if (node.south()) addLine(midX, h, midX, midY, lineColor);
        if (node.west()) addLine(0, midY, midX, midY, lineColor);

        if (node.isBulb()) {
            Circle circle = new Circle(midX, midY, Math.min(w, h) / 4);
            circle.setFill(node.light() ? Color.RED : Color.GRAY);
            getChildren().add(circle);
        }

        if (node.isPower()) {
            setStyle("-fx-background-color: green;");
        } else {
            setStyle("-fx-background-color: transparent;");
        }
    }

    private void addLine(double x1, double y1, double x2, double y2, Color color) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(color);
        line.setStrokeWidth(2);
        line.setManaged(false); // Ignore StackPane's layout rules
        line.setLayoutX(0);
        line.setLayoutY(0);
        getChildren().add(line);
    }
}
