import bagel.Image;
import java.util.Properties;

/**
 * Description: Platform is the 'ground' in the game where player stands on at the start of the game
 */
public class Platform extends GameEntity{
    private final int MAXCOORDINATE = 3000;

    /**
     * This is a constructor that constructs an instance of the class platform, which is also a child class of the
     * abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines this instance. Other info includes
     *              horizontalSpeed, Image
     */
    public Platform (double x, double y, Properties props){
        super(x, y, props);
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.platform.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.platform.image")));
    }

}
