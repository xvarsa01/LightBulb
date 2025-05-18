/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: UI panel shown when the game is paused.
 */

package UI;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

public class PausePanel extends VBox {
    public PausePanel(Runnable onResume, Runnable onMainMenu) {
        super(20);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.5),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setMouseTransparent(false);

        Button resumeButton = GamePage.createStyledButton("▶ Resume", "green", onResume);
        Button homeButton = GamePage.createStyledButton("🏠 Main Menu", "gray", onMainMenu);

        getChildren().addAll(resumeButton, homeButton);
        setVisible(false);
        // Make sure it resizes and blocks interaction
        setMouseTransparent(false);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    }
}
