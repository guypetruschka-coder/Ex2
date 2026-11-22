package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.FallingHeart;
import bricker.lives.Lives;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A collision strategy decorator that spawns a {@link FallingHeart} at the center
 * of the broken brick upon collision, in addition to executing the wrapped base strategy.
 */
public class ExtraLifeStrategy implements CollisionStrategy {

	private static final float HEART_FALLING_SPEED = 100f;

	private final CollisionStrategy baseStrategy;
	private final GameObjectCollection gameObjects;
	private final Renderable heartImage;
	private final Vector2 heartDimensions;
	private final Lives lives;
	private final float windowHeight;
	private final String mainPaddleTag;

	/**
	 * Constructs a new ExtraLifeStrategy instance.
	 *
	 * @param baseStrategy    The underlying collision strategy to execute first.
	 * @param gameObjects     The collection of game objects to which the falling heart will be added.
	 * @param heartImage      The renderable image for the spawned falling heart.
	 * @param heartDimensions The width and height of the spawned falling heart.
	 * @param lives           The player's lives manager, updated if the heart is collected.
	 * @param windowHeight    The height of the game window, used by the heart to detect exit.
	 * @param mainPaddleTag   The tag identifying the main paddle allowed to collect the heart.
	 */
	public ExtraLifeStrategy(CollisionStrategy baseStrategy, GameObjectCollection gameObjects,
							 Renderable heartImage, Vector2 heartDimensions, Lives lives,
							 float windowHeight, String mainPaddleTag) {
		this.baseStrategy = baseStrategy;
		this.gameObjects = gameObjects;
		this.heartImage = heartImage;
		this.heartDimensions = heartDimensions;
		this.lives = lives;
		this.windowHeight = windowHeight;
		this.mainPaddleTag = mainPaddleTag;
	}

	/**
	 * Executes the base collision strategy and spawns a falling heart at the center
	 * of the collided brick.
	 *
	 * @param thisObj  The brick that was collided with.
	 * @param otherObj The game object that collided with the brick.
	 */
	@Override
	public void onCollision(Brick thisObj, GameObject otherObj) {
		baseStrategy.onCollision(thisObj, otherObj);
		Vector2 brickCenter = thisObj.getCenter();
		FallingHeart fallingHeart = createFallingHeart(brickCenter);
		gameObjects.addGameObject(fallingHeart, Layer.DEFAULT);
	}

	private FallingHeart createFallingHeart(Vector2 center) {
		FallingHeart fallingHeart = new FallingHeart(Vector2.ZERO, heartDimensions, heartImage,
				lives, gameObjects, windowHeight, mainPaddleTag);
		fallingHeart.setCenter(center);
		fallingHeart.setVelocity(Vector2.DOWN.mult(HEART_FALLING_SPEED));
		return fallingHeart;
	}
}