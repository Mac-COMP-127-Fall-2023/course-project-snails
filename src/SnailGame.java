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
        
        snail = new Snail(new Point(CANVAS_WIDTH/2, CANVAS_HEIGHT/2), CANVAS_WIDTH/20);
        canvas.add(snail.getGraphics());

        canvas.draw();
        handleSnailMovement();
    }

    private void handleSnailMovement(){
        canvas.animate(() -> {
            Point newPos = snail.testMove(
                            canvas.getKeysPressed(), 
                            possibleDirections(snail.getBoundaryPoints()
                                                    .stream()
                                                    .map(point -> currentLevel.checkCollision(point))
                                                    .collect(Collectors.toList()))
                            );
           if(remainsAttached(newPos.getX(), newPos.getY()) || snail.getCurrentMovement() == Snail.Movement.FALL){
                snail.move(newPos.getX(), newPos.getY());
            }
        });
    }

    /*
     * returns a list of booleans representing the directions and whether or not
     * the snail can currently go that way based on obstacles
     */
    private List<Boolean> possibleDirections(List<Boolean> hitPoints){
        boolean canLeft = canMoveDirection(hitPoints, 0, 7, 6);
        boolean canUp = canMoveDirection(hitPoints, 0, 1, 2);
        boolean canDown = canMoveDirection(hitPoints, 6, 5, 4);
        boolean canRight = canMoveDirection(hitPoints, 2, 3, 4);

        return List.of(canLeft, canUp, canDown, canRight);
    }

    /*
     * returns true if the snail would remain attached to an object if moved to 
     * position (testX, testY)
     */
    private boolean remainsAttached(double testX, double testY){
        List<Boolean> testHitPoints = snail.getTestBoundaryPoints(testX, testY).stream()
                                                    .map(p -> currentLevel.checkCollision(p))
                                                    .collect(Collectors.toList());
        if(testHitPoints.contains(true)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean canMoveDirection(List<Boolean> hitPoints, int side1, int middle, int side2){
        boolean can = true;

        if(hitPoints.get(middle)){
            can = false;
        }

        return can;
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}