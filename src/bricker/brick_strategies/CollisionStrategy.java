package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;

/**
 * Defines a strategy for handling collisions with a brick in the Bricker game.
 */
public interface CollisionStrategy {
	/**
	 * Executes the collision behavior when another game object collides with a brick.
	 *
	 * @param thisObj  The brick that was collided with.
	 * @param otherObj The other game object involved in the collision.
	 */
	void onCollision(Brick thisObj, GameObject otherObj);
}