import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

public class Snail {
    private int x, y;

    private Image currentImage;
    private int currentFrame = 0; // update animation increments by 1

    private Tile attachedTile;

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
    Appearance currentAppearance;
    Appearance lastAppearance;
    Movement currentMovement;
    Orientation snailBottomOrientation; //the side that the snail is attached to
    Orientation facing; // the side that the snail is facing relative to it's surface

    // current velocity while falling
    private int velocity = 0;

    public Snail(Point snailPos) {
        x = (int)snailPos.getX();
        y = (int)snailPos.getY();
        currentImage = new Image(0, 0);
        currentImage.setPosition(x,y);
        currentAppearance = Appearance.CRAWLING;
        facing = Orientation.RIGHT;
        currentMovement = Movement.CRAWL;
        snailBottomOrientation = Orientation.BOTTOM;
        updateAnimation();
        System.out.println(""+ x +" "+ y);
    }

    public void setAttachedTile(Tile newTile){
        attachedTile = newTile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Orientation getOrientation() {
        return snailBottomOrientation;
    }

    /**
     * we call this every animation frame, so this handles a bunch of the state logic.
     * i ended up removing space and adding the thing i was talking about with it curling up to turn around
     * i think it looks nice but we can change that if yall don't like it, no big deal
     * @param keysPressed
     */

    public void move(Set<Key> keysPressed){
        if (currentMovement==Movement.FALL) {
            fall();
        } else if (currentAppearance == Appearance.CURLING) {
            updateAnimation();
        } else if (currentAppearance == Appearance.UNCURLING) {
            updateAnimation();
        } else {
            Orientation inputDirection = Orientation.BOTTOM; // idt this is necessary haha but it made my linter not mad, just using it as null
            
            if (keysPressed.contains(Key.RIGHT_ARROW)) {
                inputDirection=Orientation.RIGHT;
            } else if (keysPressed.contains(Key.LEFT_ARROW)) {
                inputDirection=Orientation.LEFT;
            }

            if (inputDirection!=Orientation.BOTTOM) {
                if (currentAppearance == Appearance.INSHELL) {
                    facing = inputDirection;
                    snailBottomOrientation=Orientation.BOTTOM;
                    currentAppearance = Appearance.UNCURLING;
                } else if (facing == inputDirection) {
                    crawl();
                } else {
                    curl();
                }
            }
        }
    }

    /**
     * Moves the snail according to its orientation.
     */
    private void crawl() {
        if (currentMovement==Movement.FALL) {
            return;
        }
        int m = (facing==Orientation.LEFT ? -1 : 1); //if it's facing left/right relative to its surface we move accordingly
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

    private void curl() {
        currentAppearance = Appearance.CURLING;
        if (snailBottomOrientation!=Orientation.BOTTOM) {
            currentMovement = Movement.FALL;
            velocity=0;
        }
        updateAnimation();
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
                if (currentFrame == 7) {
                    currentAppearance = Appearance.CRAWLING;
                }
                break;
            case INSHELL:
                path += "Curl/"; //inshell is a special case when fully curled up
                currentFrame = 7;
                break;
        }
        path += "snail" + currentFrame + ".png";
        currentImage.setImagePath(path);
        
        if (facing==Orientation.LEFT) {
            currentImage.setScale(-1, 1);
        } else {
            currentImage.setScale(1);
        }

        lastAppearance = currentAppearance;
    }

    public Image getGraphics() {
        return currentImage;
    }

    public Orientation getFacing(){
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
