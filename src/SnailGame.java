import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;
import java.util.List;
import java.util.stream.Collectors;

public class SnailGame {
    CanvasWindow canvas;

    int CANVAS_WIDTH = 800;
    int CANVAS_HEIGHT = 800;

    private Snail snail;

    private Level currentLevel;


    public SnailGame() {
        canvas = new CanvasWindow("Snails", CANVAS_WIDTH, CANVAS_HEIGHT);
        
        currentLevel = new Level(CANVAS_WIDTH,CANVAS_HEIGHT);
        canvas.add(currentLevel.getGraphics());
        
        snail = new Snail(new Point(CANVAS_WIDTH/2, CANVAS_HEIGHT/2), CANVAS_WIDTH/20, currentLevel.getFirstElement());
        canvas.add(snail.getGraphics());

        canvas.draw();
        handleSnailMovement();
    }

    private void handleSnailMovement(){
        canvas.animate(() -> {
            List<Boolean> hitPoints = snail.getBoundaryPoints()
                                                    .stream()
                                                    .map(point -> currentLevel.checkCollision(point))
                                                    .collect(Collectors.toList());

            snail.move(canvas.getKeysPressed(), hitPoints);
        });
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}