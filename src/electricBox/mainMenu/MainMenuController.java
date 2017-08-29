package electricBox.mainMenu;

import electricBox.levelMenu.LevelMenuController;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-05-30.
 */
public class MainMenuController {
    private MainMenuView _view;
    private Stage _primaryStage;

    public MainMenuController(Stage primaryStage){
        _primaryStage = primaryStage;

        _view = new MainMenuView(_primaryStage);
        _view.setPlayButton(event -> launchLevelMenu());
        _view.setExitButton(event -> _primaryStage.close());
    }

    public void launchLevelMenu(){
        new LevelMenuController(_primaryStage);
    }
}