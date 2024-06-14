import java.util.Properties;

/**
 * Description: score is a figure that associated with the game entity Player, it increases when the player
 *              collected coins, and is increased by the value attached with the coin collected
 */
public class Score {
    private final double INITIAL_SCORE = 0.0;
    private double currentScore;

    /**
     * This is a default constructor that construct an instance of the class Score
     *
     *              At creation, the score should be set to zero
     */
    public Score (){
        this.currentScore = INITIAL_SCORE;
    }

    /**
     * This is a getter method
     * @return int This returns the currentScore of the player
     */
    public int getCurrentScore(){
        return (int)this.currentScore;
    }

    /**
     * This is a setter method that increase player's score by the value of the coin collected
     * @param value This is a figure that associated with the valuable gameEntity the player had collected
     */
    public void increaseScore(double value){
        this.currentScore += value;
    }

}
