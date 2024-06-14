import java.math.BigDecimal;
import java.util.Properties;

/**
 * Description: HealthPoint is a figure that associated with the GameEntities: player and the enemyBoss,
 *              it has an initial value at the start of the game and its value decreased when its associated
 *              GameEntity got attacked.
 */
public class HealthPoint {
    private final double INITIAL_HEALTH;
    private double currentHealthPoint;

    /**
     * This is a constructor that constructs an instance of the class HealthPoint
     *
     * @param nameOfGameEntity This is the name of the GameEntity that the healthpoint associated with
     * @param props This is a file that stores other information that defines an instance of HealthPoint. This includes
     *              the initial healthPoint that the associated GameEntity should have at the start of the game
     */
    public HealthPoint(String nameOfGameEntity, Properties props) {
        this.INITIAL_HEALTH = Double.parseDouble(props.getProperty("gameObjects."+nameOfGameEntity+".health"));
        this.currentHealthPoint = this.INITIAL_HEALTH;
    }

    /**
     * This is a setter method that update the associated gameEntity's current HealthPoint
     * @param damageSize This is the damage size of the object attacked the associated gameEntity, hence the healthPoint
     *                   of the associated gameEntity should decrease by this damageSize.
     */
    public void decreaseHealthPoint(double damageSize) {
        //use of bigDecimal to fix number precision error
        BigDecimal preciseHealthPoint = BigDecimal.valueOf(this.currentHealthPoint).subtract(BigDecimal.valueOf(damageSize));
        this.currentHealthPoint = preciseHealthPoint.doubleValue();
    }

    /**
     * This is a getter method that get the currentHealthPoint of the associated GameEntity in percentage form (without
     * the percentage sign). E.g if currentHealthPoint = 0.5, return int will be 50
     * @return int This returns the current Health Point in percentage form
     */
    public int getHealthPointPercentage(){
        double percentage = this.currentHealthPoint * 100.0;
        return (int)percentage;
    }


}
