package UI;

import common.Observable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.GameNode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class NodeView extends StackPane implements Observable.Observer {

    private final GameNode node;
    private final String selectedColor;
    private int changedModel = 0;
    private boolean interactionDisabled = false;

    public NodeView(final GameNode node, String selectedColor) {
        this.node = node;
        this.selectedColor = selectedColor;
        setPrefSize(60, 60);
        updateView();

        setOnMouseClicked(e -> {
            if (!interactionDisabled) {
                node.turn();
            }
        });
    }

    public void setInteractionDisabled(boolean disabled) {
        this.interactionDisabled = disabled;
    }

    public boolean isInteractionDisabled() {
        return interactionDisabled;
    }


    public void update(Observable observable) {
        changedModel++;
        updateView();
    }

    public int numberUpdates() {
        return changedModel;
    }

    public void updateView() {
        getChildren().clear();

        double w = getPrefWidth() > 0 ? getPrefWidth() : 60;
        double h = getPrefHeight() > 0 ? getPrefHeight() : 60;
        double midX = w / 2;
        double midY = h / 2;

        Color lineColor = node.light() ? Color.RED : Color.BLACK;

        String imageName = null;
        if (node.isLink()) {
            imageName = switch (node.getLinkShape()) {
                case L -> "pipe_L.png";
                case I -> "pipe_I.png";
                case T -> "pipe_T.png";
                case X -> "pipe_X.png";
                default -> null;
            };
        }
        else if (node.isPower()) {
            imageName = switch (node.getLinkShape()) {
                case L -> "pipe_L_P.png";
                case I -> "pipe_I_P.png";
                case T -> "pipe_T_P.png";
                case X -> "pipe_X_P.png";
                case S -> "pipe_S_P.png";
                default -> null;
            };
        }
        else if (node.isBulb()){
            imageName = "bulb.png";
        }
        if (!node.light()){
            imageName = "off_" + imageName;
        }

        try {
            Image image = new Image("file:lib/icons/darkBlue/" + imageName);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
            imageView.setPreserveRatio(true);

            // Rotate based on icon rotation counter (0 to 3) -> 0°, 90°, 180°, 270°
            int rotationSteps = node.getIconRotatedCounter(); // from 0 to 3
            imageView.setRotate(rotationSteps * 90);

            getChildren().add(imageView);
        } catch (Exception e) {
            System.err.println("Image load failed: " + imageName);
        }

        // left just for potential debug
//        if (node.north()) addLine(midX, 0, midX, midY, lineColor);
//        if (node.east()) addLine(w, midY, midX, midY, lineColor);
//        if (node.south()) addLine(midX, h, midX, midY, lineColor);
//        if (node.west()) addLine(0, midY, midX, midY, lineColor);
    }

    private void addLine(double x1, double y1, double x2, double y2, Color color) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(color);
        line.setStrokeWidth(2);
        line.setManaged(false);
        line.setLayoutX(0);
        line.setLayoutY(0);
        getChildren().add(line);
    }
}
