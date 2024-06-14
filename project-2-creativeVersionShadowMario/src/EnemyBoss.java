import bagel.Image;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Description: Enemy boss only appears in level 3 of the game. it can only attack the player by shooting a fireball
 *              Once it is defeated, it will disappear off the screen by moving vertically down
 */
public class EnemyBoss extends GameEntity{
    private final double ACTIVATION_RADIUS;
    private ArrayList<Fireball> allEnemyFireballs = new ArrayList<>();
    private HealthPoint healthPoint;
    private boolean defeatedStatus = false;
    private double disappearOffSpeed = -2.0;
    private int randomShootFramesGap = 100;

    /**
     * This is a constructor that constructs an instance of the class EnemyBoss, which is also a child class of
     * the abstract class GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines an instance of EnemyBoss.
     *              Other info includes ACTIVATION_RADIUS, HorizontalSpeed, Image, Radius. The file also contains info
     *              that construct the attribute healthPoint of an enemyBoss
     */
    public EnemyBoss(double x, double y, Properties props){
        super(x, y, props);
        this.ACTIVATION_RADIUS = Double.parseDouble(props.getProperty("gameObjects.enemyBoss.activationRadius"));
        this.setHorizontalSpeed(Double.parseDouble(props.getProperty("gameObjects.enemyBoss.speed")));
        this.setImage(new Image(props.getProperty("gameObjects.enemyBoss.image")));
        this.setRadius(Double.parseDouble(props.getProperty("gameObjects.enemyBoss.radius")));
        this.healthPoint = new HealthPoint("enemyBoss", props);

    }

    /**
     * This is a setter method that set the defeatedStatus of an enemyBoss to be true, it is used when we recognise that
     * the enemyBoss' healthPoint is less than zero.
     */
    public void hadBeenDefeated() {
        this.defeatedStatus = true;
    }

    /**
     * This is a getter method
     * @return int This returns the enemyBoss' current healthPoint in percentage form (without the % sign)
     */
    public int getHealthPercentage(){
        return this.healthPoint.getHealthPointPercentage();
    }
    public boolean isDefeatedStatus() {
        return defeatedStatus;
    }


    /**
     * This is a getter method
     * @return ArrayList<Fireball> This returns all the fireBalls that the enemyBoss had generated (which could includes
     *                             active Fireballs and inactive Fireballs)
     */
    public ArrayList<Fireball> getAllEnemyFireballs() {
        return allEnemyFireballs;
    }

    /**
     * This is a method that move the enemyBoss vertically down, it is used when the enemyBoss is defeated by the player
     */
    public void disappearOffScreen(){
        this.moveDown(this.disappearOffSpeed);

    }

    /**
     *  This is a method that recognises the enemyBoss had been hurt by a fireball shot by the player, its healthPoint
     *  therefore decreased
     *
     * @param damageSize double This is the damageSize attached with the fireBall shot by the player
     */
    public void getHurt(double damageSize){
        this.healthPoint.decreaseHealthPoint(damageSize);
    }

    /**
     * Every 100 frames, the enemy boss will randomly inflict damage on the player by shooting a fireball
     * if the player is at least 500 pixels from it. The randomness is decided as follows - every 100 frames,
     * a random boolean is generated - if it is true, the enemy can fire and if false, it cannot fire.
     * @param props This is a Properties attribute that assists us in the creation of fireBall
     * @param direction This is a String that indicates in which way the fireBall should shoot to
     */
    public void shootFireballRandomly (Properties props, String direction) {
        Random random = new Random();
        boolean randomBoolean;

        //randomly deciding whether to shoot a fireball, make a decision every 100 frames
        if (this.randomShootFramesGap == 100) {
            randomBoolean = random.nextBoolean();
            //let's shoot a fireball
            if (randomBoolean == true){
                Fireball newFireball = new Fireball(this.getPositionX(), this.getPositionY(), props);
                //set the direction of the newly created fireball
                if (direction.equals("left")){
                    newFireball.shootToLeft();
                } else if (direction.equals("right")){
                    newFireball.shootToRight();
                }
                this.allEnemyFireballs.add(newFireball);
            }
        }

        //we are counting the number Of frames, and we want to renew the frame count every 100 frames
        if (this.randomShootFramesGap > 0){
            this.randomShootFramesGap -= 1;
        } else {
            this.randomShootFramesGap = 100;
        }

    }
}
