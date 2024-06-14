import bagel.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map;


/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2024
 *
 * Please enter your name below
 * @author Wingyee He
 */
public class ShadowMario extends AbstractGame {
    private final Image BACKGROUND_IMAGE;

    //stores all message text and font for start and end page of the Game
    Map<String, GameMessage> allMessages = new HashMap<>();

    //boolean values that manage the start and end of  the game
    private boolean gameStartFlag = false, gameEndFlag = false, gameWinFlag = false;
    private Map<String, GameLevel> allGameLevels = new HashMap<>();


    /**
     * The constructor of the class ShadowMario.
     * Description: This is a 2D game that constructed based on the java bagel class. The game currently has three
     *              game levels.
     * @param game_props This is a file that contains information to construct the GameEntities in the game
     * @param message_props This is a file that contains information to construct the GameMessages in the game
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
              Integer.parseInt(game_props.getProperty("windowHeight")),
              message_props.getProperty("title"));

        //There are currently 3 game levels of the ShadowMario for user to choose
        allGameLevels.put("level1", new GameLevel(1, game_props, message_props));
        allGameLevels.put("level2", new GameLevel(2, game_props, message_props));
        allGameLevels.put("level3", new GameLevel(3, game_props, message_props));

        //below are image and message texts and fonts that draws the start and the end page of the game
        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        allMessages.put("title", new GameMessage("title", message_props, game_props));
        allMessages.put("instruction", new GameMessage("instruction", message_props, game_props));
        allMessages.put("gameOver", new GameMessage("gameOver", message_props, game_props));
        allMessages.put("gameWon", new GameMessage("gameWon", message_props, game_props));

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update of the selected level.
     * Allows the game to exit when the escape key is pressed.
     * Handle screen navigation between levels and instruction pages here.
     */
    @Override
    protected void update(Input input) {

        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        /*
         * set up that game level according to the keyboard input from user
         */
        if (!gameStartFlag && input.wasPressed(Keys.NUM_1)){
            allGameLevels.get("level1").setOn(true);
            gameStartFlag = true;
            allGameLevels.get("level1").set();
        } else if (!gameStartFlag && input.wasPressed(Keys.NUM_2)){
            allGameLevels.get("level2").setOn(true);
            gameStartFlag = true;
            allGameLevels.get("level2").set();
        } else if (!gameStartFlag && input.wasPressed(Keys.NUM_3)){
            allGameLevels.get("level3").setOn(true);
            gameStartFlag = true;
            allGameLevels.get("level3").set();
        }

        /*
         * switch back from game end page to the game start page when user pressed SPACE
         */
        if (input.wasPressed(Keys.SPACE)){
            /*if a game just ended, the user is looking to reset the game
             * reset flag status, so that START page can be shown
             */
            if (gameStartFlag == true && gameEndFlag == true) {
                gameStartFlag = false;
                gameEndFlag = false;
            }

        }

        /*
         * draw the background, START page, END page when player lost/won according to the flag attributes
         */
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!gameStartFlag){
            drawSTARTPAGE();
        }

        if (gameStartFlag && gameEndFlag && gameWinFlag) {
            drawENDWINPAGE();
        }
        if (gameStartFlag && gameEndFlag && !gameWinFlag) {
            drawENDLOSEPAGE();
        }

        /**
         * if game is on, depending on which game level the player chose, respond to player's keyboard input accordingly
         */
        if (gameStartFlag && !gameEndFlag) {
            if(allGameLevels.get("level1").isOn()){
                allGameLevels.get("level1").play(input);
            } else if (allGameLevels.get("level2").isOn()){
                allGameLevels.get("level2").play(input);
            } else if (allGameLevels.get("level3").isOn()){
                allGameLevels.get("level3").play(input);
            }
        }

        /*
         * if game is on, keep an eye on whether it had ended or not, and whether the player had won or lost.
         * The update of flag attribute is done by passing the gameLevel's flag attribute out
         */
        for (Map.Entry<String, GameLevel> entry: allGameLevels.entrySet()) {
            GameLevel level = entry.getValue();
            if (level.isOn()) {
                gameEndFlag = level.isGameEnd();
                gameWinFlag = level.isGameWon();
                if (level.isGameEnd()) {
                    //if a game ended, set the level's ON status to be false
                    level.setOn(false);
                }
            }
        }

    }

    private void drawSTARTPAGE(){
        allMessages.get("title").drawMessage();
        allMessages.get("instruction").drawMessage();
    }
    private void drawENDWINPAGE(){
        allMessages.get("gameWon").drawMessage();
    }
    private void drawENDLOSEPAGE(){
        allMessages.get("gameOver").drawMessage();
    }
}
