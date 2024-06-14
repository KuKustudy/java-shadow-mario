/**
 * Description: each GameEntity in the game has its own position, it is represented in a cartesian form (x, y)
 *              the position of an GameEntity indicates where should we render the image of that GameEntity on the
 *              screen.
 */
public class Position {
    private final double INITIALX;
    private final double INITIALY;
    private double x;
    private double y;


    /**
     * This is a constructor that constructs an instance of the class Position
     *
     * @param x This is the X-coordinate of the position
     * @param y This is the Y-coordinate of the position
     *          Every instance of position also stores their initial x and y, which is the x and y value they got
     *          assigned to at its creation.
     */
    public Position (double x, double y){
        this.x = x;
        this.y = y;
        this.INITIALX = x;
        this.INITIALY = y;
    }

    /**
     * This is a setter method that set the X-coordinate of a game entity
     * @param x This is the new X-coordinate of a game entity
     */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * This is a setter method that set the Y-coordinate of a game entity
     * @param y This is the new Y-coordinate of a game entity
     */
    public void setY(double y){
        this.y = y;
    }



    /**
     * This is a getter method
     * @return double This returns the X-coordinate of a game entity
     */
    public double getX() {
        return this.x;
    }

    /**
     * This is a getter method
     * @return double This returns the Y-coordinate of a game entity
     */
    public double getY() {
        return this.y;
    }

    /**
     * This is a getter method
     * @return double This returns the initial X-coordinate of a game entity
     */
    public double getINITIALX(){
        return this.INITIALX;
    }

    /**
     * This is a getter method
     * @return double This returns the initial Y-coordinate of a game entity
     */
    public double getINITIALY(){
        return this.INITIALY;
    }

    /**
     * This is method that calculate the distance between two positions (x1, y1) and (x2, y2)
     * @param other This is the other position
     * @return double This returns the distance between two positions
     */
    public double distance (Position other) {
        double x1 = this.x, y1 = this.y;
        double x2 = other.x, y2 = other.y;
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

}
