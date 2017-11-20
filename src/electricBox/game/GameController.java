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


        //view.createSprite(GameView.SpriteType.PROPELLER, GameView.Board.INVENTORY_BOARD, 1, 1);
        this.view.createSprite(GameView.SpriteType.CURRENT_GENERATOR, GameView.Board.GAME_BOARD, 1, 4);
        this.view.createSprite(GameView.SpriteType.CURRENT_RECEIVER, GameView.Board.GAME_BOARD, 6, 4);
        this.view.createSprite(GameView.SpriteType.CURRENT_RECEIVER, GameView.Board.GAME_BOARD, 6, 7);
        this.view.createSprite(GameView.SpriteType.CURRENT_RECEIVER, GameView.Board.GAME_BOARD, 6, 1);

        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 2, 4);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 3, 4);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 4);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 5, 4);

        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 5);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 6);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 7);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 5, 7);

        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 3);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 2);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 4, 1);
        this.view.createSprite(GameView.SpriteType.WIRE, GameView.Board.GAME_BOARD, 5, 1);

        try {
            this.model.createObject(GameModel.GameObjectType.CURRENT_GENERATOR, GameModel.BoardType.GAME_BOARD, 1, 4);
            this.model.createObject(GameModel.GameObjectType.CURRENT_RECEIVER, GameModel.BoardType.GAME_BOARD, 6, 4);
            this.model.createObject(GameModel.GameObjectType.CURRENT_RECEIVER, GameModel.BoardType.GAME_BOARD, 6, 7);
            this.model.createObject(GameModel.GameObjectType.CURRENT_RECEIVER, GameModel.BoardType.GAME_BOARD, 6, 1);

            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 2, 4);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 3, 4);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 4);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 5, 4);

            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 5);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 6);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 7);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 5, 7);

            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 3);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 2);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 4, 1);
            this.model.createObject(GameModel.GameObjectType.WIRE, GameModel.BoardType.GAME_BOARD, 5, 1);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

        class ModelListener implements GameModel.Listener{
            public void power(int x, int y){
                view.power(x, y);
            }
        }

        class ViewListener implements GameView.Listener{
            public void clicked(int x, int y){
                model.power(x, y);
            }
        }

        model.addListener(new ModelListener());
        view.addListener(new ViewListener());
    }

    private void launchMenu(){
        MainMenuController controller = new MainMenuController(this.primaryStage);
    }
    private void launchLevelMenu(){
        LevelMenuController controller = new LevelMenuController(this.primaryStage);
    }
}
