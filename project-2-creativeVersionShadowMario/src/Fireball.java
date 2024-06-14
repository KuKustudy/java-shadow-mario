import bagel.Image;
import java.util.Properties;

/**
 * Description: Fireball can be shot by player and enemyBoss to each other in the game, and if fireball collided
 *              with the player or the enemyBoss, their healthPoint will decrease by the DAMAGE_SIZE of the fireball
 */
public class Fireball extends GameEntity implements Shootable{
    private final double DAMAGE_SIZE;
    private double shootSpeed;
    private boolean active =  true;
    private boolean shotToLeft = false;

    /**
     * This is a constructor that constructs an instance of the class Fireball, which is also a child class of
     * the abstract class GameEntity and had implemented the interface Shootable.
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of Fireball.
     *              Other info includes: Image, HorizontalSpeed, Radius, DAMAGE_SIZE, shootSpeed
     */
    public Fireball (double x, double y, Properties props){
        super(x, y, props);
        this.DAMAGE_SIZE = Double.parseDouble(props.getProperty("gameObjects.fireball.damageSize"));
        this.shootSpeed = Double.parseDouble(props.getProperty("gameObjects.fireball.speed"));
        this.setImage(new Image(props.getProperty("gameObjects.fireball.image")));
        // assume horizontal speed is same as other game entity
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.coin.speed")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.fireball.radius")));
    }

    /**
     * This is setter method that set a fireball's active status to be false, so that it disappear from the screen
     * This is used when a fireball had hit the target OR missed the target and out of boundary of the window
     */
    public void setToInactive() {
        this.active = false;
    }

    /**
     * This is a setter method that set the direction of the fireball should move to.
     */
    public void shootToLeft(){
        this.shotToLeft = true;
    }

    /**
     * This is a setter method that set the direction of the fireball should move to.
     */
    public void shootToRight(){
        this.shotToLeft = false;
    }

    /**
     * This is a getter method that returns the active status of a fireball, indicating whether we should render it on the
     * screen.
     * @return boolean This indicates the active status of the fireball
     */
    public boolean isActive() {
        return active;
    }

    /**
     * This is a getter method that returns damage size of the fireball
     * @return double This returns the damageSize of the fireball
     */
    public double getDAMAGE_SIZE() {
        return DAMAGE_SIZE;
    }

    /**
     * This is a method that moves the fireball to the direction they needed
     */
    public void shoot(){
        if(this.shotToLeft == true){
            this.moveRight(this.shootSpeed);
        } else {
            this.moveLeft(this.shootSpeed);
        }
    }
}
