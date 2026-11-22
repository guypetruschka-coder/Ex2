package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.GraphicLifeCounter;
import bricker.gameobjects.NumericLifeCounter;
import bricker.gameobjects.Paddle;
import bricker.lives.Lives;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Manages the Bricker game, including initialization of all game objects,
 * the game loop, and win/loss conditions.
 */
public class BrickerGameManager extends GameManager {
	private static final String WINDOW_TITLE = "Bricker";
	private static final float WINDOW_WIDTH = 700f;
	private static final float WINDOW_HEIGHT = 500f;
	private static final float WALL_THICKNESS = 10f;
	private static final float BALL_SIZE = 20f;
	private static final float BALL_SPEED = 200f;
	private static final float PADDLE_WIDTH = 100f;
	private static final float PADDLE_HEIGHT = 15f;
	private static final float PADDLE_BOTTOM_OFFSET = 30f;
	private static final float BRICK_HEIGHT = 15f;
	private static final float BRICK_SPACING = 2f;
	private static final float HEART_SIZE = 20f;
	private static final float UI_PADDING = 5f;
	private static final float MID_FACTOR = 0.5f;
	private static final int DEFAULT_BRICK_ROWS = 7;
	private static final int DEFAULT_AMOUNT_OF_BRICKS_IN_ROW = 8;
	private static final int EXPECTED_ARGS_COUNT = 2;
	private static final int AMOUNT_OF_BRICKS_IN_ROW_INDEX = 0;
	private static final int BRICK_ROWS_INDEX = 1;
	private static final int INITIAL_LIVES = 3;
	private static final int MAX_LIVES = 4;
	private static final String WIN_PROMPT = "You win! Play again?";
	private static final String LOSE_PROMPT = "You lose! Play again?";

	// paths
	private static final String DARK_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
	private static final String BALL_IMAGE_PATH = "assets/ball.png";
	private static final String BLOP_SOUND_PATH = "assets/blop.wav";
	private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
	private static final String BRICK_IMAGE_PATH = "assets/brick.png";
	private static final String HEART_IMAGE_PATH = "assets/heart.png";

	private final int amountOfBricksInRow;
	private final int brickRows;

	private WindowController windowController;
	private Vector2 windowDimensions;
	private UserInputListener inputListener;
	private Ball ball;

	private final Random random = new Random();

	private Counter bricksCounter;

	private Lives lives;

	/**
	 * Constructs a new BrickerGameManager with a custom brick grid layout.
	 *
	 * @param windowTitle         The title of the game window.
	 * @param windowDimensions    The dimensions of the game window in pixels.
	 * @param amountOfBricksInRow The number of bricks in each row.
	 * @param brickRows           The number of brick rows.
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int amountOfBricksInRow,
							  int brickRows) {
		super(windowTitle, windowDimensions);
		this.brickRows = brickRows;
		this.amountOfBricksInRow = amountOfBricksInRow;
	}

	/**
	 * Constructs a new BrickerGameManager with the default brick grid layout.
	 *
	 * @param windowTitle      The title of the game window.
	 * @param windowDimensions The dimensions of the game window in pixels.
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
		this(windowTitle, windowDimensions, DEFAULT_AMOUNT_OF_BRICKS_IN_ROW,
				DEFAULT_BRICK_ROWS); // Calls the other constructor
	}

	/**
	 * Initializes the game by creating the background, walls, ball, paddle, bricks,
	 * and life counters.
	 *
	 * @param imageReader      Contains a single method: readImage, which reads an image from disk.
	 * @param soundReader      Contains a single method: readSound, which reads a wav file from disk.
	 * @param inputListener    Contains a single method: isKeyPressed, which returns whether a given
	 *                         key is currently pressed by the user.
	 * @param windowController Contains an array of helpful, self-explanatory methods concerning
	 *                         the window.
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.windowController = windowController;
		windowDimensions = windowController.getWindowDimensions();
		this.inputListener = inputListener;
		createBackground(imageReader);
		createWalls();
		createBall(imageReader, soundReader);
		createPaddle(imageReader, inputListener);
		createBricks(imageReader);
		createLivesCounters(imageReader);
	}

	/**
	 * Updates the game state each frame, checking for life loss, game over, and win conditions.
	 *
	 * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (ball.getCenter().y() > windowDimensions.y()) {
			lives.decrease();
			if (lives.getLives() > 0) {
				resetBall();
			} else {
				handleGameEnd(LOSE_PROMPT);
			}
		} else if (bricksCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
			handleGameEnd(WIN_PROMPT);
		}
	}

	private void handleGameEnd(String prompt) {
		if (windowController.openYesNoDialog(prompt)) {
			windowController.resetGame();
		} else {
			windowController.closeWindow();
		}
	}

	private void createBackground(ImageReader imageReader) {
		Renderable backgroundImage = imageReader.readImage(DARK_IMAGE_PATH, false);
		GameObject background = new GameObject(Vector2.ZERO,
				windowDimensions, backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	private void createWalls() {
		Vector2 horizontalWallDimensions = new Vector2(windowDimensions.x(), WALL_THICKNESS);
		Vector2 verticalWallDimensions = new Vector2(WALL_THICKNESS, windowDimensions.y());

		GameObject leftWall = new GameObject(Vector2.ZERO, verticalWallDimensions, null);

		Vector2 rightWallLocation = new Vector2(windowDimensions.x() - WALL_THICKNESS, 0);
		GameObject rightWall = new GameObject(rightWallLocation, verticalWallDimensions, null);

		GameObject upperWall = new GameObject(Vector2.ZERO, horizontalWallDimensions, null);

		gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
		gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
		gameObjects().addGameObject(upperWall, Layer.STATIC_OBJECTS);
	}

	private void createBall(ImageReader imageReader, SoundReader soundReader) {
		Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
		Sound ballSound = soundReader.readSound(BLOP_SOUND_PATH);
		Vector2 ballSize = new Vector2(BALL_SIZE, BALL_SIZE);
		this.ball = new Ball(Vector2.ZERO,
				ballSize, ballImage, ballSound);

		resetBall();

		gameObjects().addGameObject(ball, Layer.DEFAULT);
	}

	private void resetBall() {
		this.ball.setCenter(windowDimensions.mult(MID_FACTOR));

		float velX;
		float velY;

		if (random.nextBoolean()) {
			velX = BALL_SPEED;
		} else {
			velX = -BALL_SPEED;
		}

		if (random.nextBoolean()) {
			velY = BALL_SPEED;
		} else {
			velY = -BALL_SPEED;
		}

		ball.setVelocity(new Vector2(velX, velY));
	}

	private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
		Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
		Vector2 paddleDimensions = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
		Paddle paddle = new Paddle(Vector2.ZERO, paddleDimensions, paddleImage, inputListener,
				windowDimensions, (int) WALL_THICKNESS);
		paddle.setCenter(new Vector2(windowDimensions.mult(MID_FACTOR).x(),
				windowDimensions.y() - PADDLE_BOTTOM_OFFSET));

		gameObjects().addGameObject(paddle, Layer.DEFAULT);
	}

	private void createBricks(ImageReader imageReader) {
		Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
		float totalAvailableWidth = windowDimensions.x() - 2 * WALL_THICKNESS
				- (amountOfBricksInRow + 1) * BRICK_SPACING; // Need to count for the thickness
		// of the left and right wall and the space inbetween bricks (Also left to the most left
		// brick and right to the most right brick)
		float brickWidth = totalAvailableWidth / amountOfBricksInRow;
		Vector2 brickDimensions = new Vector2(brickWidth, BRICK_HEIGHT);
		this.bricksCounter = new Counter(0);
		CollisionStrategy collisionStrategy = new BasicCollisionStrategy(gameObjects(),
				bricksCounter);

		for (int r = 0; r < brickRows; r++) {
			for (int c = 0; c < amountOfBricksInRow; c++) {
				float x = WALL_THICKNESS + BRICK_SPACING + c * (brickWidth + BRICK_SPACING);
				float y = WALL_THICKNESS + BRICK_SPACING + r * (BRICK_HEIGHT + BRICK_SPACING);
				Vector2 brickLocation = new Vector2(x, y);
				Brick brick = new Brick(brickLocation, brickDimensions, brickImage,
						collisionStrategy, r, c);

				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
				bricksCounter.increment();
			}
		}
	}

	private void createLivesCounters(ImageReader imageReader) {
		this.lives = new Lives(INITIAL_LIVES, MAX_LIVES);
		Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
		Vector2 numericLifeCounterLocation = new Vector2(WALL_THICKNESS + UI_PADDING,
				windowDimensions.y() - HEART_SIZE - UI_PADDING);
		Vector2 graphicLifeCounterLocation =
				new Vector2(numericLifeCounterLocation.x() + HEART_SIZE + UI_PADDING,
						windowDimensions.y() - HEART_SIZE - UI_PADDING);
		NumericLifeCounter numericLifeCounter =
				new NumericLifeCounter(numericLifeCounterLocation,
						Vector2.ONES.mult(HEART_SIZE));
		GraphicLifeCounter graphicLifeCounter =
				new GraphicLifeCounter(graphicLifeCounterLocation,
						Vector2.ONES.mult(HEART_SIZE), heartImage, gameObjects());

		lives.registerObserver(numericLifeCounter);
		lives.registerObserver(graphicLifeCounter);

		gameObjects().addGameObject(numericLifeCounter, Layer.UI);
		gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
	}

	/**
	 * Entry point of the Bricker game. Accepts optional command-line arguments for
	 * the number of bricks per row and the number of rows.
	 *
	 * @param args Optional command-line arguments: [bricksPerRow, brickRows].
	 */
	public static void main(String[] args) {
		Vector2 windowDimensions = new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT);
		BrickerGameManager brickerGameManager;

		if (args.length == EXPECTED_ARGS_COUNT) {
			final int amountOfBricksInRow = Integer.parseInt(args[AMOUNT_OF_BRICKS_IN_ROW_INDEX]);
			final int brickRows = Integer.parseInt(args[BRICK_ROWS_INDEX]);
			brickerGameManager = new BrickerGameManager(WINDOW_TITLE,
					windowDimensions, amountOfBricksInRow, brickRows);
		} else {
			brickerGameManager = new BrickerGameManager(WINDOW_TITLE,
					windowDimensions);
		}

		brickerGameManager.run();
	}
}