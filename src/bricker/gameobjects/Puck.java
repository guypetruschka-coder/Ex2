package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a puck (extra ball) in the Bricker game.
 * Behaves like a standard {@link Ball}, except it removes itself from the game
 * when falling below the bottom boundary of the window without costing a life.
 */
public class Puck extends Ball {

	private final float windowHeight;
	private final GameObjectCollection gameObjects;

	/**
	 * Constructs a new Puck instance.
	 *
	 * @param topLeftCorner  Position of the puck, in window coordinates (pixels).
	 * @param dimensions     Width and height in window coordinates.
	 * @param renderable     The renderable representing the puck.
	 * @param collisionSound The sound played when the puck collides with another object.
	 * @param windowHeight   The height of the game window, used to detect when the puck exits the screen.
	 * @param gameObjects    The collection of game objects from which the puck removes itself.
	 */
	public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				Sound collisionSound, float windowHeight, GameObjectCollection gameObjects) {
		super(topLeftCorner, dimensions, renderable, collisionSound);
		this.windowHeight = windowHeight;
		this.gameObjects = gameObjects;
	}

	/**
	 * Updates the puck's state each frame and removes it from the game if it falls below the window.
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