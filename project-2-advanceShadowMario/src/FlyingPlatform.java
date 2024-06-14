import java.util.Properties;
import java.util.Random;
import bagel.Image;

/**
 * Description: This is a featured type of platform that appears in GameLevel 2 and 3, it moves horizontal randomly
 * within a range. The player can jump on it but can only jump to a higher platform, and cannot jump from
 * higher to lower platform
 */
public class FlyingPlatform extends GameEntity{
    private double randomSpeed;
    private String randomDirection;
    private double displacementOfRandomMovement = 0.0;
    private double halfLength;
    private double halfHeight;
    private final int MAX_DISPLACEMENTX;

    /**
     * This is a constructor that constructs an instance of the class FlyingPlatform, which is also a child
     * class of the abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of FlyingPlatform.
     *              Other info includes: Image, HorizontalSpeed, randomSpeed, halfLength, halfHeight, MAX_DISPLACEMENT
     *              Each flyingPlatform also has an attribute - randomDirection that determines their direction of random
     *              movement.
     */
    public FlyingPlatform(double x, double y, Properties props){
        super(x, y, props);
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.flyingPlatform.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.flyingPlatform.image")));
        this.randomSpeed = Double.parseDouble(props.getProperty("gameObjects.flyingPlatform.randomSpeed"));
        this.halfLength = Double.parseDouble(props.getProperty("gameObjects.flyingPlatform.halfLength"));
        this.halfHeight = Double.parseDouble(props.getProperty("gameObjects.flyingPlatform.halfHeight"));
        this.MAX_DISPLACEMENTX = Integer.parseInt(props.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));

        //each flyingPlatform should choose a random horizontal direction at time of creation
        String[] directions = {"left", "right"};
        Random random = new Random();
        int randomIndex = random.nextInt(directions.length);
        this.randomDirection = directions[randomIndex];
    }


    /**
     * This is a getter method that returns the HalfLength of the flyingPlatform, it is called to assist in the
     * determination of landing player on platform
     * @return double This returns halfLength of the flyingPlatform
     */
    public double getHalfLength() {
        return halfLength;
    }

    /**
     * This is a getter method that returns the HalfHeight of the flyingPlatform, it is called to assist in the
     * determination of landing player on platform
     * @return double This returns halfHeight of the flyingPlatform
     */
    public double getHalfHeight() {
        return halfHeight;
    }

    /**
     * This method move the flying platform horizontal randomly within a set range.
     * FlyingPlatform should move according to the randomDirection that had set to them at the time of creation
     * if the flyingPlatform had reached the maximum displacement it can move, it should move reversely until it reach
     * the maximum displacement again.
     */
    public void moveHorizontalRandomly(){

        if (this.randomDirection.equals("left")){
            //if the flyingPlatform had reached the maximum displacement, reverse its direction
            if (this.displacementOfRandomMovement >= this.MAX_DISPLACEMENTX) {
                this.randomDirection = "right";
                this.displacementOfRandomMovement = 0;
            }
            this.moveLeft(this.randomSpeed);
            this.displacementOfRandomMovement += this.randomSpeed;
        } else {
            if (this.displacementOfRandomMovement >= this.MAX_DISPLACEMENTX) {
                this.randomDirection = "left";
                this.displacementOfRandomMovement = 0;
            }
            this.moveRight(this.randomSpeed);
            this.displacementOfRandomMovement += this.randomSpeed;
        }

    }
}
