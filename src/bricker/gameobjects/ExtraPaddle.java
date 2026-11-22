package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents an extra temporary paddle in the Bricker game.
 * Behaves like a standard {@link Paddle}, except it removes itself from the game
 * after absorbing a fixed number of collisions and updates the active extra-paddle counter.
 */
public class ExtraPaddle extends Paddle {

	private static final int MAX_HITS = 4;

	private final Counter extraPaddleCounter;
	private final GameObjectCollection gameObjects;
	private int hitsCount;

	/**
	 * Constructs a new ExtraPaddle instance.
	 *
	 * @param topLeftCorner      Position of the paddle, in window coordinates (pixels).
	 * @param dimensions         Width and height in window coordinates.
	 * @param renderable         The renderable representing the paddle.
	 * @param inputListener      Listener for user keyboard input to control paddle movement.
	 * @param windowDimensions   The dimensions of the game window.
	 * @param minDistFromEdge    The minimum allowed distance from the left and right window edges.
	 * @param extraPaddleCounter Shared counter tracking the number of active extra paddles.
	 * @param gameObjects        The collection of game objects from which the paddle removes itself.
	 */
	public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
					   UserInputListener inputListener, Vector2 windowDimensions,
					   int minDistFromEdge, Counter extraPaddleCounter,
					   GameObjectCollection gameObjects) {
		super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions,
				minDistFromEdge);
		this.extraPaddleCounter = extraPaddleCounter;
		this.gameObjects = gameObjects;
		this.hitsCount = 0;
	}

	/**
	 * Handles a collision event by incrementing the hit count and removing the extra paddle
	 * once it reaches {@value #MAX_HITS} hits.
	 *
	 * @param other     The game object with which this paddle collided.
	 * @param collision Information regarding this collision.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		hitsCount++;
		if (hitsCount == MAX_HITS) {
			gameObjects.removeGameObject(this, Layer.DEFAULT);
			extraPaddleCounter.decrement();
		}
	}
}