import bagel.Image;
import java.util.Properties;

/**
 * This is an abstract class that each instance represents an object in the game ShadowMario
 */
abstract class GameEntity implements Moveable{
    private Position position;
    private double horizontalSpeed;
    private double verticalSpeed;
    private Image image;
    private double radius;

    /**
     * This is a constructor of the abstract class GameEntity, it serves as a base constructor for all entities that
     * exist as a subclass of GameEntity
     *
     * @param x This is the X-coordinate of the position of this instance
     * @param y This is the Y-coordinate of the position of this instance
     * @param props This is a file that stores other information that defines this instance.
     */
    public GameEntity(double x, double y, Properties props){
        this.position = new Position(x, y);
    }

    /**
     * This is a setter method that set the horizontalSpeed of this GameEntity, it is used in the constructor of
     * some of the child classes of GameEntity
     * @param horizontalSpeed This is the horizontalSpeed of a GameEntity
     */
     public void setHorizontalSpeed(double horizontalSpeed) {
         this.horizontalSpeed = horizontalSpeed;
     }

    /**
     * This is a setter method that set the verticalSpeed of this GameEntity, it is mainly used when a GameEntity has
     * jump motion.
     * @param verticalSpeed This is the verticalSpeed of a GameEntity
     */
    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    /**
     * This is a setter method that set the Radius of this GameEntity, it is used in the constructor of
     * some of the child classes of GameEntity
     * @param radius This is the radius of a GameEntity
     */
    public void setRadius (double radius){
        this.radius = radius;
    }

    /**
     * This is a setter method that set the Image of this GameEntity, it is used in the constructor of
     * the child classes of GameEntity
     * @param img This is the image of a GameEntity
     */
    public void setImage(Image img){
        this.image = img;
    }

    /**
     * This is a getter method
     * @return Position This returns the Position of this GameEntity
     */
    public Position getPosition(){
        return this.position;
    }

    /**
     * This is a getter method
     * @return double This returns the X-Coordinate of the position of the GameEntity
     */
    public double getPositionX(){
        return this.position.getX();
    }
    /**
     * This is a getter method
     * @return double This returns the Y-Coordinate of the position of the GameEntity
     */
    public double getPositionY(){
        return this.position.getY();
    }

    /**
     * This is a getter method
     * @return double This returns the current vertical speed of the GameEntity
     */
    public double getVerticalSpeed(){
        return this.verticalSpeed;
    }

    /**
     * This is a getter method, it is mainly used to determine the collision of two GameEntity
     * @return double This returns the radius of the GameEntity
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * This method moves the gameEntity up according to its speed
     */
    public void moveUp(){
         this.position.setY(this.position.getY() + this.verticalSpeed);
    }

    /**
     * This method moves the gameEntity up according to a speed other than the GameEntity's normal moving speed
     * for example: its speed of disappearing from the screen
     */
    public void moveUp(double disappearSpeed){
        this.position.setY(this.position.getY() + disappearSpeed);
    }

    /**
     * This method moves the gameEntity down according to its speed
     */
     public void moveDown(){
         this.position.setY(this.position.getY() - this.verticalSpeed);
     }

    /**
     * This method moves the gameEntity down according to a speed other than the GameEntity's normal moving speed
     * for example: its speed of disappearing from the screen
     */
    public void moveDown(double disappearSpeed){
        this.position.setY(this.position.getY() - disappearSpeed);
    }

    /**
     * This method moves the gameEntity to the left according to its speed
     */
     public void moveLeft(){
         this.position.setX(this.position.getX() + this.horizontalSpeed);
     }

    /**
     * This method moves the gameEntity to the left according to a speed other than the GameEntity's normal moving speed
     * for example: its speed of randomly moving on the screen
     */
    public void moveLeft(double randomSpeed){
        this.position.setX(this.position.getX() + randomSpeed);
    }

    /**
     * This method moves the gameEntity to the right according to its speed
     */
     public void moveRight(){
         this.position.setX(this.position.getX() - this.horizontalSpeed);
     }

    /**
     * This method moves the gameEntity to the right according to a speed other than the GameEntity's normal moving
     * speed. For example: its speed of randomly moving on the screen
     * */
    public void moveRight(double randomSpeed){
        this.position.setX(this.position.getX() - randomSpeed);
    }

    /**
     * This method render the GameEntity on the screen using its current Position
     */
     public void draw(){
         this.image.draw(this.getPosition().getX(), this.getPosition().getY());
     }

}
