import edu.macalester.graphics.CanvasWindow;

public class SnailGame {
    CanvasWindow canvas;

    public static final int PIXEL_RATIO = 6;

    Level level1 = new Level("▛");
    Level level2 = new Level("""
▛▀▀▀▀▀▀▜
▌░░░░░░▐
▌░░░░░░▐
▙▄▄▄▄▄▄▟
    """);

    public SnailGame() {
        canvas = new CanvasWindow("Snails", 1920, 1080);
        canvas.add(level1.getGraphics());
        canvas.draw();
    }
    public static void main(String[] args) {
        SnailGame game = new SnailGame();
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
}
