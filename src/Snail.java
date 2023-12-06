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

    GraphicsObject attachedObject;

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
    Orientation facing;

    private int velocity = 0;

    public Snail(Point snailPos, double size, GraphicsObject startingOnShape) {
        x = snailPos.getX();
        y = snailPos.getY();

        graphic = new Rectangle(x, y, size, size);
        graphic.setFillColor(Color.ORANGE);

        currentMovement = Movement.CRAWL;
        currentOrientation = Orientation.BOTTOM;
        facing = Orientation.RIGHT;

        attachedObject = startingOnShape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updateAttachedObject(GraphicsGroup newAttachment){
        attachedObject = newAttachment;
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
                facing = Orientation.RIGHT;
            }
            else if(keysPressed.contains(Key.LEFT_ARROW) && canLeft){
                nextX -= 1;
                currentMovement = Movement.CRAWL;
                facing = Orientation.LEFT;
            }
            else if(keysPressed.contains(Key.DOWN_ARROW) && canDown){
                nextY +=1;
                currentMovement = Movement.CRAWL;
                facing = Orientation.BOTTOM;
            }
            else if(keysPressed.contains(Key.UP_ARROW)&& canUp){ //
                nextY-= 1;
                currentMovement = Movement.CRAWL;
                facing = Orientation.TOP;
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

        else if(!hitPoints.get(getBoundaryPoints().indexOf(middleBottom()))){ //if the middle bottom is no longer hitting
            if(currentOrientation == Orientation.BOTTOM){
                if(facing == Orientation.RIGHT){
                    rotate(new Point(attachedObject.getX() + attachedObject.getWidth(), attachedObject.getY()), Snail.Orientation.LEFT);
                }
                else if(facing == Orientation.LEFT){
                    rotate(attachedObject.getPosition(), Snail.Orientation.RIGHT);
                }
            }
            if(currentOrientation == Orientation.LEFT){
                if(facing == Orientation.TOP){
                    rotate(new Point(attachedObject.getX() + attachedObject.getWidth(), attachedObject.getY()), Snail.Orientation.BOTTOM);
                }
            }
        }
    }

  private Point middleBottom(){
        if(currentOrientation == Orientation.BOTTOM){
            return new Point(x + graphic.getWidth()/2, y + graphic.getHeight());
        }
        else if(currentOrientation == Orientation.TOP){
            return new Point(x + graphic.getWidth()/2, y);
        }
        else if(currentOrientation == Orientation.LEFT){
            return new Point(x, y + graphic.getHeight()/2);
        }
        else{
            return new Point(x + graphic.getWidth(), y + graphic.getHeight()/2);
        }
    }


    /*
     * Move the snail according the the new orientation and midpoint, the newSideMidpoint
     * being the midpoint of the side that is the newOrientation.
     * 
     * @param newSideMidpoint should be the corner of the block snail is on
     */
    private void rotate(Point newSideMidpoint, Orientation newOrientation){
        if(newOrientation == Orientation.LEFT && currentOrientation == Orientation.BOTTOM){
            x = newSideMidpoint.getX();
            y = newSideMidpoint.getY() - graphic.getHeight()/2;
           // facing = Orientation.BOTTOM;
        }
        else if(newOrientation == Orientation.BOTTOM && currentOrientation == Orientation.LEFT){
            x = newSideMidpoint.getX() - graphic.getWidth()/2;
            y = newSideMidpoint.getY() - graphic.getHeight();
           // facing = Orientation.LEFT;
        }
        else if (newOrientation == Orientation.TOP && currentOrientation == Orientation.LEFT){
            x = newSideMidpoint.getX();
            y = newSideMidpoint.getY() - graphic.getHeight()/2;
          //  facing = Orientation.LEFT;
        }
        else if (newOrientation == Orientation.LEFT && currentOrientation == Orientation.TOP){
            x = newSideMidpoint.getX() - graphic.getWidth()/2;
            y = newSideMidpoint.getY();
          //  facing = Orientation.TOP;
        }
        else if (newOrientation == Orientation.RIGHT && currentOrientation == Orientation.BOTTOM){
            x = newSideMidpoint.getX() - graphic.getWidth();
            y = newSideMidpoint.getY() - graphic.getHeight()/2;
        }

        graphic.setPosition(x,y);
        currentOrientation = newOrientation;
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
