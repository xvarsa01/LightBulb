/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: UI panel displayed when the player wins.
 */

package UI;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class WinPanel extends StackPane {

    public WinPanel(Runnable onPlayAgain, Runnable onHome) {
        setPickOnBounds(true);

        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.widthProperty().bind(widthProperty());
        overlay.heightProperty().bind(heightProperty());

        Label winLabel = new Label("🎉 YOU WIN 🎉");
        winLabel.setFont(Font.font(36));
        winLabel.setTextFill(Color.BLACK);

        Button playAgain = GamePage.createStyledButton("▶ Play Again", "green", onPlayAgain);
        Button goHome = GamePage.createStyledButton("🏠 Home", "red", onHome);

        VBox panel = new VBox(20, winLabel, playAgain, goHome);
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-padding: 40px;");

        getChildren().addAll(overlay, panel);
        setVisible(false);
        // Make sure it resizes and blocks interaction
        setMouseTransparent(false);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    }
}
