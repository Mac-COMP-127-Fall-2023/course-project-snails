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
        snail.setOrientation(Snail.Orientation.BOTTOM);
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

            determineOrientation(hitPoints);
            //System.out.println("Orientation: " + snail.getCurrentOrientation());
            snail.move(canvas.getKeysPressed(), possibleDirections(hitPoints));

            System.out.println("is hitting just right of bottom left: " + hitPoints.get(13));
        });
    }

    /*
     * returns a list of booleans representing the directions and whether or not
     * the snail can currently go that way based on obstacles
     */
    private List<Boolean> possibleDirections(List<Boolean> hitPoints){
        boolean canLeft = canMoveDirection(hitPoints, Snail.Orientation.LEFT);
        boolean canUp = canMoveDirection(hitPoints, Snail.Orientation.TOP);
        boolean canDown = canMoveDirection(hitPoints, Snail.Orientation.BOTTOM);
        boolean canRight = canMoveDirection(hitPoints, Snail.Orientation.RIGHT);

        return List.of(canLeft, canUp, canDown, canRight);
    }


    private void determineOrientation(List<Boolean> hitPoints){
        // if(snail.getCurrentOrientation() != Snail.Orientation.TOP &&hitPoints.get(1)){ 
        //    snail.setOrientation(Snail.Orientation.TOP);
        // }
        // else if (snail.getCurrentOrientation() != Snail.Orientation.RIGHT && hitPoints.get(3)){
        //     snail.setOrientation(Snail.Orientation.RIGHT);
        // }
         if (snail.getCurrentOrientation() != Snail.Orientation.BOTTOM && hitPoints.get(5)){
            snail.setOrientation(Snail.Orientation.BOTTOM);
        }
        // else if (snail.getCurrentOrientation() != Snail.Orientation.LEFT && hitPoints.get(7)){
        //     snail.setOrientation(Snail.Orientation.LEFT);
        // }

         else if(isOnlyHit(hitPoints, 0)){
            if(snail.getCurrentOrientation() == Snail.Orientation.LEFT){
                snail.setOrientation(Snail.Orientation.TOP);
            }
            else{
                snail.setOrientation(Snail.Orientation.LEFT);
            }
        }
        else if(isOnlyHit(hitPoints, 2)){
            if(snail.getCurrentOrientation() == Snail.Orientation.RIGHT){
                snail.setOrientation(Snail.Orientation.TOP);
            }
            else{
                snail.setOrientation(Snail.Orientation.LEFT);
            }
        }
        else if(isOnlyHit(hitPoints, 4)){
             if(snail.getCurrentOrientation() == Snail.Orientation.RIGHT){
                snail.setOrientation(Snail.Orientation.BOTTOM);
            }
            else{
                snail.setOrientation(Snail.Orientation.RIGHT);
            }
        }
         else if(isOnlyHit(hitPoints, 6)){
             if(snail.getCurrentOrientation() == Snail.Orientation.LEFT){
                snail.setOrientation(Snail.Orientation.BOTTOM);
            }
            else{
                snail.setOrientation(Snail.Orientation.LEFT);
            }
        }

        System.out.println("Orientation: " + snail.getCurrentOrientation());
    }

    private boolean isOnlyHit(List<Boolean> hitPoints, int index){
        System.out.println("entering isOnlyHit()");
        for(int i = 0; i < hitPoints.size(); i++){
            if(i != index && hitPoints.get(i)){
                System.out.println("there are multiple hits");
                return false;
            }
        }
        if (hitPoints.get(index) == true)System.out.println("something is the only hit");
        else System.out.println("there are no hits");
        return hitPoints.get(index);
    }

    /*
     * determines whether there is an obstacle preventing the snail from moving 
     * in a certain direction
     */
    private boolean canMoveDirection(List<Boolean> hitPoints, Snail.Orientation direction){
       int midPoint;

        if(direction == Snail.Orientation.LEFT){
            midPoint = 7;
        }
        else if (direction == Snail.Orientation.TOP){
            midPoint = 1;
        }
        else if (direction == Snail.Orientation.RIGHT){
            midPoint = 3;
        }
        else{
            midPoint = 5;
        }

        if(snail.getCurrentOrientation() == Snail.Orientation.BOTTOM){
            if(direction == Snail.Orientation.TOP){
                return false;
            }
        }

        if(snail.getCurrentOrientation() == Snail.Orientation.LEFT){
            if(direction == Snail.Orientation.RIGHT){
                return false;
            }
        }

         if(snail.getCurrentOrientation() == Snail.Orientation.RIGHT){
            if(direction == Snail.Orientation.LEFT){
                return false;
            }
        }

         if(snail.getCurrentOrientation() == Snail.Orientation.TOP){
            if(direction == Snail.Orientation.BOTTOM){
                return false;
            }
        }

        if(snail.getCurrentOrientation() == direction){
            return false;
        }

        if(hitPoints.get(midPoint)){
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}