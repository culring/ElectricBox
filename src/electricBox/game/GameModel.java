package electricBox.game;

import java.util.LinkedList;

/**
 * Created by Culring on 2017-06-06.
 */
public class GameModel {
    public enum GameState {
        EDITTING,
        RUNNING,
        FINISHED
    }
    public enum GameObjectType {
        CURRENT_GENERATOR,
        CURRENT_RECEIVER,
        WIRE,
        PROPELLER,
        WIND_TURBINE,
        LASER,
        LASER_DETECTOR,
        MIRROR
    }
    public enum BoardType {
        GAME_BOARD(0),
        INVENTORY_BOARD(1);

        private int value;

        BoardType(int value){
            this.value = value;
        }

        int getValue(){
            return value;
        }
    }
    private class Coordinations {
        int x, y;
    }

    private Board inventoryBoard;
    private GameBoard gameBoard;
    private Board boards[];
    private GameState gameState;

    {
        inventoryBoard = new Board(6, 3);
        gameBoard = new GameBoard(9, 9);
        boards = new Board[2];
        boards[BoardType.GAME_BOARD.getValue()] = gameBoard;
        boards[BoardType.INVENTORY_BOARD.getValue()] = inventoryBoard;
        gameState = GameState.EDITTING;
    }

    // return 0 if a transaction will end up with a success,
    // otherwise 1
    int createObject(GameObjectType gameObjectType, BoardType boardType, int x, int y) throws Exception {
        GameObject gameComponent;

        switch (gameObjectType) {
            case CURRENT_GENERATOR:
                gameComponent = new CurrentGenerator();
                break;

            case CURRENT_RECEIVER:
                gameComponent = new CurrentReceiver();
                break;

            case WIRE:
                gameComponent = new Wire();
                break;

            default:
                throw new Exception("This game object hasn't been implemented yet");
        }

        // if tried to put newly created object
        // on an already occupied place
        if (boards[boardType.getValue()].doesCollide(gameComponent, x, y)) {
            throw new Exception("Tried to put object on an already occupied place. Position: ("+x+","+y+").");
        }
        boards[boardType.getValue()].set(gameComponent, x, y);

        return 0;
    }

    // return true if object was successfully moved,
    // otherwise 0
    public boolean move(BoardType sourceBoardType, int sourceX, int sourceY,
                        BoardType destBoardType, int destX, int destY) {
        int sourceBoard = sourceBoardType.getValue();
        int destBoard = destBoardType.getValue();

        // if gameState is already running or finished, or
        // object is not movable, move operation cannot be proceeded
        if (gameState == GameState.RUNNING || gameState == GameState.FINISHED ||
                !boards[sourceBoard].board[sourceX][sourceY].isMovable()) {
            return false;
        }

        GameObject collisionObject = boards[sourceBoard].board[sourceX][sourceY];
        if (!boards[destBoard].doesCollide(collisionObject, destX, destY)) {
            boards[sourceBoard].remove(sourceX, sourceY);
            boards[destBoard].set(collisionObject, destX, destY);

            return true;
        }
        return false;
    }

    public void startGame() {
        gameState = GameState.RUNNING;
    }

    public void power(int x, int y){
        System.out.println("Model power");
        gameBoard.power(x, y);
    }

    public void addListener(Listener listener){
        gameBoard.addListener(listener);
    }

    protected enum Rotation{
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
    private abstract class GameObject {
        protected Rotation rotation;
        protected boolean isRotatable;
        protected boolean isMovable;
        protected boolean isActivated;
        protected int width, height;

        {
            rotation = Rotation.RIGHT;
            isRotatable = false;
            isMovable = false;
            isActivated = false;
            width = height = 1;
        }

        public abstract void power(int x, int y);
        public void rotateClockwise(){
            rotation = Rotation.values()[(rotation.ordinal()-1)%4];
        }
        public void rotateCounterclockwise(){
            rotation = Rotation.values()[(rotation.ordinal()+1)%4];
        }

        public boolean isRotatable(){
            return isRotatable;
        }
        public boolean isMovable(){
            return isMovable;
        }
        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }

        public void activate(){ isActivated = true; }
        public void setMovable(){
            isMovable = true;
        }
        public void setWidth(int width){
            this.width = width;
        }
        public void setHeight(int height){
            this.height = height;
        }
        public void setRotatable(){
            isRotatable = true;
        }
    }

    // old definitions of game objects
    private class CurrentGenerator extends GameObject{
        CurrentGenerator(){
            height = 1;
            width = 1;
            isMovable = true;
        }

        public void power(int x, int y){
            gameBoard.powerStar(x, y);
        }
    }

    private class CurrentReceiver extends GameObject{
        CurrentReceiver(){
            height = 1;
            width = 1;
            isMovable = true;
        }

        public void power(int x, int y){
            gameBoard.powerStar(x, y);
        }
    }

    private class Wire extends GameObject{
        Wire(){
            height = 1;
            width = 1;
            isMovable = true;
        }

        public void power(int x, int y){
            gameBoard.powerStar(x, y);
        }
    }

    //private class Propeller extends GameComponent{
    //    Propeller(){
    //        super(true);
    //
    //        height = 2;
    //        width = 1;
    //    }
    //}
    //
    //private class WindTurbine extends GameComponent{
    //    WindTurbine(){
    //        super(true);
    //
    //        height = 1;
    //        width = 1;
    //    }
    //}
    //
    //private class Laser extends GameComponent{
    //    Laser(){
    //        super(true);
    //
    //        height = 1;
    //        width = 1;
    //    }
    //}
    //
    //private class LaserDetector extends GameComponent{
    //    LaserDetector(){
    //        super(false);
    //
    //        height = 1;
    //        width = 1;
    //    }
    //}
    //
    //private class Mirror extends GameComponent{
    //    Mirror(){
    //        super(false);
    //
    //        height = 1;
    //        width = 1;
    //    }
    //}

    private class Board {
        protected GameObject board[][];
        protected int height, width;

        Board(int height, int width){
            this.height = height;
            this.width = width;
            this.board = new GameObject[this.height][this.width];
        }

        public int getHeight() {
            return height;
        }
        public int getWidth() {
            return width;
        }

        public void set(GameObject gameComponent, int x, int y) {
            int collisionWidth = gameComponent.getWidth();
            int collisionHeight = gameComponent.getHeight();
            for (int i = 0; i < collisionWidth; i++) {
                for (int j = 0; j < collisionHeight; j++) {
                    board[x + i][y + j] = gameComponent;
                }
            }
        }

        public boolean doesCollide(GameObject gameObject, int x, int y) {
            int collisionWdith = gameObject.getWidth();
            int collisionHeight = gameObject.getHeight();

            for (int i = 0; i < collisionWdith; ++i) {
                for (int j = 0; j < collisionHeight; j++) {
                    if (x + i >= width || y + j >= height ||
                            (board[x + i][y + j] != null && board[x + i][y + j] != gameObject)) {
                        return true;
                    }
                }
            }

            return false;
        }

        public void remove(int x, int y) {
            GameObject gameObject = board[x][y];
            int collisionWidth = gameObject.getWidth();
            int collisionHeight = gameObject.getHeight();

            for (int i = 0; i < collisionWidth; ++i) {
                for (int j = 0; j < collisionHeight; j++) {
                    board[x + i][y + j] = null;
                }
            }
        }

        public GameObject getObject(int x, int y){
            return board[x][y];
        }
    }

    private class GameBoard extends Board{
        protected LinkedList<Listener> listeners;

        {
            listeners = new LinkedList<Listener>();
        }
        GameBoard(int height, int width) {
            super(height, width);
        }

        public void addListener(Listener listener){
            listeners.add(listener);
        }

        public void power(int x, int y){
            board[x][y].power(x, y);
        }

        public void powerStar(int x, int y){
            if(x < 0 || y < 0 || x >= width || y >= height ||
                    board[x][y] == null ||
                    (board[x][y] != null && board[x][y].isActivated)){
                return;
            }
            System.out.println("Star");
            board[x][y].activate();
            for(Listener listener : listeners){
                listener.power(x, y);
            }
            powerStar(x-1, y);
            powerStar(x, y+1);
            powerStar(x+1, y);
            powerStar(x, y-1);
        }
    }

    interface Listener{
        void power(int x, int y);
    }
}
