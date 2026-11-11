package bricker.lives;
/**
 * An observer of the player's lives count.
 * Classes implementing this interface are notified whenever
 * the number of lives changes.
 */
public interface LivesObserver {
    /**
     * Called whenever the number of lives changes, and once when the
     * object is created, with the initial value.
     *
     * @param lives The current number of lives.
     */
    public void onLivesChanged(int lives);
}
