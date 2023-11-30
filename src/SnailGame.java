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
}
