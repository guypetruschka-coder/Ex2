package bricker.gameobjects;

import bricker.lives.LivesObserver;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.ArrayList;

/**
 * Displays the player's remaining lives as a row of heart icons in the UI layer.
 * Adds or removes heart objects on screen whenever notified of a change in lives.
 */
public class GraphicLifeCounter extends GameObject implements LivesObserver {
	private static final float HEART_PADDING = 5f;

	private final Renderable heartImage;
	private final GameObjectCollection gameObjects;
	private final ArrayList<GameObject> hearts;
	private int currentLives;

	/**
	 * Constructs a new graphic life counter.
	 *
	 * @param topLeftCorner Position of the first heart, in window coordinates (pixels).
	 * @param dimensions    Width and height of a single heart in window coordinates.
	 * @param renderable    The image renderable used for each heart.
	 * @param gameObjects   The game object collection to which hearts are added and removed.
	 */
	public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,
							  Renderable renderable, GameObjectCollection gameObjects) {
		super(topLeftCorner, dimensions, null);
		this.heartImage = renderable;
		this.gameObjects = gameObjects;
		this.currentLives = 0;
		this.hearts = new ArrayList<>();
	}

	/**
	 * Updates the displayed heart icons to match the player's current number of lives.
	 *
	 * @param lives The current number of lives.
	 */
	@Override
	public void onLivesChanged(int lives) {
		while (currentLives < lives) {
			float x = getTopLeftCorner().x() + currentLives * (getDimensions().x() + HEART_PADDING);
			Vector2 heartPosition = new Vector2(x, getTopLeftCorner().y());
			GameObject newHeart = new GameObject(heartPosition, getDimensions(), heartImage);
			gameObjects.addGameObject(newHeart, Layer.UI);
			hearts.add(newHeart);
			currentLives++;
		}

		while (currentLives > lives && currentLives > 0) {
			currentLives--;
			GameObject removedHeart = hearts.remove(currentLives);
			gameObjects.removeGameObject(removedHeart, Layer.UI);
		}
	}
}