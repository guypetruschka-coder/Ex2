package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.gui.Sound;

/**
 * A collision strategy decorator that plays an explosion sound and triggers
 * collisions on the four orthogonal neighboring bricks in the grid upon collision,
 * in addition to executing the wrapped base strategy.
 */
public class ExplodingBrickStrategy implements CollisionStrategy {

	private static final int[][] NEIGHBOR_OFFSETS = {
			{-1, 0}, {1, 0}, {0, -1}, {0, 1}
	};
	private static final int ROW_OFFSET_INDEX = 0;
	private static final int COL_OFFSET_INDEX = 1;

	private final CollisionStrategy baseStrategy;
	private final Brick[][] bricksGrid;
	private final Sound explosionSound;

	/**
	 * Constructs a new ExplodingBrickStrategy instance.
	 *
	 * @param baseStrategy   The underlying collision strategy to execute first.
	 * @param bricksGrid     The 2D grid of bricks in the game, used to locate neighboring bricks.
	 * @param explosionSound The sound played when the brick explodes.
	 */
	public ExplodingBrickStrategy(CollisionStrategy baseStrategy, Brick[][] bricksGrid,
								  Sound explosionSound) {
		this.baseStrategy = baseStrategy;
		this.bricksGrid = bricksGrid;
		this.explosionSound = explosionSound;
	}

	/**
	 * Executes the base collision strategy, plays the explosion sound, and triggers
	 * collision handling on all valid orthogonal neighboring bricks.
	 *
	 * @param thisObj  The brick that was collided with.
	 * @param otherObj The game object that collided with the brick.
	 */
	@Override
	public void onCollision(Brick thisObj, GameObject otherObj) {
		baseStrategy.onCollision(thisObj, otherObj);
		explosionSound.play();
		int row = thisObj.getRow();
		int col = thisObj.getCol();

		for (int[] neighborOffset : NEIGHBOR_OFFSETS) {
			int neighborRow = row + neighborOffset[ROW_OFFSET_INDEX];
			int neighborCol = col + neighborOffset[COL_OFFSET_INDEX];
			if (isInBounds(neighborRow, neighborCol)) {
				Brick neighbor = bricksGrid[neighborRow][neighborCol];
				if (neighbor != null) {
					neighbor.onCollisionEnter(thisObj, null);
				}
			}
		}
	}

	private boolean isInBounds(int row, int col) {
		return row >= 0 && row < bricksGrid.length
				&& col >= 0 && col < bricksGrid[row].length;
	}
}