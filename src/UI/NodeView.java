package UI;

import common.Observable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.GameNode;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class NodeView extends StackPane implements Observable.Observer {

    private final GameNode node;
    private final double width;
    private final double height;
    private final String selectedColor;
    private int changedModel = 0;
    private boolean interactionDisabled = false;
    private static final Map<String, Image> imageCache = new HashMap<>();

    public NodeView(final GameNode node, String selectedColor) {
        this.node = node;
        this.selectedColor = selectedColor;

        setPrefSize(60, 60);
        width = getPrefWidth() > 0 ? getPrefWidth() : 60;
        height = getPrefHeight() > 0 ? getPrefHeight() : 60;

        updateView();

        setOnMouseClicked(e -> {
            if (!interactionDisabled) {
                node.turn(true);
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
        if (!node.isLighted()){
            imageName = "off_" + imageName;
        }

        if (imageName != null) {
            String imagePath = "file:lib/icons/" + selectedColor + "/" + imageName;
            Image image = imageCache.computeIfAbsent(imagePath, path -> {
                try {
                    return new Image(path);
                } catch (Exception e) {
                    System.err.println("Image load failed: " + path);
                    return null;
                }
            });

            if (image != null) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                imageView.setPreserveRatio(true);
                imageView.setRotate(node.getActualRotation() * 90);
                getChildren().add(imageView);
            }
        }
    }
}
