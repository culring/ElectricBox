package electricBox.levelMenu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Culring on 2017-05-31.
 */
public class LevelMenuView {
    private Stage primaryStage;
    private Button backButton, levelButtons[];

    public LevelMenuView(Stage primaryStage, int maxLevel, int maxUnlockedLevel) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Level menu");

        StackPane root = new StackPane();
        HBox hboxLevels = new HBox();
        hboxLevels.setSpacing(2);
        hboxLevels.setAlignment(Pos.CENTER);

        this.levelButtons = new Button[10];
        for(int i = 1; i <= maxUnlockedLevel; ++i) {
            this.levelButtons[i-1] = new LevelButton(i);
            hboxLevels.getChildren().add(this.levelButtons[i-1]);
        }
        for(int i = maxUnlockedLevel + 1; i <= maxLevel; ++i) {
            this.levelButtons[i-1] = new LevelButton("??");
            hboxLevels.getChildren().add(this.levelButtons[i-1]);
        }
        this.backButton = new BackButton();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(2);
        vbox.getChildren().addAll(hboxLevels, backButton);
        root.getChildren().add(vbox);

        this.primaryStage.setScene(new Scene(root, 720, 540));
    }

    void setBackButton(EventHandler<ActionEvent> e){
        this.backButton.setOnAction(e);
    }

    void setLevelButton(int level, EventHandler<ActionEvent> e){
        this.levelButtons[level-1].setOnAction(e);
    }

    private class BackButton extends Button {
        BackButton(){
            super("Back");
        }
    }

    private class LevelButton extends Button {
        {
            setPrefHeight(48);
            setPrefWidth(48);
        }

        LevelButton(int levelNumber) {
            super(Integer.toString(levelNumber));
        }

        LevelButton(String s){
            super(s);
        }
    }
}
