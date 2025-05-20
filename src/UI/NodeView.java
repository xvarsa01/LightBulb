/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Visual representation of a node in the UI.
 */

package UI;

import common.Observable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.GameNode;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

/**
 * NodeView is the UI representation of a single {@link GameNode} in the game grid.
 * It displays the correct image based on node type, rotation, and power/light status.
 */
public class NodeView extends StackPane implements Observable.Observer {

    private final GameNode node;
    private final double width;
    private final double height;
    private final String selectedColor;
    private int changedModel = 0;
    private boolean interactionDisabled = true;
    private static final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Constructs a NodeView for the given game node using the selected color scheme.
     *
     * @param node           the GameNode this view represents
     * @param selectedColor  the theme color (e.g., "lime", "pink", etc.) for this node
     */
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

    /**
     * Enables or disables user interaction (clicking) on this node.
     *
     * @param disabled true to disable interaction, false to enable
     */
    public void setInteractionDisabled(boolean disabled) {
        this.interactionDisabled = disabled;
    }

    /**
     * Checks whether user interaction is currently disabled.
     *
     * @return true if interaction is disabled, false otherwise
     */
    public boolean isInteractionDisabled() {
        return interactionDisabled;
    }

    /**
     * Called by the game logic when this node changes state.
     * Increments an internal change counter and refreshes the UI.
     *
     * @param observable the observable object triggering the update
     */
    public void update(Observable observable) {
        changedModel++;
        updateView();
    }

    /**
     * Updates the graphical representation of this node based on its type, state,
     * rotation, and lighting. Uses image caching to improve performance.
     */
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
                imageView.setRotate(node.getIconRotation() * 90);
                getChildren().add(imageView);
            }
        }
    }
}
