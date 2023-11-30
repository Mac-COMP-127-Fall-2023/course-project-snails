public class Snail {
    private int x, y;
    
    private boolean attached;
    // the side that the snail is attached to
    enum Orientation {
        BOTTOM,
        LEFT,
        TOP,
        RIGHT
    }
    Orientation snailBottomOrientation;
    // current velocity while falling
    private int velocity = 0;
    public Snail(int[] snailPos) {
        x = snailPos[0];
        y = snailPos[1];
        snailBottomOrientation = Orientation.BOTTOM;
        attached = true;
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
     * Moves the snail according to its orientation when the right
     * arrow key is pressed. Then checks for collisions (unimplemented)
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
        SnailGame.checkCollisions();
    }
    /**
     * Accelerates the snail downwards and then checks for collisions
     */
    private void fall() {
        velocity+=2;
        y+=velocity;
        SnailGame.checkCollisions();
    }
    public void setAttached(boolean attached) {
        this.attached = attached;
    }
    public void setOrientation(Orientation newOrientation) {
        this.snailBottomOrientation = newOrientation;
    }
}
