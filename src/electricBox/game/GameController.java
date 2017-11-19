package electricBox.game;

import electricBox.levelMenu.LevelMenuController;
import electricBox.mainMenu.MainMenuController;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-06-09.
 */
public class GameController {
    private Stage primaryStage;
    private GameView view;
    private GameModel model;
    private int level;

    public GameController(Stage stage, int level){
        this.primaryStage = stage;
        this.level = level;
        this.model = new GameModel();

        this.view = new GameView(primaryStage, level);
        this.view.setMenuButton(event -> launchMenu());
        this.view.setLevelButton(event -> launchLevelMenu());

        class TestIsModelAcceptingMoveHandler implements GameView.TestIsModelAcceptingMove{
            @Override
            public boolean test(int sourceBoard, int sourceX, int sourceY,
                                int destBoard, int destX, int destY) throws Exception{
                GameModel.BoardType sourceBoardType, destBoardType;

                switch(sourceBoard){
                    case 0:
                        sourceBoardType = GameModel.BoardType.GAME_BOARD;
                        break;

                    case 1:
                        sourceBoardType = GameModel.BoardType.INVENTORY_BOARD;
                        break;

                    default:
                        throw new Exception("Board not found");
                }
                switch(destBoard){
                    case 0:
                        destBoardType = GameModel.BoardType.GAME_BOARD;
                        break;

                    case 1:
                        destBoardType = GameModel.BoardType.INVENTORY_BOARD;
                        break;

                    default:
                        throw new Exception("Board not found");
                }

                return model.move(sourceBoardType, sourceX, sourceY,
                        destBoardType, destX, destY);
            }
        }
        view.setDragHandler(new TestIsModelAcceptingMoveHandler());

        this.view.createSprite(GameView.SpriteType.CURRENT_GENERATOR, GameView.Board.GAME_BOARD, 0, 0);
        //view.createSprite(GameView.SpriteType.PROPELLER, GameView.Board.INVENTORY_BOARD, 1, 1);
        this.view.createSprite(GameView.SpriteType.CURRENT_RECEIVER, GameView.Board.GAME_BOARD, 1, 1);
        try {
            this.model.createObject(GameModel.GameObjectType.CURRENT_GENERATOR, GameModel.BoardType.GAME_BOARD, 0, 0);
            //model.createObject(GameModel.GameObjectType.PROPELLER, GameModel.BoardType.INVENTORY, 1, 1);
            this.model.createObject(GameModel.GameObjectType.CURRENT_RECEIVER, GameModel.BoardType.GAME_BOARD, 1, 1);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void launchMenu(){
        MainMenuController controller = new MainMenuController(this.primaryStage);
    }
    private void launchLevelMenu(){
        LevelMenuController controller = new LevelMenuController(this.primaryStage);
    }
}
