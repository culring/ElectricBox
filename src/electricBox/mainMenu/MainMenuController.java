package electricBox.mainMenu;

import electricBox.levelMenu.LevelMenuController;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-05-30.
 */
public class MainMenuController {
    private MainMenuView view;
    private Stage primaryStage;

    public MainMenuController(Stage primaryStage){
        this.primaryStage = primaryStage;

        this.view = new MainMenuView(this.primaryStage);
        this.view.setPlayButton(event -> launchLevelMenu());
        this.view.setExitButton(event -> this.primaryStage.close());
    }

    public void launchLevelMenu(){
        new LevelMenuController(this.primaryStage);
    }
}