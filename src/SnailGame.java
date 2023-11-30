import edu.macalester.graphics.CanvasWindow;

import java.awt.Dimension;
import java.awt.Toolkit;

public class SnailGame {
    CanvasWindow canvas;

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_PIXEL_RATIO = (int) SCREEN_SIZE.getWidth() / 320; //the size, in screen pixels, of a single in-game pixel 

    private static Level level1 = new Level("░░░░░");

    private static Level level2 = new Level("""
    ▍
    ▍
    ▍
    ▍░░░░░░░░
    ▙▃▃▃▃▃▃▃▟""");

    public SnailGame() {
        canvas = new CanvasWindow("Snails", (int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight());
        canvas.add(level2.getGraphics());
        canvas.draw();
    }
    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}
