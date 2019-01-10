package dot.empire.golgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dot.empire.golgdx.BaseEngine;

/**
 * Main file. Contains main method.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see dot.empire.golgdx.BaseEngine
 */
public final class DesktopLauncher {

    /**
     * @deprecated You can not instantiate this class.
     */
    @Deprecated
    private DesktopLauncher() {
    }

    /**
     * @param args Arguments from the command line
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        final LwjglApplicationConfiguration cfg =
                new LwjglApplicationConfiguration();

        cfg.title = BaseEngine.TAG;

        cfg.width = BaseEngine.WIDTH;
        cfg.height = BaseEngine.HEIGHT;

        cfg.samples = 0; // for clean edges
        // cfg.resizable = false;

        new LwjglApplication(new BaseEngine(), cfg);
    }
}
