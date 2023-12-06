import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsObject;
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
        
        snail = new Snail(new Point(CANVAS_WIDTH/2, CANVAS_HEIGHT/2), CANVAS_WIDTH/20);
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

            snail.orientSnail(hitPoints, currentLevel.getGraphics());
            snail.move(canvas.getKeysPressed(), hitPoints);
        });
    }

    // /*
    //  * returns a list of booleans representing the directions and whether or not
    //  * the snail can currently go that way based on obstacles
    //  */
    // private List<Boolean> possibleDirections(List<Boolean> hitPoints){
    //     boolean canLeft = canMoveDirection(hitPoints, Snail.Orientation.LEFT);
    //     boolean canUp = canMoveDirection(hitPoints, Snail.Orientation.TOP);
    //     boolean canDown = canMoveDirection(hitPoints, Snail.Orientation.BOTTOM);
    //     boolean canRight = canMoveDirection(hitPoints, Snail.Orientation.RIGHT);

    //     return List.of(canLeft, canUp, canDown, canRight);
    // }

    // private boolean canMoveDirection(List<Boolean> hitPoints, Snail.Orientation direction){
    //    int midPoint;

    //     if(direction == Snail.Orientation.LEFT){
    //         midPoint = 7;
    //     }
    //     else if (direction == Snail.Orientation.TOP){
    //         midPoint = 1;
    //     }
    //     else if (direction == Snail.Orientation.RIGHT){
    //         midPoint = 3;
    //     }
    //     else{
    //         midPoint = 5;
    //     }

    //     if(snail.getCurrentOrientation() == Snail.Orientation.BOTTOM){
    //         if(direction == Snail.Orientation.TOP){
    //             return false;
    //         }
    //     }

    //     if(snail.getCurrentOrientation() == Snail.Orientation.LEFT){
    //         if(direction == Snail.Orientation.RIGHT){
    //             return false;
    //         }
    //     }

    //      if(snail.getCurrentOrientation() == Snail.Orientation.RIGHT){
    //         if(direction == Snail.Orientation.LEFT){
    //             return false;
    //         }
    //     }

    //      if(snail.getCurrentOrientation() == Snail.Orientation.TOP){
    //         if(direction == Snail.Orientation.BOTTOM){
    //             return false;
    //         }
    //     }

    //     if(snail.getCurrentOrientation() == direction){
    //         return false;
    //     }

    //     if(hitPoints.get(midPoint)){
    //         return false;
    //     }

    //     return true;
    // }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}