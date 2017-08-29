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

    {
        _boardSpaces = new Rectangle[2][][];
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
        Pane pane = new Pane();
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

        pane.getChildren().add(grid);
        layout.getChildren().add(pane);

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

    private class Sprite {
        private int _x, _y;
        private Board _board;
        private BoardImageView _imageView;

        public Sprite(SpriteType spriteType, Board board, int x, int y){
            _board = board;
            _x = x;
            _y = y;

            Image image = new Image(spriteType.getImageNotActivatedPath(), spriteType.getWidth(), spriteType.getHeight(), false, false);
            _imageView = new BoardImageView();
            _imageView.setImage(image);

            move(board, x, y);

            _layout.getChildren().add(_imageView);

            if(_board == Board.INVENTORY_BOARD) {
                addDraggingHandlers();
            }
        }

        public void move(Board board, int x, int y){
            System.out.println(x + " | " + y);

            Rectangle space = _boardSpaces[board.getValue()][x][y];
            Bounds bounds = space.localToScene(space.getBoundsInLocal());
            double newTranslateX = bounds.getMinX();
            double newTranslateY = bounds.getMinY();
            _imageView.setTranslateX(newTranslateX);
            _imageView.setTranslateY(newTranslateY);
        }

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

            _imageView.setOnDragDetected(event -> {
                _imageView.startFullDrag();
            });

            _imageView.setOnMousePressed(new DraggingHandler<MouseEvent>(_x, _y) {
                @Override
                public void handle(MouseEvent event) {
                    _imageView.setMouseTransparent(true);

                    orgSceneX = event.getSceneX();
                    orgSceneY = event.getSceneY();

                    orgTranslateX = orgSceneX;
                    orgTranslateY = orgSceneY;
                }
            });

            _imageView.setOnMouseReleased((MouseEvent event) -> {
                _imageView.setMouseTransparent(false);

                if(!_imageView.isMoved){
                    _imageView.cancelDrag();
                }
                else{
                    if(testIsModelAcceptingMove.test(_board.getValue(), _x, _y,
                            _imageView.newBoard.getValue(), _imageView.newBoardX, _imageView.newBoardY)){
                        _imageView.confirmDrag();
                        _x = _imageView.newBoardX;
                        _y = _imageView.newBoardY;
                        _board = _imageView.newBoard;
                    }
                    else{
                        _imageView.cancelDrag();
                    }
                }
            });

            _imageView.setOnMouseDragged(new DraggingHandler<MouseEvent>(_x, _y) {
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
    }

    public void createSprite(SpriteType spriteType, Board board, int x, int y){
        new Sprite(spriteType, board, x, y);
    }

    interface TestIsModelAcceptingMove{
        boolean test(int sourceBoard, int sourceX, int sourceY,
                            int destBoard, int destX, int destY);
    }
    TestIsModelAcceptingMove testIsModelAcceptingMove;

    public enum SpriteType{
        CURRENT_GENERATOR(48, 48,
                "electricBox/game/graphics/current_generator_not_activated.png",
                "electricBox/game/graphics/current_generator_activated.png"),
        CURRENT_RECEIVER(48, 48,
                "electricBox/game/graphics/current_receiver_not_activated.png",
                "current_receiver_activated.png"),
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