package electricBox;

import electricBox.mainMenu.MainMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        new MainMenuController(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}