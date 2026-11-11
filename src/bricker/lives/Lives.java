package bricker.lives;
/**
 * Holds the number of lives (strikes) the player has left, and notifies its
 * observers whenever that number changes.
 */
public class Lives {
    private static final int MAX_OBSERVERS = 10;
    private int lives;
    private final int maxLives;
    private int observersCount;
    private final LivesObserver[] livesObservers = new LivesObserver[MAX_OBSERVERS];

    /**
     * Constructs a new lives counter with no registered observers.
     *
     * @param lives    The initial number of lives.
     * @param maxLives The maximum number of lives the player can have.
     */
    public Lives(int lives, int maxLives) {
        this.lives = lives;
        this.maxLives = maxLives;
        this.observersCount = 0;
    }

    /**
     * Registers an observer to be notified whenever the number of lives
     * changes.
     * @param observer The observer to register.
     */
    public void registerObserver(LivesObserver observer) {
        if (observersCount < MAX_OBSERVERS) {
            livesObservers[observersCount] = observer;
            observersCount++;
            observer.onLivesChanged(lives);
        }
    }

    /**
     * Checks whether the player has no lives left.
     *
     * @return true if the number of lives is zero, false otherwise.
     */
    public boolean isEmpty() {
        return (lives == 0);
    }

    /**
     * Adds a single life, unless the maximum has already been reached.
     * Observers are notified only if the number of lives actually changed.
     */
    public void increase() {
        if (lives < maxLives) {
            lives++;
            notifyObservers();
        }
    }

    /**
     * Removes a single life, unless there are no lives left.
     * Observers are notified only if the number of lives actually changed.
     */
    public void decrease() {
        if (lives > 0) {
            lives--;
            notifyObservers();
        }
    }

    /**
     * Notifies all observers of the current number of lives.
     */
    private void notifyObservers() {
        for (int i = 0; i < observersCount; i++) {
            livesObservers[i].onLivesChanged(lives);
        }
    }



}
