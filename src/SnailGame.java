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
            /*
             * evaluate a possible new position
             */
            Point newPos = snail.testMove(
                            canvas.getKeysPressed(), 
                            possibleDirections(snail.getBoundaryPoints()
                                                    .stream()
                                                    .map(point -> currentLevel.checkCollision(point))
                                                    .collect(Collectors.toList()))
                            );

            /*
             * if the possible new position follows the rules of the game (is either attached to something
             * or is falling), move the snail to the new position.
             */
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

    /*
     * determines whether there is an obstacle preventing the snail from moving 
     * in a certain direction
     * @param int side1, int middle, and int side2 represent the indexes in hitPoints of the 
     * 3 points on the particular side
     */
    private boolean canMoveDirection(List<Boolean> hitPoints, int side1, int middle, int side2){
        boolean can = true;

        if(hitPoints.get(middle)){
            can = false;
        }

        return can;
    }

    // private boolean allElseFalse(List<Boolean> hitPoints, int a, int b){
    //     for(int i = 0; i < hitPoints.size(); i++){
    //         if(i != a && i != b){
    //             if(hitPoints.get(i)){
    //                 return false;
    //             }
    //         }
    //     }
    //     return true;
    // }
    
    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}