import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

public class Snail {
    private int x, y;
    private boolean attached;

    private Image currentImage;
    private int currentFrame = 0; // update animation increments by 1

    //current animation state
    public static enum Appearance {
        CRAWLING,
        ROLLING,
        CURLING,
        UNCURLING,
        INSHELL
    }

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

    Appearance currentAppearance;
    Appearance lastAppearance;
    Movement currentMovement;
    Orientation snailBottomOrientation;
    Direction facing; //TO DO: write into code

    // current velocity while falling
    private int velocity = 0;

    public Snail(Point snailPos, double scale) {
        x = (int)snailPos.getX();
        y = (int)snailPos.getY();
        currentImage = new Image(x, y);
        currentImage.setScale(scale);
        currentAppearance = Appearance.CRAWLING;
        facing = Direction.RIGHT;
        currentMovement = Movement.CRAWL;
        snailBottomOrientation = Orientation.BOTTOM;
        attached = true;
        updateAnimation();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getAttached() {
        return attached;
    }

    public Orientation getOrientation() {
        return snailBottomOrientation;
    }

    public void move(Set<Key> keysPressed){
        if(currentMovement == Movement.FALL){
            fall();
        }
        else{
            if(keysPressed.contains(Key.RIGHT_ARROW)){
                crawl(1);
            }
            else if(keysPressed.contains(Key.LEFT_ARROW)){
                crawl(-1);
                
            }
            else if (keysPressed.contains(Key.SPACE)){
                curl();
            }
        }
    }

    /**
     * Moves the snail according to its orientation.
     */
    private void crawl(int m) {
        m *= SnailGame.SCREEN_PIXEL_RATIO;
        switch (snailBottomOrientation) {
            case BOTTOM:
                x+=m;
                break;
            case LEFT:
                y+=m;
                break;
            case TOP:
                x-=m;
                break;
            case RIGHT:
                y-=m;
                break;
        }
        currentImage.setPosition(x,y);

        updateAnimation();

        currentMovement = Movement.CRAWL;
        currentAppearance = Appearance.CRAWLING;
    }

    public void curl() {
        if (attached) {
            currentAppearance = Appearance.CURLING;
            currentMovement = Movement.FALL;
            fall();
        }
    }
    /**
     * Accelerates the snail downwards
     */
    private void fall() {
        velocity += 2;
        y += velocity;
        currentImage.setPosition(x,y);
        updateAnimation();
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public void updateAnimation() {
        String path = "Snail/";
        if (currentAppearance == lastAppearance) {
            currentFrame = ( currentFrame + 1 ) % 8; //cycles the frame if state didn't change
        } else {
            currentFrame = 0;
        }

        switch (currentAppearance) {
            case CRAWLING:
                path += "Crawl/";
                break;
            case ROLLING:
                path += "Roll/";
                break;
            case CURLING:
                path += "Curl/";
                if (currentFrame == 7) {
                    currentAppearance = Appearance.INSHELL;
                }
                break;
            case UNCURLING:
                path += "Uncurl/";
                break;
            case INSHELL:
                path += "Curl/"; //inshell is a special case when fully curled up
                currentFrame = 7;
                break;
        }
        path += currentFrame + ".png";
        currentImage.setImagePath(path);
        lastAppearance = currentAppearance;
    }

    public Image getGraphics() {
        return currentImage;
    }

    public Direction getFacing(){
        return facing;
    }

    public List<Point> getBoundryPoints(){
        return List.of(
            currentImage.getPosition(), //top right
            currentImage.getPosition().add(new Point(0, currentImage.getHeight()/2)), // right middle
            currentImage.getPosition().add(new Point(0, currentImage.getHeight())), //bottom right
            currentImage.getCenter().add(new Point(0, currentImage.getHeight()/2)), //bottom middle
            currentImage.getCenter().add(new Point(currentImage.getWidth()/2, currentImage.getHeight()/2)),//bottom left
            currentImage.getCenter().add(new Point(currentImage.getWidth()/2, 0)), //left middle
            currentImage.getCenter().add(new Point(currentImage.getWidth()/2, - currentImage.getHeight()/2)) //top left
         );
    }

    public void setOrientation(Orientation newOrientation) {
        this.snailBottomOrientation = newOrientation;
    }
}
