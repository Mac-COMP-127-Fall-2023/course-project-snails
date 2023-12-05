import java.awt.Color;
import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.Key;


import java.util.stream.Collectors;

public class Snail {
    private int x, y;

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
        x = (int)snailPos.getX();
        y = (int)snailPos.getY();

        graphic = new Rectangle(x, y, size, size);
        graphic.setFillColor(Color.ORANGE);

        currentMovement = Movement.CRAWL;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(Set<Key> keysPressed, List<Boolean> hitPoints, Level level){ 
        int nextX = x;
        int nextY = y;

        boolean canLeft = canMoveDirection(hitPoints, 0, 7, 6);
        boolean canUp = canMoveDirection(hitPoints, 0, 1, 2);
        boolean canDown = canMoveDirection(hitPoints, 6, 5, 4);
        boolean canRight = canMoveDirection(hitPoints, 2, 3, 4);

        if(currentMovement == Movement.FALL && canDown){
            fall();
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
                fall();
                currentMovement = Movement.FALL;
            }

            if(remainsAttached(level, nextX, nextY)){
                x = nextX;
                y = nextY;
                graphic.setPosition(x,y);
            }
        }
    }

    private boolean canMoveDirection(List<Boolean> hitPoints, int side1, int middle, int side2){
        boolean can = true;

        if(hitPoints.get(middle)){
            can = false;
        }

        return can;
    }

    private boolean remainsAttached(Level level, int testX, int testY){
        List<Boolean> testHitPoints = getTestBoundaryPoints(testX, testY).stream()
                                                    .map(p -> level.checkCollision(p))
                                                    .collect(Collectors.toList());
        if(testHitPoints.contains(true)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean allElseFalse(List<Boolean> hitPoints, int a, int b){
        for(int i = 0; i < hitPoints.size(); i++){
            if(i != a && i != b){
                if(hitPoints.get(i)){
                    return false;
                }
            }
        }
        return true;
    }

    private void fall() {
        velocity += 2;
        y += velocity;
        graphic.setPosition(x,y);
    }

    public Rectangle getGraphics() {
        return graphic;
    }

    public List<Point> getBoundaryPoints(){
        return getTestBoundaryPoints(x, y);
    }

    private List<Point> getTestBoundaryPoints(int x, int y){
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
