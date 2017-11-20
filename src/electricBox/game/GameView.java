package electricBox.game;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-05-31.
 */
public class GameView {
    private final int _levelNumber;
    private Stage _primaryStage;
    private Pane _layout;
    private PaneButton _menuButton, _levelButton;
    private Rectangle[][][] _boardSpaces;
    // a sprite array in a game board
    private Sprite sprite[][];
    Listener listener;

    {
        _boardSpaces = new Rectangle[2][][];
        sprite = new Sprite[Board.GAME_BOARD.getWidth()][Board.GAME_BOARD.getHeight()];
    }

    public GameView(Stage primaryStage, int levelNumber){
        _primaryStage = primaryStage;
        _levelNumber = levelNumber;

        primaryStage.setTitle("Level " + Integer.toString(levelNumber));

        _layout = setLayout();
        Scene scene = new Scene(_layout, 720, 540);
        scene.getStylesheets().add("electricBox/game/GameStylesheet.css");

        _primaryStage.setScene(scene);
    }

    private Pane setLayout(){
        Pane layout = new Pane();
        GridPane grid = new GridPane();
        grid.setVgap(11);
        grid.setHgap(11);
        grid.setLayoutX(25);
        grid.setLayoutY(25);

        grid.add(setTitleLayout(), 0, 0);
        grid.add(setGameLayout(), 0, 1);
        grid.add(setInventoryBoxLayout(), 1, 0);
        grid.add(setInventoryLayout(), 1, 1);
        grid.add(setOptionsLayout(), 2, 1);

        layout.getChildren().add(grid);

        return layout;
    }

    private BorderPane setTitleLayout(){
        BorderPane titleBorderPane = new BorderPane();
        Text electricBoxText = new Text("Electric Box");
        HideHelpButton hideHelpButton = new HideHelpButton("HideHelpButton");
        titleBorderPane.setLeft(electricBoxText);
        titleBorderPane.setRight(hideHelpButton);

        electricBoxText.setId("electricBoxText");
        hideHelpButton.setId("hideHelpButton");

        return titleBorderPane;
    }

    private GridPane setGameLayout(){
        GridPane gameGridPane = new GridPane();
        gameGridPane.setVgap(2);
        gameGridPane.setHgap(2);

        ImageView [][] imageViews = createGrid(9, 9, gameGridPane, Board.GAME_BOARD);

        return gameGridPane;
    }

    private ImageView[][] createGrid(int rows, int columns, GridPane grid, Board board){
        ImageView [][] imageViews = new ImageView[columns][rows];

        class Space extends Rectangle{
            private int _boardX, _boardY;
            private Board _board;

            Space(int width, int height, int x, int y, Board board){
                super(width, height);
                _boardX = x;
                _boardY = y;
                _board = board;

                setOnMouseDragEntered(event -> {
                    BoardImageView imageView = (BoardImageView)(event.getGestureSource());
                    Bounds boundsInScene = localToScene(getBoundsInLocal());
                    imageView.newX = boundsInScene.getMinX();
                    imageView.newY = boundsInScene.getMinY();
                    imageView.newBoardX = _boardX;
                    imageView.newBoardY = _boardY;
                    imageView.newBoard = _board;
                    imageView.isMoved = true;
                });

                setOnMouseDragExited(event ->{
                    BoardImageView imageView = (BoardImageView) (event.getGestureSource());
                    imageView.isMoved = false;
                });
            }
        }

        _boardSpaces[board.getValue()] = new Rectangle[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Space space = new Space(48, 48, i, j, board);
                _boardSpaces[board.getValue()][i][j] = space;
                space.getStyleClass().add("space");
                grid.add(space, i, j);
            }
        }

        return imageViews;
    }

    private FlowPane setInventoryBoxLayout(){
        FlowPane inventoryBox = new FlowPane();
        Text inventoryText = new Text("Inventory");

        inventoryBox.getChildren().add(inventoryText);

        inventoryBox.setId("inventoryBox");
        inventoryBox.getStyleClass().add("infoBox");
        inventoryText.setId("inventoryText");

        return inventoryBox;
    }

    private BorderPane setInventoryLayout(){
        BorderPane borderPane = new BorderPane();
        FlowPane flowPane = new FlowPane();
        GridPane grid = new GridPane();

        ImageView [][] imageViews = createGrid(6, 3, grid, Board.INVENTORY_BOARD);

        grid.setVgap(2);
        grid.setHgap(2);
        flowPane.getChildren().add(grid);

        FlowPane hintBox = new FlowPane();

        borderPane.setTop(flowPane);
        borderPane.setBottom(hintBox);

        flowPane.setId("inventory");
        flowPane.getStyleClass().add("infoBox");
        hintBox.getStyleClass().add("infoBox");
        hintBox.setId("hintBox");

        return borderPane;
    }

    private BorderPane setOptionsLayout(){
        BorderPane borderPane = new BorderPane();
        GridPane optionsGrid = new GridPane();
        optionsGrid.setVgap(3);

        _menuButton = new PaneButton();
        _levelButton = new PaneButton();

        Text menuText = new Text("Menu");
        Text levelText = new Text("Levels");

        _menuButton.getChildren().add(menuText);
        _levelButton.getChildren().add(levelText);

        optionsGrid.add(_menuButton, 0, 0);
        optionsGrid.add(_levelButton, 0, 1);

        Text levelNumberText = new Text("Level\n" + Integer.toString(_levelNumber));
        FlowPane levelBox = new FlowPane();
        levelBox.getChildren().add(levelNumberText);

        borderPane.setTop(optionsGrid);
        borderPane.setBottom(levelBox);

        _menuButton.getStyleClass().add("optionBox");
        _levelButton.getStyleClass().add("optionBox");
        levelBox.getStyleClass().add("optionBox");

        return borderPane;
    }

    public void setMenuButton(EventHandler<MouseEvent> e){
        _menuButton.setOnMouseClicked(e);
    }

    public void setLevelButton(EventHandler<MouseEvent> e){
        _levelButton.setOnMouseClicked(e);
    }

    public void setDragHandler(TestIsModelAcceptingMove handler){
        testIsModelAcceptingMove = handler;
    }

    private class HideHelpButton extends Button{
        {
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                }
            });
            setVisible(false);
        }
        HideHelpButton(String s){
            super(s);
        }
    }

    private class PaneButton extends FlowPane{
        {
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setStyle("-fx-background-color: rgba(170, 170, 170, 1);");
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setStyle("-fx-background-color: rgba(140, 140, 140, 1);");
                }
            });
        }
    }

    public enum Board{
        GAME_BOARD(0, 9, 9),
        INVENTORY_BOARD(1, 3, 6);

        private int _value, _width, _height;

        Board(int number, int width, int height){
            _value = number;
            _width = width;
            _height = height;
        }

        int getValue(){
            return _value;
        }
        int getWidth(){
            return _width;
        }
        int getHeight(){
            return _height;
        }
    }

    private class BoardImageView extends ImageView{
        public double newX, newY;
        public double oldX, oldY;
        public boolean isMoved;
        public int newBoardX, newBoardY;
        public Board newBoard;

        BoardImageView(double x, double y){
            oldX = x;
            oldY = y;
        }

        public void cancelDrag(){
            setTranslateX(oldX);
            setTranslateY(oldY);
        }

        public void confirmDrag(){
            setTranslateX(newX);
            setTranslateY(newY);

            oldX = newX;
            oldY = newY;

            isMoved = false;
        }
    }

    interface Listener{
        void clicked(int x, int y);
    }

    public void addListener(Listener listener){
        this.listener = listener;
    }

    private class Sprite {
        private int x, y;
        private Board _board;
        private BoardImageView imageView;
        private SpriteType spriteType;

        public Sprite(SpriteType spriteType, Board board, int x, int y){
            _board = board;
            this.x = x;
            this.y = y;
            this.spriteType = spriteType;
            sprite[x][y] = this;

            Image image = new Image(spriteType.getImageNotActivatedPath(), spriteType.getWidth(), spriteType.getHeight(), false, false);
            Position position = getPosition(board, x, y);
            imageView = new BoardImageView(position.x, position.y);
            imageView.setImage(image);

            move(board, x, y);

            _layout.getChildren().add(imageView);

            addDraggingHandlers();
            //addClickHandler();
        }

        public void move(Board board, int x, int y){
            System.out.println(x + " | " + y);

            Position position = getPosition(board, x, y);
            imageView.setTranslateX(position.x);
            imageView.setTranslateY(position.y);

            sprite[this.x][this.y] = null;
            sprite[x][y] = this;
        }

        public Position getPosition(Board board, int x, int y){
            Rectangle space = _boardSpaces[board.getValue()][x][y];
            Bounds bounds = space.localToScene(space.getBoundsInLocal());
            Position position = new Position();
            position.x = bounds.getMinX();
            position.y = bounds.getMinY();
            return position;
        }

        private class Position{
            public double x, y;
        }

        //private void addClickHandler(){
        //    abstract class ClickHandler implements EventHandler<MouseEvent>{
        //        private int x, y;
        //
        //        ClickHandler(int x, int y){
        //            this.x = x;
        //            this.y = y;
        //        }
        //    }
        //
        //    imageView.setOnMouseClicked(new ClickHandler(this.x, this.y) {
        //        @Override
        //        public void handle(MouseEvent event) {
        //            System.out.println("Click!");
        //            listener.clicked(x, y);
        //        }
        //    });
        //}

        private void addDraggingHandlers(){
            abstract class DraggingHandler<T extends Event> implements EventHandler<T>{
                protected int _x, _y;
                protected double orgSceneX, orgSceneY;
                protected double orgTranslateX, orgTranslateY;

                DraggingHandler(int x, int y){
                    _x = x;
                    _y = y;
                }

                abstract public void handle(T event);
            }

            imageView.setOnDragDetected(event -> {
                imageView.startFullDrag();
            });

            imageView.setOnMousePressed(new DraggingHandler<MouseEvent>(x, y) {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("Press!");
                    imageView.setMouseTransparent(true);

                    orgSceneX = event.getSceneX();
                    orgSceneY = event.getSceneY();

                    orgTranslateX = orgSceneX;
                    orgTranslateY = orgSceneY;
                }
            });

            imageView.setOnMouseReleased((MouseEvent event) -> {
                imageView.setMouseTransparent(false);

                if(!imageView.isMoved){
                    imageView.cancelDrag();
                    System.out.println("Click!");
                    if(listener != null){
                        listener.clicked(x, y);
                    }
                }
                else{
                    try {
                        if (testIsModelAcceptingMove.test(_board.getValue(), x, y,
                                imageView.newBoard.getValue(), imageView.newBoardX, imageView.newBoardY)) {
                            imageView.confirmDrag();
                            x = imageView.newBoardX;
                            y = imageView.newBoardY;
                            _board = imageView.newBoard;
                        } else {
                            imageView.cancelDrag();
                        }
                    }
                    catch(Exception e){
                    }
                }
            });

            imageView.setOnMouseDragged(new DraggingHandler<MouseEvent>(x, y) {
                @Override
                public void handle(MouseEvent event) {
                    double offsetX = event.getSceneX() - orgSceneX;
                    double offsetY = event.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((ImageView)(event.getSource())).setTranslateX(newTranslateX);
                    ((ImageView)(event.getSource())).setTranslateY(newTranslateY);
                }
            });
        }

        public void power(){
            Image newImage = new Image(spriteType.getImageActivatedPath(), spriteType.getWidth(),
                    spriteType.getHeight(), false, false);
            this.imageView.setImage(newImage);
        }
    }

    public void createSprite(SpriteType spriteType, Board board, int x, int y){
        new Sprite(spriteType, board, x, y);
    }

    public void power(int x, int y){
        System.out.println("(x,y)="+x+","+y+" powered");
        sprite[x][y].power();
    }

    interface TestIsModelAcceptingMove{
        boolean test(int sourceBoard, int sourceX, int sourceY,
                            int destBoard, int destX, int destY) throws Exception;
    }
    TestIsModelAcceptingMove testIsModelAcceptingMove;

    public enum SpriteType{
        CURRENT_GENERATOR(48, 48,
                "electricBox/game/graphics/current_generator_not_activated.png",
                "electricBox/game/graphics/current_generator_activated.png"),
        CURRENT_RECEIVER(48, 48,
                "electricBox/game/graphics/current_receiver_not_activated.png",
                "electricBox/game/graphics/current_receiver_activated.png"),
        WIRE(48, 48,
                "electricBox/game/graphics/wire_not_activated.png",
                "electricBox/game/graphics/wire_activated.png"),
        PROPELLER(48, 2*48 + 2,
                "electricBox/game/graphics/propeller.png",
                null);

        private double _width, _height;
        private String _imageNotActivated, _imageActivated;

        SpriteType(int width, int height, String imageNotActivated, String imageActivated){
            _height = height;
            _width = width;
            _imageNotActivated = imageNotActivated;
            _imageActivated = imageActivated;
        }

        public double getWidth(){
            return _width;
        }
        public double getHeight(){
            return _height;
        }

        public String getImageNotActivatedPath(){
            return _imageNotActivated;
        }
        public String getImageActivatedPath(){
            return _imageActivated;
        }

    }
}