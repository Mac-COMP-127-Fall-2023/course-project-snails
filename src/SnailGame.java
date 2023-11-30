import edu.macalester.graphics.CanvasWindow;

import java.awt.Dimension;
import java.awt.Toolkit;

public class SnailGame {
    CanvasWindow canvas;

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_PIXEL_RATIO = (int) SCREEN_SIZE.getWidth() / 320; //the size, in screen pixels, of a single in-game pixel 

    private Snail snail;

    private static Level level1 = new Level("░░░░░");

    private static Level level2 = new Level("""
    ▍
    ▍
    ▍
    ▍S░░░░░░░
    ▙▃▃▃▃▃▃▃▟""");

    public SnailGame(Level curLevel) {
        canvas = new CanvasWindow("Snails", (int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight());
        canvas.add(curLevel.getGraphics());
        snail = new Snail(curLevel.getSnailPos());
        canvas.draw();
    }

        /**
     * called after our snail moves or falls, should check collisions
     * and update its orientation if necessary. **We also need to check
     * if one pixel below the center of the snail bottom is no longer
     * colliding with a tile, this means that it is most of the way up
     * a convex portion of the level, so we should rotate it the rest
     * of the way so it doesn't look weird.** 
     * idk if im explaining that poorly lol and there's probably a 
     * better way to do that
     */
    public static void checkCollisions() {
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame(level2);
    }
}
