import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public class Snail {
    private int x, y;
    private boolean attached;

    private Image currentImage;
    private int currentFrame = 1;
    private String currentPath;

    //current action state--corresponds to animation state
    enum State {
        CRAWLING,
        ROLLING,
        CURLED,
        UNCURLED
    }
    // the side that the snail is attached to
    enum Orientation {
        BOTTOM,
        LEFT,
        TOP,
        RIGHT
    }
    State currentState;
    State lastState;
    Orientation snailBottomOrientation;
    // current velocity while falling
    private int velocity = 0;
    public Snail(Point snailPos) {
        x = (int)snailPos.getX();
        y = (int)snailPos.getY();
        currentImage = new Image(x, y);
        currentState = State.CRAWLING;
        // lastState = currentState;
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

    public void moveRight() {
        move(1);
    }

    public void moveLeft() {
        move(-1);
    }

    /**
     * Moves the snail according to its orientation.
     */
    private void move(int m) {
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
    }

    /**
     * Accelerates the snail downwards
     */
    private void fall() {
        velocity+=2;
        y+=velocity;
       // SnailGame.checkCollisions(); I don't think this should be in Snail
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public void updateAnimation() {
        if ((currentState == State.CRAWLING)) {
            currentPath = "Snail/Crawl/snail_crawl";
        }

        else if ((currentState == State.ROLLING)) {
            currentPath = "Snail/Roll/snail_roll";
        }
        
        else if ((currentState == State.CURLED)) {
            currentPath = "Snail/Curl/snail_curl";
        }
        
        else if ((currentState == State.UNCURLED)) {
            currentPath = "Snail/Uncurl/snail_uncurl";
        }


        currentFrame = currentFrame >= 8 || currentState != lastState ? 1 : currentFrame + 1;
        currentImage.setImagePath(currentPath + currentFrame + ".png");
        lastState = currentState;
    }

    public Image getGraphics() {
        return currentImage;
    }


    public void setOrientation(Orientation newOrientation) {
        this.snailBottomOrientation = newOrientation;
    }
}
