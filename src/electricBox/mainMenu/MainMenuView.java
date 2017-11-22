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
    private Stage primaryStage;
    private Button playButton, exitButton;

    public MainMenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Main menu");

        BorderPane root = new BorderPane();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699");
        this.playButton = new PlayButton();
        this.exitButton = new ExitButton();
        hbox.getChildren().addAll(this.playButton, this.exitButton);
        hbox.setAlignment(Pos.CENTER);

        root.setCenter(hbox);

        this.primaryStage.setScene(new Scene(root, 720, 540));
        this.primaryStage.show();
    }

    public void setPlayButton(EventHandler<ActionEvent> e){
        this.playButton.setOnAction(e);
    }

    public void setExitButton(EventHandler<ActionEvent> e){
        this.exitButton.setOnAction(e);
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
