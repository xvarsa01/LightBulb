/**
 * Authors: xhavli59
 * Date: 09.05.2025
 *
 * Description: Main entry point of the JavaFX application.
 */

import UI.Navigation;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Navigation.showHomePage(stage);
        //stage.setMaximized(true); //todo maximize all pages
    }
}
