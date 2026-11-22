package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A collision strategy decorator that spawns additional {@link Puck} balls
 * at the center of the broken brick upon collision, in addition to executing
 * the wrapped base strategy.
 */
public class PuckStrategy implements CollisionStrategy {

	private static final int NUM_PUCKS = 2;

	private final CollisionStrategy baseStrategy;
	private final GameObjectCollection gameObjects;
	private final Renderable puckImage;
	private final Sound collisionSound;
	private final Vector2 puckDimensions;
	private final float ballSpeed;
	private final float windowHeight;
	private final Random random = new Random();

	/**
	 * Constructs a new PuckStrategy instance.
	 *
	 * @param baseStrategy   The underlying collision strategy to execute first.
	 * @param gameObjects    The collection of game objects to which the new pucks will be added.
	 * @param puckImage      The renderable image for the spawned pucks.
	 * @param collisionSound The sound played when a puck collides with another object.
	 * @param puckDimensions The width and height of each spawned puck.
	 * @param ballSpeed      The speed magnitude of each spawned puck.
	 * @param windowHeight   The height of the game window, used by pucks to detect exit.
	 */
	public PuckStrategy(CollisionStrategy baseStrategy, GameObjectCollection gameObjects,
						Renderable puckImage, Sound collisionSound, Vector2 puckDimensions,
						float ballSpeed, float windowHeight) {
		this.baseStrategy = baseStrategy;
		this.gameObjects = gameObjects;
		this.puckImage = puckImage;
		this.collisionSound = collisionSound;
		this.puckDimensions = puckDimensions;
		this.ballSpeed = ballSpeed;
		this.windowHeight = windowHeight;
	}

	/**
	 * Executes the base collision strategy and spawns {@value #NUM_PUCKS} pucks
	 * at the center of the collided brick.
	 *
	 * @param thisObj  The brick that was collided with.
	 * @param otherObj The game object that collided with the brick.
	 */
	@Override
	public void onCollision(Brick thisObj, GameObject otherObj) {
		baseStrategy.onCollision(thisObj, otherObj);
		Vector2 brickCenter = thisObj.getCenter();
		for (int i = 0; i < NUM_PUCKS; i++) {
			Puck puck = createPuck(brickCenter);
			gameObjects.addGameObject(puck, Layer.DEFAULT);
		}
	}

	private Puck createPuck(Vector2 center) {
		Puck puck = new Puck(Vector2.ZERO, puckDimensions, puckImage, collisionSound,
				windowHeight, gameObjects);
		puck.setCenter(center);

		double angle = random.nextDouble() * Math.PI;
		float velocityX = (float) Math.cos(angle) * ballSpeed;
		float velocityY = (float) Math.sin(angle) * ballSpeed;
		puck.setVelocity(new Vector2(velocityX, velocityY));

		return puck;
	}
}