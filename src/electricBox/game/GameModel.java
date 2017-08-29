package electricBox.game;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Culring on 2017-06-06.
 */
public class GameModel {
    public enum GameState {
        EDITTING,
        RUNNING,
        FINISHED
    }

    //public enum GameObjectType{
    //    CURRENT_GENERATOR,
    //    CURRENT_RECEIVER,
    //    WIRE,
    //    PROPELLER,
    //    WIND_TURBINE,
    //    LASER,
    //    LASER_DETECTOR,
    //    MIRROR
    //}
    public enum BoardType {
        GAME(0),
        INVENTORY(1);

        private int _value;

        BoardType(int value) {
            _value = value;
        }

        public int getValue() {
            return _value;
        }
    }

    private final Board boards[];
    private GameState gameState;
    private LinkedList<FlowingCurrentAction> actionQueue;

    private Coordinations generatorCoordinations, receiverCoordinations;

    {
        boards = new Board[2];
        boards[BoardType.GAME.getValue()] = new Board(BoardType.GAME);
        boards[BoardType.INVENTORY.getValue()] = new Board(BoardType.INVENTORY);

        gameState = GameState.EDITTING;
    }

    // returns 0 if a transaction will end up with a success,
    // otherwise 1
    int createObject(GameObjectType componentType, BoardType board, int x, int y) throws Exception {
        GameComponent gameComponent;

        switch (componentType) {
            case CURRENT_GENERATOR:
                if (board != BoardType.GAME) {
                    throw new Exception("Generator tried to put in the inventory board");
                }
                gameComponent = new CurrentGenerator();
                generatorCoordinations = new Coordinations(x, y);

                break;

            case CURRENT_RECEIVER:
                if (board != BoardType.GAME) {
                    throw new Exception("Receiver tried to put in the inventory board");
                }
                gameComponent = new CurrentReceiver();
                receiverCoordinations = new Coordinations(x, y);

                break;

            case WIRE:
                gameComponent = new Wire();
                break;

            case PROPELLER:
                gameComponent = new Propeller();
                break;

            default:
                gameComponent = null;
        }

        if (boards[board.getValue()].doesCollide(gameComponent, x, y)) {
            System.exit(1);
        }
        boards[board.getValue()].set(gameComponent, x, y);

        return 0;
    }

    /* return true if object was successfully moved,
    * otherwise 0 */
    public boolean move(int sourceBoard, int sourceX, int sourceY,
                        int destBoard, int destX, int destY) {

        /* if gameState is already running or finished, or
        * object is not movable, move operation cannot be proceeded */
        if (gameState == GameState.RUNNING || gameState == GameState.FINISHED ||
                !boards[sourceBoard]._board[sourceX][sourceY].isMovable()) {
            return false;
        }

        Board destinationBoard = boards[destBoard];
        GameComponent collisionObject = boards[sourceBoard]._board[sourceX][sourceY];
        if (!destinationBoard.doesCollide(collisionObject, destX, destY)) {
            boards[sourceBoard].remove(collisionObject, sourceX, sourceY);

            destinationBoard.set(collisionObject, destX, destY);

            return true;
        }
        return false;
    }

    public void startGame() throws Exception {
        /* TODO: po dodaniu generatora itd. odkomentować */
        /*if(generatorCoordinations == null || receiverCoordinations == null){
            throw new Exception("Cannot find current generator or current receiver");
        }*/

        gameState = GameState.RUNNING;

        /* launching current generator */
        int generatorX = generatorCoordinations.getBoardX();
        int generatorY = generatorCoordinations.getBoardY();
        boards[BoardType.GAME.getValue()]._board[generatorX][generatorY].activate();
    }

    class FlowingCurrentAction {
        protected Board _board;
        protected int _boardX, _boardY;
    }

    class Coordinations {
        private int _boardX, _boardY;

        Coordinations(int x, int y) {
            _boardX = x;
            _boardY = y;
        }

        public int getBoardX() {
            return _boardX;
        }

        public int getBoardY() {
            return _boardY;
        }
    }

    // old definitions of game objects
    //private class CurrentGenerator extends GameComponent{
    //    CurrentGenerator(){
    //        super();
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //
    //    public void activate(){
    //
    //    }
    //}

    //private class CurrentReceiver extends GameComponent{
    //    CurrentReceiver(){
    //        super(false);
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //}
    //
    //private class Wire extends GameComponent{
    //    Wire(){
    //        super(false);
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //}
    //
    //private class Propeller extends GameComponent{
    //    Propeller(){
    //        super(true);
    //
    //        collisionHeight = 2;
    //        collisionWidth = 1;
    //    }
    //}
    //
    //private class WindTurbine extends GameComponent{
    //    WindTurbine(){
    //        super(true);
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //}
    //
    //private class Laser extends GameComponent{
    //    Laser(){
    //        super(true);
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //}
    //
    //private class LaserDetector extends GameComponent{
    //    LaserDetector(){
    //        super(false);
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //}
    //
    //private class Mirror extends GameComponent{
    //    Mirror(){
    //        super(false);
    //
    //        collisionHeight = 1;
    //        collisionWidth = 1;
    //    }
    //}

    private class Board {
        private GameComponent _board[][];
        private int _height, _width;

        Board(BoardType boardType) {
            switch (boardType) {
                case GAME:
                    _height = 9;
                    _width = 9;
                    break;

                case INVENTORY:
                    _height = 6;
                    _width = 3;
                    break;

                default:
                    break;
            }

            _board = new GameComponent[_width][_height];
        }

        public int getHeight() {
            return _height;
        }

        public int getWidth() {
            return _width;
        }

        public void set(GameComponent gameComponent, int x, int y) {
            int collisionWidth = gameComponent.getCollisionWidth();
            int collisionHeight = gameComponent.getCollisionHeight();
            for (int i = 0; i < collisionWidth; i++) {
                for (int j = 0; j < collisionHeight; j++) {
                    _board[x + i][y + j] = gameComponent;
                }
            }
        }

        public boolean doesCollide(GameComponent gameComponent, int x, int y) {
            int collisionWdith = gameComponent.getCollisionWidth();
            int collisionHeight = gameComponent.getCollisionHeight();

            for (int i = 0; i < collisionWdith; ++i) {
                for (int j = 0; j < collisionHeight; j++) {
                    if (x + i >= _width || y + j >= _height ||
                            (_board[x + i][y + j] != null && _board[x + i][y + j] != gameComponent)) {
                        return true;
                    }
                }
            }

            return false;
        }

        public void remove(GameComponent gameComponent, int x, int y) {
            int collisionWidth = gameComponent.getCollisionWidth();
            int collisionHeight = gameComponent.getCollisionHeight();

            for (int i = 0; i < collisionWidth; ++i) {
                for (int j = 0; j < collisionHeight; j++) {
                    _board[x + i][y + j] = null;
                }
            }
        }

    }
}
