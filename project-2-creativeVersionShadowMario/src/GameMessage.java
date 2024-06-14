import bagel.*;
import java.util.Properties;

/**
 * Description: GameMessage is message that will render on the screen and intended to guide the user how to play
 * the game
 */
public class GameMessage {
    private final double X;
    private final double Y;
    private final String TEXT;
    private final Font FONT;

    /**
     * This is a constructor that constructs an instance of the class GameMessage
     *
     * @param prompt This is a text that helps to get the message string stored in the message_props fild
     * @param message_props This is a file that stores all message text
     * @param game_props This is a file that stores the X and Y of the position of the message on the screen, and the
     *                   font of the message
     */
    public GameMessage(String prompt, Properties message_props, Properties game_props) {


        if (prompt.contains("playerHealth") || prompt.contains("enemyBossHealth")){
            //playerHealth and enemyBossHealth shares the same message text
            this.TEXT = message_props.getProperty("health");
        } else {
            //get the message text using the prompt
            this.TEXT = message_props.getProperty(prompt);
        }

        if (prompt.contains("gameWon") || prompt.contains("gameOver")){
            /*prompt 'gameWon' and 'gameOver' has same FONT and position as 'message'
              since their FONT and position had not stored separately in the file, change the prompt
              so that they have correct position and FONT.
             */
            prompt = "message";
        }

        //set the FONT for the gameMessage using the prompt
        this.FONT = new Font(game_props.getProperty("font")
                            , Integer.parseInt(game_props.getProperty(prompt+".fontSize")));

        //centralise message according to the Project instruction
        if (prompt.contains("message") ){
            this.X = Window.getWidth() / 2 - this.FONT.getWidth(this.TEXT)/2;
        } else if (prompt.contains("instruction")) {
            this.X = Window.getWidth() / 2 - this.FONT.getWidth(this.TEXT)/2;
        }else {
            this.X = Double.parseDouble(game_props.getProperty(prompt+".x"));
        }

        this.Y = Double.parseDouble(game_props.getProperty(prompt+".y"));

    }



    /**
     * This method render the message text on the screen using its current position
     */
    public void drawMessage(){
        this.FONT.drawString(this.TEXT, this.X, this.Y);
    }

    /**
     * This method render the message text and a value on the screen using its current position. It is used for printing
     * the score and healthPoint of GameEntities.
     *
     * @param value This is a figure that associated with the score/healthPoint of an GameEntity
     */
    public void drawMessage(int value){
        this.FONT.drawString(this.TEXT + " " + value, this.X, this.Y);
    }

    /**
     * This method render the message text and additional information. It is used for printing the record of gameWon
     * elapsed time.
     * @param recordMessage This is a string that contains minute/second value
     */
    public void drawRecordMessage(String recordMessage){
        this.FONT.drawString(this.TEXT + " " + recordMessage, this.X, this.Y);
    }

    /**
     * This method render the message text and additional information. It is used for printing the record of gameWon
     * elapsed time.
     * @param recordMessage This is a string that contains minute/second value
     * @param option This is a draw option that may colour the string that we are printing
     */
    public void drawRecordMessage(String recordMessage, DrawOptions option){
        this.FONT.drawString(this.TEXT + " " + recordMessage, this.X, this.Y, option);
    }


    /**
     * This method render the message text and a value on the screen in a wanted color using its current position.
     * It is used for printing the healthPoint of GameEntities.
     *
     * @param value This is a figure that associated with the score/healthPoint of an GameEntity
     * @param colorOption This is a customised drawing option, e.g draw the string in color red
     */
    public void drawMessage(int value, DrawOptions colorOption){
        this.FONT.drawString(this.TEXT + " " + value, this.X, this.Y, colorOption);
    }
    /**
     * This method render the message text on the screen using set drawoption
     *
     * @param colorOption This is a customised drawing option, e.g draw the string in color red, rotate the string by
     *                    pi/2
     */
    public void drawMessage(DrawOptions colorOption){
        this.FONT.drawString(this.TEXT, this.X, this.Y, colorOption);
    }

}
