package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the Bricker game that bounces off objects upon collision
 * and plays a collision sound.
 */
public class Ball extends GameObject {
	private final Sound collisionSound;

	/**
	 * Constructs a new Ball instance.
	 *
	 * @param topLeftCorner  Position of the ball, in window coordinates (pixels).
	 * @param dimensions     Width and height of the ball in window coordinates.
	 * @param renderable     The renderable representing the ball.
	 * @param collisionSound The sound to play when the ball collides with another object.
	 */
	public Ball(Vector2 topLeftCorner, Vector2 dimensions,
				Renderable renderable, Sound collisionSound) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionSound = collisionSound;
	}

	/**
	 * Called when the ball collides with another game object.
	 * Flips the ball's velocity according to the collision normal and plays the collision sound.
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		Vector2 newVel = getVelocity().flipped(collision.getNormal());
		setVelocity(newVel);
		this.collisionSound.play();
	}
}