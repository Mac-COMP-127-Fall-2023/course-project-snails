import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.Key;


import java.util.stream.Collectors;

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

    private int velocity = 0;

    public Snail(Point snailPos, double size) {
        x = snailPos.getX();
        y = snailPos.getY();

        graphic = new Rectangle(x, y, size, size);
        graphic.setFillColor(Color.ORANGE);

        currentMovement = Movement.CRAWL;
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

    public Point testMove(Set<Key> keysPressed, List<Boolean> possibleDirections){ 
        boolean canLeft = possibleDirections.get(0);
        boolean canUp = possibleDirections.get(1);
        boolean canDown = possibleDirections.get(2);
        boolean canRight = possibleDirections.get(3);

        double nextX = x;
        double nextY = y;

        if(currentMovement == Movement.FALL && canDown){
            velocity += 2;
            nextY += velocity;
            //graphic.setPosition(x,y);
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
        return new Point(nextX, nextY);
    }

    public void move(double newX, double newY){
        x = newX;
        y = newY;
        graphic.setPosition(x,y);
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

    public Rectangle getGraphics() {
        return graphic;
    }

    public List<Point> getBoundaryPoints(){
        return getTestBoundaryPoints(x, y);
    }

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

         //TO DO: transfer to: 
        // HashMap<String, Point> boundaryPoints = new HashMap<>();
        // boundaryPoints.put("top left", position);
        // boundaryPoints.put("top", new Point(graphic.getWidth()/2, 0));
        // boundaryPoints.put("top right", new Point(graphic.getWidth(), 0));
        // boundaryPoints.put("right", new Point(graphic.getWidth(), graphic.getHeight()/2));
        // boundaryPoints.put("bottom right", new Point(graphic.getWidth(), graphic.getHeight()));
        // boundaryPoints.put("bottom",new Point(graphic.getWidth()/2, graphic.getHeight()));
        // boundaryPoints.put("bottom left", new Point(0, graphic.getHeight()));
        // boundaryPoints.put("left",new Point(0, graphic.getHeight()/2));
    }
}
