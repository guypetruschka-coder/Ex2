package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the player-controlled paddle in the Bricker game.
 * Moves horizontally based on keyboard input while staying within screen boundaries.
 */
public class Paddle extends GameObject {
	private static final float MOVEMENT_SPEED = 300f;

	private final UserInputListener inputListener;
	private final Vector2 windowDimensions;
	private final int minDistFromEdge;

	/**
	 * Constructs a new Paddle instance.
	 *
	 * @param topLeftCorner    Position of the paddle, in window coordinates (pixels).
	 * @param dimensions       Width and height of the paddle in window coordinates.
	 * @param renderable       The renderable representing the paddle.
	 * @param inputListener    Listener for reading player keyboard input.
	 * @param windowDimensions The dimensions of the game window.
	 * @param minDistFromEdge  Minimum allowed distance between the paddle and the screen edges.
	 */
	public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				  UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge) {
		super(topLeftCorner, dimensions, renderable);
		this.inputListener = inputListener;
		this.windowDimensions = windowDimensions;
		this.minDistFromEdge = minDistFromEdge;
	}

	/**
	 * Updates the paddle's velocity according to left/right arrow key input
	 * and clamps its position within the allowed screen boundaries.
	 *
	 * @param deltaTime The time elapsed, in seconds, since the last frame.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Vector2 movementDirection = Vector2.ZERO;

		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
			movementDirection = movementDirection.add(Vector2.LEFT);
		}
		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
			movementDirection = movementDirection.add(Vector2.RIGHT);
		}

		setVelocity(movementDirection.mult(MOVEMENT_SPEED));

		float maxRightX = windowDimensions.x() - minDistFromEdge - getDimensions().x();
		float x = getTopLeftCorner().x();

		if (x < minDistFromEdge) {
			Vector2 leftEdgeLocation = new Vector2(minDistFromEdge, getTopLeftCorner().y());
			setTopLeftCorner(leftEdgeLocation);
		} else if (x > maxRightX) {
			Vector2 rightEdgeLocation = new Vector2(maxRightX, getTopLeftCorner().y());
			setTopLeftCorner(rightEdgeLocation);
		}
	}
}