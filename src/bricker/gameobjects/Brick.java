package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single brick in the Bricker game.
 * Delegates collision handling to its assigned {@link CollisionStrategy}.
 */
public class Brick extends GameObject {
	private final CollisionStrategy collisionStrategy;
	private final int row;
	private final int col;

	private boolean isBroken = false;

	/**
	 * Constructs a new Brick instance.
	 *
	 * @param topLeftCorner     Position of the brick, in window coordinates (pixels).
	 * @param dimensions        Width and height of the brick in window coordinates.
	 * @param renderable        The renderable representing the brick.
	 * @param collisionStrategy The strategy to execute when another object collides with this brick.
	 * @param row               The row index of the brick in the brick grid.
	 * @param col               The column index of the brick in the brick grid.
	 */
	public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				 CollisionStrategy collisionStrategy, int row, int col) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionStrategy = collisionStrategy;
		this.row = row;
		this.col = col;
	}

	/**
	 * Returns the row index of this brick in the brick grid.
	 *
	 * @return The brick's row index.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column index of this brick in the brick grid.
	 *
	 * @return The brick's column index.
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Called when another game object collides with this brick.
	 * Delegates the collision handling to the brick's collision strategy.
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (isBroken) {
			return;
		}
		isBroken = true;
		this.collisionStrategy.onCollision(this, other);
	}
}