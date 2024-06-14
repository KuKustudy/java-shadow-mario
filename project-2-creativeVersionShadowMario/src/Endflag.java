import bagel.Image;
import java.util.Properties;

/**
 * Description: Endflag can be reached by the player, and being reached by the player is the win condition
 * for game level 1 and 2
 */
public class Endflag extends GameEntity{
    /**
     * This is a constructor that constructs an instance of the class Endflag, which is also a child
     * class of the abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of Endflag.
     *              Other info includes: HorizontalSpeed, Image, Radius
     */
    public Endflag(double x, double y, Properties props){
        super(x, y, props);
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.endFlag.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.endFlag.image")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.endFlag.radius")));
    }

}
