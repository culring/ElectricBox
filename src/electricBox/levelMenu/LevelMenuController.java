package electricBox.levelMenu;

import electricBox.game.GameController;
import electricBox.mainMenu.MainMenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Culring on 2017-06-09.
 */
public class LevelMenuController {
    private Stage primaryStage;
    private LevelMenuView view;

    private static int MAX_LEVEL = 10;
    private static String UNLOCKED_LEVEL_FILENAME = new String("unlockedLevel.info");

    public LevelMenuController(Stage primaryStage){
        this.primaryStage = primaryStage;

        //int unlockedLevel = getUnlockedLevel();
        int unlockedLevel = 1;
        this.view = new LevelMenuView(this.primaryStage, MAX_LEVEL, unlockedLevel);
        this.view.setBackButton(event -> {
            new MainMenuController(this.primaryStage);
        });
        for (int i = 1; i <= unlockedLevel; ++i) {
            this.view.setLevelButton(i, new LevelButtonHandler(i, true));
        }
        for (int i = unlockedLevel+1; i <= MAX_LEVEL; ++i) {
            this.view.setLevelButton(i, new LevelButtonHandler(i, false));
        }
    }

    private int getUnlockedLevel(){
        int maxUnlockedLevel;
        try{
            FileInputStream fileInputStream = new FileInputStream(UNLOCKED_LEVEL_FILENAME);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            if(dataInputStream.available() >= 4)
                maxUnlockedLevel = dataInputStream.readInt();
            else{
                maxUnlockedLevel = 1;
                setUnlockedLevel(maxUnlockedLevel);
            }
            dataInputStream.close();
            fileInputStream.close();
        }
        catch(java.io.IOException e){
            maxUnlockedLevel = 1;
        }
        return maxUnlockedLevel;
    }

    private void setUnlockedLevel(int level){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(UNLOCKED_LEVEL_FILENAME);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeInt(level);
            dataOutputStream.close();
            fileOutputStream.close();
        }
        catch(java.io.IOException e){
            System.out.println("Couldn't write to file");
            System.exit(1);
        }
    }

    private class LevelButtonHandler implements EventHandler<ActionEvent>{
        private int _number;
        private boolean _isUnlocked;

        public LevelButtonHandler(int number, boolean isUnlocked){
            _number = number;
            _isUnlocked = isUnlocked;
        }

        @Override
        public void handle(ActionEvent e){
            if(_isUnlocked) {
                new GameController(primaryStage, _number);
            }
        }
    }
}
