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
        return getTestBoundaryPoints(x, y);
    }

    /*
     * returns a list of points that correspond to points on the snail based on a
     * theoretical position of the snail x, y
     */
    public List<Point> getTestBoundaryPoints(double x, double y){
        Point position = new Point (x, y);

        return List.of(
            position, //top left
            position.add(new Point(graphic.getWidth()/2, 0)), //top
            position.add(new Point(graphic.getWidth(), 0)), //top right
            position.add(new Point(graphic.getWidth(), graphic.getHeight()/2)), //right
            position.add(new Point(graphic.getWidth(), graphic.getHeight())), //bottom right
            position.add(new Point(graphic.getWidth()/2, graphic.getHeight())), //bottom
            position.add(new Point(0, graphic.getHeight())), //bottom left
            position.add(new Point(0, graphic.getHeight()/2)) //left
         );
    }
}
