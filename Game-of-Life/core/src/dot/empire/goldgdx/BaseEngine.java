package dot.empire.goldgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Main game class for Game of Life GDX, a libGDX implementation of Conway's Game of life.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public class BaseEngine extends ApplicationAdapter implements Disposable {

	/**
	 * Tag for logging.
	 */
	public static final String TAG = "Game-of-Life-GDX";

	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;

	private ShapeRenderer renderer;

	private boolean[][] cells;
	/**
	 * The dimensions of a single cell.
	 */
	private Vector2 dimensions;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		this.renderer = new ShapeRenderer();
		this.renderer.setAutoShapeType(true);
		this.renderer.setColor(Color.BLACK);

		this.cells = new boolean[500][500];
		for (int x = 0; x < cells[0].length; x++) {
			for (int y = 0; y < cells[0].length; y++) {
				this.cells[x][y] = MathUtils.randomBoolean();
			}
		}
		this.dimensions = new Vector2(WIDTH / (float) cells[0].length, HEIGHT / (float) cells.length);
	}

	public void update(float dt) {
		for (int x = 0; x < cells[0].length; x++) {
			for (int y = 0; y < cells[0].length; y++) {
				if (cells[x][y]) {
					if (isCrowded(x, y) || isAlone(x, y)) {
						die(x, y);
					}
				} else {
					if (isAlive(x, y)) {
						alive(x, y);
					}
				}
			}
		}
	}

	private void alive(final int xPos, final int yPos) {
		Gdx.app.postRunnable(() -> {
			cells[xPos][yPos] = true;
		});
	}

	private void die(final int xPos, final int yPos) {
		Gdx.app.postRunnable(() -> {
			cells[xPos][yPos] = false;
		});
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1); // white

		final float dt = Gdx.graphics.getDeltaTime();
		this.update(dt);

		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

		this.renderer.begin(ShapeRenderer.ShapeType.Filled);
		{
			for (int x = 0; x < cells[0].length; x++) {
				for (int y = 0; y < cells[0].length; y++) {
					if (cells[x][y]) {
						this.renderer.rect(x * dimensions.x, y * dimensions.y, dimensions.x, dimensions.y);
					}
				}
			}
		}
		this.renderer.end();
	}

	private boolean isAlive(int xPos, int yPos) {
		return getNeighbours(xPos, yPos) == 3;
	}

	private boolean isAlone(int xPos, int yPos) {
		return getNeighbours(xPos, yPos) < 2;
	}

	private boolean isCrowded(int xPos, int yPos) {
		return getNeighbours(xPos, yPos) > 3;
	}

	private int getNeighbours(int xPos, int yPos) {
		int num = 0;

		num += getNeighbours(xPos, yPos, 1, 0);
		num += getNeighbours(xPos, yPos, -1, 0);

		num += getNeighbours(xPos, yPos, 0, 1);
		num += getNeighbours(xPos, yPos, 0, -1);

		num += getNeighbours(xPos, yPos, 1, 1);
		num += getNeighbours(xPos, yPos, 1, -1);

		num += getNeighbours(xPos, yPos, -1, 1);
		num += getNeighbours(xPos, yPos, -1, -1);

		return num;
	}

	private int getNeighbours(int xPos, int yPos, int xOffset, int yOffset) {
		try {
			return cells[xPos + xOffset][yPos + yOffset] ? 1 : 0;
		} catch (IndexOutOfBoundsException ignore) {
		}
		return 0;
	}

	@Override
	public void dispose() {
		this.renderer.dispose();
	}
}
