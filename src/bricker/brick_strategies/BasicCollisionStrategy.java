package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Basic collision strategy that removes the brick from the game
 * and decrements the remaining bricks counter on collision.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;

	/**
	 * Constructs a new BasicCollisionStrategy.
	 *
	 * @param gameObjects   The game object collection from which bricks are removed.
	 * @param bricksCounter Counter tracking the number of remaining bricks in the game.
	 */
	public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricksCounter) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
	}

	/**
	 * Removes the collided brick from the static objects layer and decrements
	 * the bricks counter if the removal succeeded.
	 *
	 * @param thisObj  The brick that was collided with.
	 * @param otherObj The other game object involved in the collision.
	 */
	@Override
	public void onCollision(Brick thisObj, GameObject otherObj) {
		if (gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}
	}
}