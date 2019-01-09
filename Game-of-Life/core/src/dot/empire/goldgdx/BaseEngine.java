package dot.empire.goldgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
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
public final class BaseEngine extends ApplicationAdapter implements Disposable {

	/**
	 * Tag for logging.
	 */
	public static final String TAG = "Game-of-Life-GDX";

    /**
     * Width of window in pixels.
     */
    public static final int WIDTH = 1600;
    /**
     * Height of window in pixels.
     */
    public static final int HEIGHT = 900;

	private ShapeRenderer renderer;

	private boolean[][] cells;
	/**
	 * The dimensions of a single cell.
	 */
	private Vector2 dimensions;
    /**
     * Prints simulation frame per seconds (fps) to the console.
     */
    private FPSLogger fpsLogger;

    /**
     * Called on start up.
     */
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.fpsLogger = new FPSLogger();

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

    /**
     * @param dt Delta time
     */
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

    /**
     * Marks a cell as alive.
     *
     * @param xPos position of the cell on the X-axis
     * @param yPos position of the cell on the Y-axis
     */
    private void alive(final int xPos, final int yPos) {
        Gdx.app.postRunnable(() -> cells[xPos][yPos] = true);
    }

    /**
     * Marks a cell as dead.
     *
     * @param xPos position of the cell on the X-axis
     * @param yPos position of the cell on the Y-axis
     */
    private void die(final int xPos, final int yPos) {
        Gdx.app.postRunnable(() -> cells[xPos][yPos] = false);
    }

    /**
     * Used to render the simulation. Calls {@link #update(float)} method. Logs frames pre second.
     */
    @Override
    @SuppressWarnings("LibGDXProfilingCode")
    public void render() {
        this.fpsLogger.log();

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

    /**
     * Checks if a given dead cell should be alive or not. A dead cell should be alive if it has exactly 3 neighbours.
     *
     * @param xPos position of the cell on the X-axis
     * @param yPos position of the cell on the Y-axis
     *
     * @return true if the cell should be alive or not
     */
    private boolean isAlive(int xPos, int yPos) {
        return getNeighbours(xPos, yPos) == 3;
    }

    /**
     * Checks if a given alive cell should be dead or not due to under population.
     * A alive cell should die from under population if it has less than 2 neighbours.
     *
     * @param xPos position of the cell on the X-axis
     * @param yPos position of the cell on the Y-axis
     *
     * @return true if the cell should be alive or not
     */
    private boolean isAlone(int xPos, int yPos) {
        return getNeighbours(xPos, yPos) < 2;
    }

    /**
     * Checks if a given alive cell should be dead or not due to over population.
     * A alive cell should die from over crowding if it has more than 3 neighbours.
     *
     * @param xPos position of the cell on the X-axis
     * @param yPos position of the cell on the Y-axis
     *
     * @return true if the cell should be dead or not
     */
    private boolean isCrowded(int xPos, int yPos) {
        return getNeighbours(xPos, yPos) > 3;
    }

    /**
     * Checks the number of neighbours of a given cell.
     *
     * @param xPos position of the cell on the X-axis
     * @param yPos position of the cell on the Y-axis
     *
     * @see #getNeighbours(int, int, int, int)
     *
     * @return the number of neighbours of the cell
     */
    private int getNeighbours(int xPos, int yPos) {
        int num = 0; // number of neighbours

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

    /**
     * Releases all resources of this object. Called when the {@link Application} is destroyed.
     * Preceded by a call to {@link #pause()}.
     */
    @Override
    public void dispose() {
        this.renderer.dispose();
    }
}
