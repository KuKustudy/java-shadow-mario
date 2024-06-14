import bagel.Image;
import java.util.Properties;

/**
 * Coin can be collected by player in the game, once collected, its value will increase player's score
 */
public class Coin extends GameEntity implements Collectable{
    private double coinValue;
    private final double DISAPPEAR_SPEED = -10.0;
    private boolean collectedStatus = false;

    /**
     *  This is a constructor that creates an instance of the class Coin, which is also a child class
     *  of the abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of Coin. Other info includes:
     *              HorizontalSpeed, Image, Radius, coinValue
     *
     */
    public Coin(double x, double y, Properties props){
        super(x, y, props);
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.coin.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.coin.image")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.coin.radius")));
        this.coinValue = Double.parseDouble(props.getProperty("gameObjects.coin.value"));
    }

    /** This is a setter method that set a Coin's collected status to be true, this action should be implemented after
     * the player collided with the coin.
     */
    public void hasBeenCollected(){
        this.collectedStatus = true;
    }

    /** This is a getter method that returns the value of the Coin
     *
     * @return double This returns the value attached with the coin
     */
    public double getCoinValue(){ return this.coinValue;}

    /** This is a getter method that indicates whether the Coin has been collected or not
     *
     * @return boolean This returns a true or false value, indicates the collected status of the Coin
     */
    public boolean isCollected(){
        return this.collectedStatus;
    }

    /** This is a method that moves the Coin vertically up on the screen, this action should be implemented after
     * detected that the player has collected (collided with) the Coin
     */

    public void disappearOffScreen(){
        this.moveUp(this.DISAPPEAR_SPEED);
    }



}
