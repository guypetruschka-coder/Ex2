package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy decorator that spawns an {@link ExtraPaddle} at the center
 * of the game window upon collision, provided the number of active extra paddles
 * is below the allowed maximum.
 */
public class ExtraPaddleStrategy implements CollisionStrategy {

	private static final float CENTER_FACTOR = 0.5f;
	private static final int MAX_EXTRA_PADDLES = 1;

	private final CollisionStrategy baseStrategy;
	private final GameObjectCollection gameObjects;
	private final Renderable extraPaddleImage;
	private final Vector2 extraPaddleDimensions;
	private final UserInputListener inputListener;
	private final Vector2 windowDimensions;
	private final int minDistFromEdge;
	private final Counter extraPaddleCounter;

	/**
	 * Constructs a new ExtraPaddleStrategy instance.
	 *
	 * @param baseStrategy          The underlying collision strategy to execute first.
	 * @param gameObjects           The collection of game objects to which the extra paddle will be added.
	 * @param extraPaddleImage      The renderable image for the spawned extra paddle.
	 * @param extraPaddleDimensions The width and height of the spawned extra paddle.
	 * @param inputListener         Listener for reading player keyboard input.
	 * @param windowDimensions      The dimensions of the game window.
	 * @param minDistFromEdge       Minimum allowed distance between the extra paddle and the screen edges.
	 * @param extraPaddleCounter    Shared counter tracking the number of active extra paddles.
	 */
	public ExtraPaddleStrategy(CollisionStrategy baseStrategy, GameObjectCollection gameObjects,
							   Renderable extraPaddleImage, Vector2 extraPaddleDimensions,
							   UserInputListener inputListener, Vector2 windowDimensions,
							   int minDistFromEdge, Counter extraPaddleCounter) {
		this.baseStrategy = baseStrategy;
		this.gameObjects = gameObjects;
		this.extraPaddleImage = extraPaddleImage;
		this.extraPaddleDimensions = extraPaddleDimensions;
		this.inputListener = inputListener;
		this.windowDimensions = windowDimensions;
		this.minDistFromEdge = minDistFromEdge;
		this.extraPaddleCounter = extraPaddleCounter;
	}

	/**
	 * Executes the base collision strategy and spawns an extra paddle at the center
	 * of the window if fewer than {@value #MAX_EXTRA_PADDLES} extra paddles are currently active.
	 *
	 * @param thisObj  The brick that was collided with.
	 * @param otherObj The game object that collided with the brick.
	 */
	@Override
	public void onCollision(Brick thisObj, GameObject otherObj) {
		baseStrategy.onCollision(thisObj, otherObj);
		if (extraPaddleCounter.value() < MAX_EXTRA_PADDLES) {
			ExtraPaddle extraPaddle = new ExtraPaddle(
					Vector2.ZERO,
					extraPaddleDimensions,
					extraPaddleImage,
					inputListener,
					windowDimensions,
					minDistFromEdge,
					extraPaddleCounter,
					gameObjects
			);
			extraPaddle.setCenter(windowDimensions.mult(CENTER_FACTOR));
			gameObjects.addGameObject(extraPaddle, Layer.DEFAULT);
			extraPaddleCounter.increment();
		}
	}
}