/**
 * This interface represents gameEntities that can move on the screen during the game of ShadowMario
 */
public interface Moveable {
    /**
     * This method move a game entity up on the screen according to its current verticalSpeed
     */
    public void moveUp ();

    /**
     * This method move a game entity down on the screen according to its current verticalSpeed
     */
    public void moveDown ();

    /**
     * This method move a game entity to the left on the screen according to its current horizontalSpeed
     */
    public void moveLeft ();

    /**
     * This method move a game entity to the right on the screen according to its current horizontalSpeed
     */
    public void moveRight ();


}
