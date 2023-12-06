import java.awt.Color;
import java.util.List;
import java.util.Set;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.Key;

public class Snail {
    private double x, y;

    private Rectangle graphic;

    //current action state
    public static enum Movement{
        CRAWL,
        FALL
    }

    // the side that the snail is attached to
    public static enum Orientation {
        BOTTOM,
        LEFT,
        TOP,
        RIGHT
    }

    // public static enum Direction {
    //     LEFT,
    //     RIGHT
    // }

    Movement currentMovement;
    Orientation currentOrientation;

    private int velocity = 0;

    public Snail(Point snailPos, double size) {
        x = snailPos.getX();
        y = snailPos.getY();

        graphic = new Rectangle(x, y, size, size);
        graphic.setFillColor(Color.ORANGE);

        currentMovement = Movement.CRAWL;
        currentOrientation = Orientation.BOTTOM;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Movement getCurrentMovement(){
        return currentMovement;
    }

    public void move(Set<Key> keysPressed, List<Boolean> hitPoints){ 
        List<Boolean> possibleDirections = possibleDirections(hitPoints);

        boolean canLeft = possibleDirections.get(0);
        boolean canUp = possibleDirections.get(1);
        boolean canDown = possibleDirections.get(2);
        boolean canRight = possibleDirections.get(3);

        double nextX = x;
        double nextY = y;

        if(currentMovement == Movement.FALL && canDown){
            velocity += 2;
            nextY += velocity;
        }
        else{
            if(keysPressed.contains(Key.RIGHT_ARROW) && canRight){
                nextX += 1;
                currentMovement = Movement.CRAWL;
            }
            else if(keysPressed.contains(Key.LEFT_ARROW) && canLeft){
                nextX -= 1;
                currentMovement = Movement.CRAWL;
            }
            else if(keysPressed.contains(Key.DOWN_ARROW) && canDown){
                nextY +=1;
                currentMovement = Movement.CRAWL;
            }
            else if(keysPressed.contains(Key.UP_ARROW)&& canUp){ //
                nextY-= 1;
                currentMovement = Movement.CRAWL;
            }
            else if (keysPressed.contains(Key.SPACE) && canDown){
                velocity += 2;
                nextY += velocity;
                currentMovement = Movement.FALL;
            }
        }
        x = nextX;
        y = nextY;
        graphic.setPosition(x, y);
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

        if(currentOrientation == Snail.Orientation.BOTTOM){
            if(direction == Snail.Orientation.TOP){
                return false;
            }
        }

        if(currentOrientation == Snail.Orientation.LEFT){
            if(direction == Snail.Orientation.RIGHT){
                return false;
            }
        }

         if(currentOrientation == Snail.Orientation.RIGHT){
            if(direction == Snail.Orientation.LEFT){
                return false;
            }
        }

         if(currentOrientation == Snail.Orientation.TOP){
            if(direction == Snail.Orientation.BOTTOM){
                return false;
            }
        }

        if(currentOrientation == direction){
            return false;
        }

        if(hitPoints.get(midPoint)){
            return false;
        }

        return true;
    }


    public void orientSnail(List<Boolean> hitPoints, GraphicsGroup levelGraphics){
        if (currentOrientation != Snail.Orientation.BOTTOM && hitPoints.get(5)){
            currentOrientation = (Snail.Orientation.BOTTOM);
        }
        else if(isOnlyHit(hitPoints, 6)){
            GraphicsObject attachedObject = levelGraphics.getElementAt(getBoundaryPoints().get(6));
           
            //currently doesn't work
            if(currentOrientation == Snail.Orientation.LEFT){
               rotate(new Point(attachedObject.getX() + attachedObject.getWidth(), attachedObject.getY()), Snail.Orientation.BOTTOM);
            }

            //works
            else if(currentOrientation == Snail.Orientation.BOTTOM){
                rotate(new Point(attachedObject.getX() + attachedObject.getWidth(), attachedObject.getY()), Snail.Orientation.LEFT);
            }
        }
        //TO DO: add similar code for each corner
    }


    /*
     * Move the snail according the the new orientation and midpoint, the newSideMidpoint
     * being the midpoint of the side that is the newOrientation.
     */
    private void rotate(Point newSideMidpoint, Orientation newOrientation){
        if(newOrientation == Orientation.LEFT && currentOrientation == Orientation.BOTTOM){
            x = newSideMidpoint.getX();
            y = newSideMidpoint.getY() - graphic.getHeight()/2;
        }
        else if(newOrientation == Orientation.BOTTOM && currentOrientation == Orientation.LEFT){
            x = newSideMidpoint.getX() - graphic.getWidth()/2;
            y = newSideMidpoint.getY() - graphic.getHeight();
        }
        else if (newOrientation == Orientation.TOP && currentOrientation == Orientation.LEFT){
            x = newSideMidpoint.getX();
            y = newSideMidpoint.getY() - graphic.getHeight()/2;
        }
        else if (newOrientation == Orientation.LEFT && currentOrientation == Orientation.TOP){
            x = newSideMidpoint.getX() - graphic.getWidth()/2;
            y = newSideMidpoint.getY();
        }

        graphic.setPosition(x,y);
        currentOrientation = newOrientation;
    }

   
    private boolean isOnlyHit(List<Boolean> hitPoints, int index){
        for(int i = 0; i < hitPoints.size(); i++){
            if(i != index && hitPoints.get(i)){
                return false;
            }
        }
        return hitPoints.get(index);
    }

    /*
     * returns a list of points on the snail in this order:
     * top left, top middle, top right, right middle, bottom right, bottom middle, bottom left, left middle
     */
    public List<Point> getBoundaryPoints(){
        Point topLeft = new Point (x, y);
        Point top = new Point(x+graphic.getWidth()/2, y);
        Point topRight = new Point(x+graphic.getWidth(), y);
        Point right = new Point(x+graphic.getWidth(), y+graphic.getHeight()/2);
        Point bottomRight = new Point(x+graphic.getWidth(), y+graphic.getHeight());
        Point bottom = new Point(x+graphic.getWidth()/2, y+graphic.getHeight());
        Point bottomLeft =  new Point(x, y+graphic.getHeight());
        Point left = new Point(x, y+graphic.getHeight()/2);

        return List.of(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left);
    }
    
    public Rectangle getGraphics() {
        return graphic;
    }

    // public void setOrientation(Orientation newOrientation){
    //     currentOrientation = newOrientation;
    // }

    public Orientation getCurrentOrientation(){
        return currentOrientation;
    }
}
