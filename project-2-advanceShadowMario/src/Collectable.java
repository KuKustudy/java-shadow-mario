/**
 * This interface represents objects that can be collected by player
 */
public interface Collectable {
    public boolean collectedStatus = false;
    /**
     * Indicates the object has been collected by player by setting indicator attribute accordingly
     */
    public void hasBeenCollected();

    /**
     *  Returns the collected status of the object, return true if object has been collected, else, return false
     *
     * @return boolean This is an indicator of whether the object has been collected or not
     */
    public boolean isCollected();
}
