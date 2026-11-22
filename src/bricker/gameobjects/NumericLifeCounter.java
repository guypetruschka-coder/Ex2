package bricker.gameobjects;

import bricker.lives.LivesObserver;
import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Displays the player's remaining lives as a colored numeric counter.
 * Updates its displayed number and color whenever notified of a change in lives.
 */
public class NumericLifeCounter extends GameObject implements LivesObserver {
	private static final int MANY_LIVES = 3;
	private static final int AVG_LIVES = 2;

	private final TextRenderable textRenderable;

	/**
	 * Constructs a new numeric life counter.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 * @param dimensions    Width and height in window coordinates.
	 */
	public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions) {
		super(topLeftCorner, dimensions, null);
		this.textRenderable = new TextRenderable("");
		this.renderer().setRenderable(textRenderable);
	}

	/**
	 * Updates the displayed lives count and adjusts the text color
	 * according to the remaining number of lives.
	 *
	 * @param lives The current number of lives.
	 */
	@Override
	public void onLivesChanged(int lives) {
		textRenderable.setString(Integer.toString(lives));
		if (lives >= MANY_LIVES) {
			textRenderable.setColor(Color.GREEN);
		} else if (lives == AVG_LIVES) {
			textRenderable.setColor(Color.YELLOW);
		} else { // Case player has low lives count
			textRenderable.setColor(Color.RED);
		}
	}
}