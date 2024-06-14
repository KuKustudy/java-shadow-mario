import bagel.Image;

import java.util.Properties;

/**
 * Description: DoubleScorePower is a "gift" that can be collected by player in the game, once collected, for a
 *              period of time (next 500 frames), every coin collected by player, its value will double up
 */
public class DoubleScorePower extends GameEntity implements Collectable{
    private final int MAXFRAMES;
    private final double DISAPPEAR_SPEED = -10.0;
    private boolean collectedStatus = false;

    /** This is a constructor that constructs an instance of the class DoubleScorePower, which is also a child
     * class of the abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of DoubleScorePower.
     *              Other info includes: HorizontalSpeed, Image, Radius, MAXFRAMES
     */
    public DoubleScorePower(double x, double y, Properties props) {
        super(x, y, props);
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.doubleScore.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.doubleScore.image")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.doubleScore.radius")));
        this.MAXFRAMES = Integer.parseInt(props.getProperty("gameObjects.doubleScore.maxFrames"));
    }

    /** This is a setter method that set the collected status of an instance of DoubleScorePower to be true.
     *  This method is used after detected that player has been collected (collided with) the DoubleScorePower
     *
     */
    public void hasBeenCollected(){
        this.collectedStatus = true;
    }

    /** This is a getter method that gets the collected status of the DoubleScorePower
     *
     * @return boolean This returns the collected status of the DoubleScorePower
     */
    public boolean isCollected() {
        return collectedStatus;
    }

    /** This is a getter method that gets the MAXFRAMES of the DoubleScorePower, this method is called to set
     * the period of time that the double score gift is activated
     *
     * @return int This returns the number of Frames the double score gift should be activated for, after collection
     */
    public int getMAXFRAMES() {
        return MAXFRAMES;
    }

    /** This is a method that moves the DoubleScorePower vertically up on the screen, this action should be implemented
     *  after the power has been collected by the player.
     */
    public void disappearOffScreen() {
        this.moveUp(this.DISAPPEAR_SPEED);
    }
}
