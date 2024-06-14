import bagel.Image;
import java.util.Properties;
import java.util.Random;

/**
 * Description: Enemy can attack player in the game by colliding with the player, once collided, the player's
 *              healthPoint will decrease by the enemy's damageSize. Enemy can move randomly within a range.
 */
public class Enemy extends GameEntity{
    private final double DAMAGE_SIZE;
    private final int MAX_DISPLACEMENTX;
    private double randomSpeed;
    private String randomDirection;
    private double displacementOfRandomMovement = 0.0;
    private boolean alreadyHitPlayerStatus = false;


    /**
     * This is a constructor that constructs an instance of the class Enemy, which is also a child class of
     * the abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of Enemy.
     *              Other info includes HorizontalSpeed, Image, Radius, randomSpeed, damageSize, max_displacementX.
     *              Each enemy also has an attribute - randomDirection that determines their direction of random
     *              movement.
     */
    public Enemy(double x, double y, Properties props){
        super(x,y,props);

        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.enemy.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.enemy.image")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.enemy.radius")));

        this.randomSpeed = Double.parseDouble(props.getProperty("gameObjects.enemy.randomSpeed"));
        this.DAMAGE_SIZE = Double.parseDouble(props.getProperty("gameObjects.enemy.damageSize"));
        this.MAX_DISPLACEMENTX = Integer.parseInt(props.getProperty("gameObjects.enemy.maxRandomDisplacementX"));

        //each enemy should choose a random horizontal direction at time of creation
        String[] directions = {"left", "right"};
        Random random = new Random();
        int randomIndex = random.nextInt(directions.length);
        this.randomDirection = directions[randomIndex];
    }

    /**
     * This is a setter method that recognise that the enemy had hit the player, this is used to ensure that each
     * enemy can only cause harm to the player once.
     */
    public void hadHitPlayer(){
        this.alreadyHitPlayerStatus = true;
    }

    /**
     * This is a getter method
     *
     * @return Double This returns the damageSize of the enemy
     */
    public double getDAMAGE_SIZE(){
        return this.DAMAGE_SIZE;
    }

    /**
     * This is a getter method
     * @return boolean This returns a boolean value indicating whether an enemy had been collided with the player
     */
    public boolean getAlreadyHitPlayerStatus(){
        return this.alreadyHitPlayerStatus;
    }

    /**
     * Enemy should move horizontally randomly in the game, the word random means that it chooses a direction
     * between left and right randomly. Each enemy should move within a range (MAX_DISPLACEMENTX)
     */
    public void moveHorizontalRandomly(){
        /*
         * Enemy should move according to the randomDirection that had set to them at the time of creation
         * if the enemy had reached the maximum displacement they can move, it should move reversely until it reach
         * the maximum displacement again.
         */
        if (this.randomDirection.equals("left")){

            if (this.displacementOfRandomMovement >= this.MAX_DISPLACEMENTX) {
                this.randomDirection = "right";
                this.displacementOfRandomMovement = 0;
            }

            this.moveLeft(this.randomSpeed);

            //keep track of the enemy's displacement
            this.displacementOfRandomMovement += this.randomSpeed;

        } else {
            if (this.displacementOfRandomMovement >= this.MAX_DISPLACEMENTX) {
                this.randomDirection = "left";
                this.displacementOfRandomMovement = 0;
            }

            this.moveRight(this.randomSpeed);
            //keep track of the enemy's displacement
            this.displacementOfRandomMovement += this.randomSpeed;

        }

    }

}
