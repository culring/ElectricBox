package electricBox.game;

import electricBox.levelMenu.LevelMenuController;
import electricBox.mainMenu.MainMenuController;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-06-09.
 */
public class GameController {
    private Stage _primaryStage;
    private GameView _view;
    private GameModel _model;
    private int _level;

    public GameController(Stage stage, int level){
        _primaryStage = stage;
        _level = level;

        _model = new GameModel();

        _view = new GameView(_primaryStage, level);
        _view.setMenuButton(event -> launchMenu());
        _view.setLevelButton(event -> launchLevelMenu());

        class TestIsModelAcceptingMoveHandler implements GameView.TestIsModelAcceptingMove{
            @Override
            public boolean test(int sourceBoard, int sourceX, int sourceY,
                                int destBoard, int destX, int destY){
                return _model.move(sourceBoard, sourceX, sourceY,
                        destBoard, destX, destY);
            }
        }
        _view.setDragHandler(new TestIsModelAcceptingMoveHandler());

        _view.createSprite(GameView.SpriteType.CURRENT_GENERATOR, GameView.Board.GAME_BOARD, 0, 0);
        _view.createSprite(GameView.SpriteType.PROPELLER, GameView.Board.INVENTORY_BOARD, 1, 1);
        _view.createSprite(GameView.SpriteType.CURRENT_RECEIVER, GameView.Board.GAME_BOARD, 1, 1);
        try {
            _model.createObject(GameModel.GameObjectType.CURRENT_GENERATOR, GameModel.BoardType.GAME, 0, 0);
            _model.createObject(GameModel.GameObjectType.PROPELLER, GameModel.BoardType.INVENTORY, 1, 1);
            _model.createObject(GameModel.GameObjectType.CURRENT_RECEIVER, GameModel.BoardType.GAME, 1, 1);
        }
        catch(Exception e){
            System.exit(1);
        }
    }

    private void launchMenu(){
        MainMenuController controller = new MainMenuController(_primaryStage);
    }
    private void launchLevelMenu(){
        LevelMenuController controller = new LevelMenuController(_primaryStage);
    }
}
