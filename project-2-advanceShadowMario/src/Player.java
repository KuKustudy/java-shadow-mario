import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.ArrayList;

import java.util.Properties;

/**
 * player represents our user in the game, and can move Up, Left and Right according to the key pressed
 * by the user. A player has associated healthPoint and score that keeps track of their performance
 * in the game, and can jump on a flying platform in game level 2, and can shoot fireball in game level
 *
 */
public class Player extends GameEntity{
    private Image imageLeft;
    private Image imageRight;
    private final double JUMPSPEED = -20.0;
    private HealthPoint healthPoint;
    private Score score;
    private ArrayList<Fireball> allPlayerFireballs = new ArrayList<>();
    private double disappearOffSpeed = -2.0;
    private int activeDoubleScoreFrames = 0;
    private int activeInvincibilityFrames = 0;
    private boolean faceRightStatus = true;
    private boolean onFlyingPlatform = false;
    private double initialPlayerYOnPlatform;
    private FlyingPlatform currOnFlyingPlatform = null;

    /**
     * This is a constructor that constructs an instance of the class player, which is also a child class of the
     * abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines this instance. Other info includes
     *              Image left, Image right, Radius, info that defines its associated healthPoint and score.
     */
    public Player (double x, double y, Properties props){
        super(x,y,props);

        this.imageLeft = new Image(props.getProperty("gameObjects.player.imageLeft"));
        this.imageRight = new Image(props.getProperty("gameObjects.player.imageRight"));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.player.radius")));

        this.healthPoint = new HealthPoint("player", props);
        this.score = new Score();
    }

    /**
     * This is a setter method that set the period of time the DoubleScorePower should be active for after the power
     * is collected by the player, the time is measured in screen refresh frames.
     * @param activeDoubleScoreFrames This is an integer that defines the period of time the doubleScorePower should be
     *                                active for
     */
    public void setActiveDoubleScoreFrames(int activeDoubleScoreFrames) {
        this.activeDoubleScoreFrames = activeDoubleScoreFrames;
    }

    /**
     * This is a setter method that set the period of time the InvinciblePower should be active for after the power
     * is collected by the player, the time is measured in screen refresh frames.
     * @param activeInvincibilityFrames This is an integer that defines the period of time the InvinciblePower should be
     *                                active for
     */
    public void setActiveInvincibilityFrames(int activeInvincibilityFrames) {
        this.activeInvincibilityFrames = activeInvincibilityFrames;
    }

    /**
     * This is a setter method that links the player with the flyingPlatform it is on, this is used after the player had
     * jumped on the flyingPlatform
     * @param currOnFlyingPlatform This is the flyingPlatform the player is current standing on
     */
    public void setCurrOnFlyingPlatform(FlyingPlatform currOnFlyingPlatform) {
        this.currOnFlyingPlatform = currOnFlyingPlatform;
    }

    /**
     * This is a setter method that set the indicator to indicate that the player is currently ON a flying platform
     */
    public void hadLandOnFlyingPlatform(){
        this.onFlyingPlatform = true;
    }

    /**
     * This is a setter method that store the player's y-coordinate when it first landed on a platform. The stored
     * y-coordinate is latter used to make sure the player can land on the flying Platform when jumping.
     * @param y This is the player's y-coordinate when it first landed on a platform.
     */
    public void setInitialPlayerYOnPlatform(double y){
        this.initialPlayerYOnPlatform = y;
    }

    /**
     * This is a getter method that returns the player's current score in an integer form
     * @return int This returns the player's current score
     */
    public int getScore(){
        return this.score.getCurrentScore();
    }

    /**
     * This is a getter method that returns the player's current HealthPoint in percentage form (without the % sign)
     * @return int This returns the player's current Health Point
     */
    public int getHealthPercentage(){
        return this.healthPoint.getHealthPointPercentage();
    }

    /**
     * This is a getter method that returns an attribute that indicates whether the player is currently on a flying
     * platform.
     * @return boolean This returns true if the player is currently on a flying platform. Otherwise, return false
     */
    public boolean isOnFlyingPlatform(){
        return this.onFlyingPlatform;
    }

    /**
     * This is a getter method that returns the remaining number of frames that the doubleScore gift should still
     * be activated
     * @return int This returns the remaining number of frames that the player's doubleScore gift will still be active
     */
    public int getActiveDoubleScoreFrames(){
        return activeDoubleScoreFrames;
    }
    /**
     * This is a getter method that returns the remaining number of frames that the invincible gift should still
     * be activated
     * @return int This returns the remaining number of frames that the player's invincible gift will still be active
     */
    public int getActiveInvincibilityFrames(){
        return activeInvincibilityFrames;
    }

    /**
     * This is a getter method that returns the flyingPlatform that the player is currently on, or return null if
     * the player is currently not on any flyingPlatform
     * @return FlyingPlatform This returns the flyingPlatform that the player is currently on. Or return null if
     *                          the player is currently not on any flyingPlatform.
     */
    public FlyingPlatform getCurrOnFlyingPlatform() {
        return currOnFlyingPlatform;
    }

    /**
     * This is a getter method
     * @return ArrayList<Fireball> This returns all fireballs that had shot by the player
     */
    public ArrayList<Fireball> getAllPlayerFireballs() {
        return allPlayerFireballs;
    }

    /**
     * This is a method that recognises the attack that the player got hit by, the player's healthPoint will decrease
     * by the damageSize of the attack object
     * @param damageSize This is the damageSize of the gameEntity the player got hit by
     */
    public void getHurt(double damageSize){
        this.healthPoint.decreaseHealthPoint(damageSize);
    }

    /**
     * This is a method that recognises the coin value the player had collected, the player's score will increase by
     * the value of the coin collected.
     * @param value This is the value of the coin collected
     */
    public void gainScore(double value){
        this.score.increaseScore(value);
    }


    /**
     * This is a method that detect player's jumping motion according to the user's keyboard input, this only detect
     * jumping motion when the player is currently on the normal platform
     * @param input This is the keyboard input from the user, to which we move the player accordingly
     */
    public void jumpUp(Input input){
        double initialY = this.getPosition().getINITIALY();

        if (input.wasPressed(Keys.UP) && this.getPositionY() == initialY) {
            //Jump up
            this.setVerticalSpeed(this.JUMPSPEED);
        }

        if (this.getPositionY() < initialY){
            //During the player's jumping motion, speed should decrease by 1 each frame to reflect gravity
            this.setVerticalSpeed(this.getVerticalSpeed() + 1);
        }

        if (this.getVerticalSpeed() > 0 && this.getPositionY() >= initialY) {
            //Finished jump, player had touched ground
            this.setVerticalSpeed(0);
            this.getPosition().setY(initialY);

            /* if the player is jumping off from a flying platform, only recognise player
             * is not on the platform anymore when player touched ground
             */
            this.currOnFlyingPlatform = null;
        }

        //move the player vertically according to its speed
        this.moveUp();
    }

    /**
     * This is a method that detect player's jumping motion according to the user's keyboard input, this only detect
     * jumping motion when the player is currently on a flying platform
     * @param input This is the keyboard input from the user, to which we move the player accordingly
     */
    public void jumpUpOnPlatform(Input input){

        if (input.wasPressed(Keys.UP) && this.getPositionY() == this.initialPlayerYOnPlatform) {
            //Jumping up on the flying platform
            this.setVerticalSpeed(this.JUMPSPEED);
        }

        if (this.getPositionY() < this.initialPlayerYOnPlatform){
            //During the player's jumping motion, speed should decrease by 1 each frame to reflect gravity
            this.setVerticalSpeed(this.getVerticalSpeed() + 1);

        } else if (this.getVerticalSpeed()  > 0 && this.getPositionY() == this.initialPlayerYOnPlatform) {
            //Finished jump, player should back on the ground of the flying platform
            this.setVerticalSpeed(0);
            this.getPosition().setY(this.initialPlayerYOnPlatform);
        }

        //move the player vertically according to its speed
        this.moveUp();
    }

    /* determine whether the player is on the flying platform or not and update related boolean
       attributes accordingly.
    */

    /**
     * This is a method that determine whether the player is on the flying platform or not and therefore update related
     * boolean attributes accordingly, the boolean attribute refers to onFlyingPlatform
     */
    public void updateOnPlatformStatus(){

        //calculate the width of the flyingPlatform that the player is current standing on
        double platformXrangeFrom = this.currOnFlyingPlatform.getPositionX() - this.currOnFlyingPlatform.getHalfLength();
        double platformXrangeTo = this.currOnFlyingPlatform.getPositionX() + this.currOnFlyingPlatform.getHalfLength();

        /* if the player is out of the range of the x values of the flying platform, it is considered not on the
           platform anymore. In other words, player's positionX is out of range of platformX.
         */
        if(this.getPositionX() < platformXrangeFrom || this.getPositionX() > platformXrangeTo){

            this.onFlyingPlatform = false;
            /*note that even though we know player is currently not on the flying platform.
             *Its positionX is out of range of platformX. The change of jumping detection
             * will only occur after the player touched ground. Therefore, we only
             * "disconnect" the player with the platform after it touched ground, and
             * the disconnection will be done on the jumpUp method in class Player
            */

        }

    }

    /**
     * This method moves the player vertically down and disappear off from the screen, this method is used when the
     * player had lost all healthPoint.
     */
    public void disappearOffScreen(){
        this.moveDown(this.disappearOffSpeed);

    }

    /**
     * This is a method that shoot a new fireball from the player to the enemyBoss in gameLevel 3
     * @param props This is a file that contains info to contruct a new Fireball
     * @param direction This is a string that specify the direction of the newly created fireball should shoot to
     */
    public void shootFireball(Properties props, String direction){
        Fireball newFireball = new Fireball(this.getPositionX(), this.getPositionY(), props);
        if (direction.equals("left")){
            newFireball.shootToLeft();
        } else if (direction.equals("right")){
            newFireball.shootToRight();
        }
        //add the newly created and shot fireball to the player's list of shot fireball
        this.allPlayerFireballs.add(newFireball);
    }


    /**
     * This is a method that draw the player's left and right image according to the user's keyboard input,
     * and detects the player's jumping motion
     * @param input This is the user's keyboard input
     */
    public void draw(Input input){

        if(input.isDown(Keys.RIGHT)){
            this.faceRightStatus = true;
        }
        if(input.isDown(Keys.LEFT)){
            this.faceRightStatus = false;
        }

        if (this.faceRightStatus){
            this.imageRight.draw(this.getPositionX(), this.getPositionY());
        } else {
            this.imageLeft.draw(this.getPositionX(), this.getPositionY());
        }


        //detect player's jump decision
        if (!this.isOnFlyingPlatform()){
            //when the player is on the normal platform
            this.jumpUp(input);
        }else {
            // when the player is on a flying platform
            this.jumpUpOnPlatform(input);
            this.updateOnPlatformStatus();
        }
    }
}
