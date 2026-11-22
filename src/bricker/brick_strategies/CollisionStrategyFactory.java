package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.lives.Lives;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Factory class responsible for randomly creating {@link CollisionStrategy} instances
 * for bricks according to the game's probability distribution:
 * 1/2 basic strategy, and 1/8 for each of the four special strategies.
 */
public class CollisionStrategyFactory {

	private static final int NUM_SPECIAL_STRATEGIES = 4;
	private static final int PUCK_STRATEGY_INDEX = 0;
	private static final int EXTRA_PADDLE_STRATEGY_INDEX = 1;
	private static final int EXPLODING_BRICK_STRATEGY_INDEX = 2;
	private static final int EXTRA_LIFE_STRATEGY_INDEX = 3;

	private final Random random = new Random();
	private final Counter extraPaddleCounter = new Counter(0);
	private final CollisionStrategy basicStrategy;

	private final GameObjectCollection gameObjects;
	private final Vector2 windowDimensions;

	private final Renderable puckImage;
	private final Sound collisionSound;
	private final Vector2 puckDimensions;
	private final float ballSpeed;

	private final Renderable paddleImage;
	private final Vector2 paddleDimensions;
	private final UserInputListener inputListener;
	private final int minDistFromEdge;

	private final Brick[][] bricksGrid;
	private final Sound explosionSound;

	private final Renderable heartImage;
	private final Vector2 heartDimensions;
	private final Lives lives;
	private final String mainPaddleTag;

	/**
	 * Constructs a new CollisionStrategyFactory with all dependencies required
	 * to instantiate any brick collision strategy.
	 *
	 * @param gameObjects      The collection of game objects in the game.
	 * @param bricksCounter    Shared counter tracking the remaining bricks.
	 * @param windowDimensions The dimensions of the game window.
	 * @param puckImage        The renderable image for spawned pucks.
	 * @param collisionSound   The sound played on ball/puck collisions.
	 * @param puckDimensions   The width and height of spawned pucks.
	 * @param ballSpeed        The speed magnitude of spawned pucks.
	 * @param paddleImage      The renderable image for the extra paddle.
	 * @param paddleDimensions The width and height of the extra paddle.
	 * @param inputListener    Listener for reading player keyboard input.
	 * @param minDistFromEdge  Minimum allowed distance between a paddle and the screen edges.
	 * @param bricksGrid       The 2D grid of bricks in the game, used for exploding bricks.
	 * @param explosionSound   The sound played when an exploding brick detonates.
	 * @param heartImage       The renderable image for falling hearts.
	 * @param heartDimensions  The width and height of falling hearts.
	 * @param lives            The player's lives manager.
	 * @param mainPaddleTag    The tag identifying the main paddle for heart collection.
	 */
	public CollisionStrategyFactory(GameObjectCollection gameObjects, Counter bricksCounter,
									Vector2 windowDimensions, Renderable puckImage,
									Sound collisionSound, Vector2 puckDimensions, float ballSpeed,
									Renderable paddleImage, Vector2 paddleDimensions,
									UserInputListener inputListener, int minDistFromEdge,
									Brick[][] bricksGrid, Sound explosionSound,
									Renderable heartImage, Vector2 heartDimensions,
									Lives lives, String mainPaddleTag) {
		this.basicStrategy = new BasicCollisionStrategy(gameObjects, bricksCounter);
		this.gameObjects = gameObjects;
		this.windowDimensions = windowDimensions;
		this.puckImage = puckImage;
		this.collisionSound = collisionSound;
		this.puckDimensions = puckDimensions;
		this.ballSpeed = ballSpeed;
		this.paddleImage = paddleImage;
		this.paddleDimensions = paddleDimensions;
		this.inputListener = inputListener;
		this.minDistFromEdge = minDistFromEdge;
		this.bricksGrid = bricksGrid;
		this.explosionSound = explosionSound;
		this.heartImage = heartImage;
		this.heartDimensions = heartDimensions;
		this.lives = lives;
		this.mainPaddleTag = mainPaddleTag;
	}

	/**
	 * Randomly selects and returns a {@link CollisionStrategy} for a brick:
	 * with probability 1/2 returns the basic strategy, and with probability 1/8 each
	 * returns a puck, extra paddle, exploding brick, or extra life strategy.
	 *
	 * @return A newly selected {@link CollisionStrategy}.
	 */
	public CollisionStrategy getStrategy() {
		if (random.nextBoolean()) {
			return basicStrategy;
		}

		int choice = random.nextInt(NUM_SPECIAL_STRATEGIES);
		switch (choice) {
			case PUCK_STRATEGY_INDEX:
				return new PuckStrategy(basicStrategy, gameObjects, puckImage, collisionSound,
						puckDimensions, ballSpeed, windowDimensions.y());
			case EXTRA_PADDLE_STRATEGY_INDEX:
				return new ExtraPaddleStrategy(basicStrategy, gameObjects, paddleImage,
						paddleDimensions, inputListener, windowDimensions,
						minDistFromEdge, extraPaddleCounter);
			case EXPLODING_BRICK_STRATEGY_INDEX:
				return new ExplodingBrickStrategy(basicStrategy, bricksGrid, explosionSound);
			case EXTRA_LIFE_STRATEGY_INDEX:
				return new ExtraLifeStrategy(basicStrategy, gameObjects, heartImage,
						heartDimensions, lives, windowDimensions.y(), mainPaddleTag);
			default:
				return basicStrategy;
		}
	}
}