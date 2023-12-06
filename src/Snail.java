import java.awt.Color;
import java.util.List;
import java.util.Set;

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

    public static enum Direction {
        LEFT,
        RIGHT
    }

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

    public void move(Set<Key> keysPressed, List<Boolean> possibleDirections){ 
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

    public Rectangle getGraphics() {
        return graphic;
    }

    public void setOrientation(Orientation newOrientation){
        currentOrientation = newOrientation;
    }

    public Orientation getCurrentOrientation(){
        return currentOrientation;
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

        double smallGap = 0.3;

        return List.of(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left,
            new Point(topLeft.getX() + smallGap, topLeft.getY()), // just to the right of top left (8)
            new Point(topRight.getX()-smallGap, topRight.getY()), //just to the left of top right (9)
            new Point(topRight.getX(), topRight.getY() + smallGap), //just below top right (10)
            new Point(bottomRight.getX(), bottomRight.getY()-smallGap), //just above bottom right (11)
            new Point(bottomRight.getX()-smallGap, bottomRight.getY()), //just to the left of bottom right (12)
            new Point(bottomLeft.getX()+ smallGap, bottomLeft.getY()), //just to the right of bottom left (13)
            new Point(bottomLeft.getX(), bottomLeft.getY()-smallGap), //just above bottom left
            new Point(topLeft.getX(), topLeft.getY() + smallGap) //just below top left
         );
    }
}
