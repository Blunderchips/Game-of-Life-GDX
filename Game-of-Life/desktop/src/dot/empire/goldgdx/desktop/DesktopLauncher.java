package dot.empire.goldgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dot.empire.goldgdx.BaseEngine;

/**
 * Main file. Contains main method.
 */
public final class DesktopLauncher {

    private DesktopLauncher() {
    }

    /**
     * @param args Arguments from the command line
     */
    public static void main(String[] args) {
        final LwjglApplicationConfiguration cfg =
                new LwjglApplicationConfiguration();

        cfg.title = BaseEngine.TAG;
        cfg.width = BaseEngine.WIDTH;
        cfg.height = BaseEngine.HEIGHT;

        cfg.samples = 32;

        new LwjglApplication(new BaseEngine(), cfg);
    }
}
