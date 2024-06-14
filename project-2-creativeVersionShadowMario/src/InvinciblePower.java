import bagel.Image;
import java.util.Properties;

/**
 * Description: Invincibility is a gift that can be collected by the player by colliding with it, once collected,
 *              for a period of time (next 500 frames), player can collide with normal enemies without getting hurt
 *              (no healthPoint will be decreased).
 */
public class InvinciblePower extends GameEntity implements Collectable{
    private final int MAXFRAMES;
    private double DISAPPEAR_SPEED = -10.0;
    private boolean collectedStatus = false;


    /**
     * This is a constructor that constructs an instance of the class InvinciblePower, which is also a child class of
     * the abstract class GameEntity.
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of InvinciblePower.
     *              Other info includes: HorizontalSpeed, Image, Radius, MAXFRAMES
     */
    public InvinciblePower(double x, double y, Properties props) {
        super(x, y, props);
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.invinciblePower.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.invinciblePower.image")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.invinciblePower.radius")));
        this.MAXFRAMES = Integer.parseInt(props.getProperty("gameObjects.invinciblePower.maxFrames"));

    }


    /** This is a setter method that set the collected status of an instance of InvinciblePower to be true.
     *  This method is used after detected that player has been collected (collided with) the InvinciblePower
     *
     */
    public void hasBeenCollected(){
        this.collectedStatus = true;
    }

    /** This is a getter method that gets the collected status of the InvinciblePower
     *
     * @return boolean This returns the collected status of the InvinciblePower
     */
    public boolean isCollected() {
        return collectedStatus;
    }

    /** This is a getter method that gets the MAXFRAMES of the InvinciblePower, this method is called to set
     * the period of time that the Invincible gift should be activated for
     *
     * @return int This returns the number of Frames the Invincible gift should be activated for, after collection
     */
    public int getMAXFRAMES() {
        return MAXFRAMES;
    }

    /** This is a method that moves the InvinciblePower vertically up on the screen, this action should be implemented
     *  after the power has been collected by the player.
     */
    public void disappearOffScreen() {
        this.moveUp(this.DISAPPEAR_SPEED);
    }
}
