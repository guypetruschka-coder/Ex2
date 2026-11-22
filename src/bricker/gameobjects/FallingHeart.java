package bricker.gameobjects;

import bricker.lives.Lives;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a falling heart pickup in the Bricker game.
 * Collides only with the main paddle to grant an extra life, and removes itself
 * from the game once collected or after falling below the bottom of the window.
 */
public class FallingHeart extends GameObject {

	private final Lives lives;
	private final GameObjectCollection gameObjects;
	private final float windowHeight;
	private final String mainPaddleTag;

	/**
	 * Constructs a new FallingHeart instance.
	 *
	 * @param topLeftCorner Position of the heart, in window coordinates (pixels).
	 * @param dimensions    Width and height of the heart in window coordinates.
	 * @param renderable    The renderable representing the heart.
	 * @param lives         The player's lives manager, incremented when the heart is collected.
	 * @param gameObjects   The collection of game objects from which the heart removes itself.
	 * @param windowHeight  The height of the game window, used to detect when the heart exits the screen.
	 * @param mainPaddleTag The tag identifying the main paddle that is allowed to collect this heart.
	 */
	public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
						Lives lives, GameObjectCollection gameObjects, float windowHeight,
						String mainPaddleTag) {
		super(topLeftCorner, dimensions, renderable);
		this.lives = lives;
		this.gameObjects = gameObjects;
		this.windowHeight = windowHeight;
		this.mainPaddleTag = mainPaddleTag;
	}

	/**
	 * Determines whether this heart should collide with another game object.
	 * Restricts collisions exclusively to the main paddle.
	 *
	 * @param other The other game object to check collision eligibility against.
	 * @return {@code true} if {@code other} has the main paddle tag, {@code false} otherwise.
	 */
	@Override
	public boolean shouldCollideWith(GameObject other) {
		return mainPaddleTag.equals(other.getTag());
	}

	/**
	 * Handles collision with the main paddle by removing the heart from the game
	 * and increasing the player's life count.
	 *
	 * @param other     The game object with which this heart collided.
	 * @param collision Information regarding this collision.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (gameObjects.removeGameObject(this, Layer.DEFAULT)) {
			lives.increase();
		}
	}

	/**
	 * Updates the heart's state each frame and removes it from the game if it falls
	 * below the bottom edge of the window.
	 *
	 * @param deltaTime The time elapsed, in seconds, since the last frame.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (getTopLeftCorner().y() > windowHeight) {
			gameObjects.removeGameObject(this, Layer.DEFAULT);
		}
	}
}