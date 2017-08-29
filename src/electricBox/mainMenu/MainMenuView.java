package electricBox.mainMenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-05-31.
 */
public class MainMenuView {
    private Stage _primaryStage;
    private Button _playButton, _exitButton;

    public MainMenuView(Stage primaryStage) {
        _primaryStage = primaryStage;

        primaryStage.setTitle("Main menu");

        BorderPane root = new BorderPane();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699");
        _playButton = new PlayButton();
        _exitButton = new ExitButton();
        hbox.getChildren().addAll(_playButton, _exitButton);
        hbox.setAlignment(Pos.CENTER);

        root.setCenter(hbox);

        _primaryStage.setScene(new Scene(root, 720, 540));
        _primaryStage.show();
    }

    public void setPlayButton(EventHandler<ActionEvent> e){
        _playButton.setOnAction(e);
    }

    public void setExitButton(EventHandler<ActionEvent> e){
        _exitButton.setOnAction(e);
    }

    private class PlayButton extends Button {
        public PlayButton() {
            super("Play");
        }
    }

    private class ExitButton extends Button {
        public ExitButton() {
            super("Exit");
        }
    }
}
