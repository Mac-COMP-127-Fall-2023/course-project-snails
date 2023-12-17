import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

public class Snail {
    private int x, y;
    private Level currentLevel;

    private Image currentImage;
    private int currentFrame = 0; // update animation increments by 1

     //the Tile that the snail is currently attached to. Needed for rotation around said Tile.

    private List<Point> bounds = List.of(
        new Point(0,0),
        new Point(16*SnailGame.SCREEN_PIXEL_RATIO,0),
        new Point(32*SnailGame.SCREEN_PIXEL_RATIO,0),
        new Point(32*SnailGame.SCREEN_PIXEL_RATIO,16*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(32*SnailGame.SCREEN_PIXEL_RATIO,32*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(16*SnailGame.SCREEN_PIXEL_RATIO,32*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(0,32*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(0,16*SnailGame.SCREEN_PIXEL_RATIO)
    );

    // private List<Point> shellInsets = List.of(
    //     new Point(6*SnailGame.SCREEN_PIXEL_RATIO,19*SnailGame.SCREEN_PIXEL_RATIO), null,
    //     new Point(21*SnailGame.SCREEN_PIXEL_RATIO,19*SnailGame.SCREEN_PIXEL_RATIO), null,
    //     new Point(21*SnailGame.SCREEN_PIXEL_RATIO,0*SnailGame.SCREEN_PIXEL_RATIO), null,
    //     new Point(6*SnailGame.SCREEN_PIXEL_RATIO,0*SnailGame.SCREEN_PIXEL_RATIO), null
    // );

    //current animation state
    public static enum Appearance {
        CRAWLING,
        ROLLING,
        CURLING,
        UNCURLING,
        INSHELL,
        EXITING
    }

    // the side that the snail is attached to
    public static enum Orientation {
        BOTTOM,
        LEFT,
        TOP,
        RIGHT
    }
    Appearance currentAppearance = Appearance.CRAWLING;
    Appearance lastAppearance;
    Boolean falling = false;
    Boolean crawled = false;
    Orientation snailBottomOrientation = Orientation.BOTTOM; //the side that the snail is attached to
    Point middleOfOrientation; //the midpoint of the side of the snail that is attached
    Orientation facing = Orientation.RIGHT; // the side that the snail is facing relative to it's surface

    // current velocity while falling
    private int velocity = 0;
    private int velocityX = 0;

    /**
     * Create a snail, initializing it as crawling right-side-up, facing right, at
     * @param snailPos
     */
    public Snail(Level level, Point snailPos) {
        currentLevel = level;
        x = (int)snailPos.getX();
        y = (int)snailPos.getY();

        currentImage = new Image(x, y);
        currentAppearance = Appearance.CRAWLING;
        updateAnimation();
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
     * Update the snail's position and movement based on 
     * @param keysPressed and its current orientation, ways it can move, etc.
     */
    public void move(Set<Key> keysPressed){
        if(falling) {
            velocity += (velocity < 14) ? 2 : 0;
            fall(velocity);
        } else if (!keysPressed.isEmpty()) {
            handleInputs(keysPressed);
        }
        if (!crawled&&currentAppearance==Appearance.CRAWLING) {
            return;
        }
        if (currentLevel.getCompleted()) {
            exit();
            SnailGame.win();
        }
        updateAnimation();
    }
    public void exit(){
        currentAppearance = Appearance.EXITING;
    }

    private void handleInputs(Set<Key> keysPressed) {
        Orientation inputDirection;
        if (keysPressed.contains(Key.RIGHT_ARROW)) {
            inputDirection=Orientation.RIGHT;
        } else if (keysPressed.contains(Key.LEFT_ARROW)) {
            inputDirection=Orientation.LEFT;
        } else {
            return;
        }

        switch (currentAppearance) {
            case INSHELL:
                Point stuckInGround = bottomLeftShell();
                y -= (32*SnailGame.SCREEN_PIXEL_RATIO-stuckInGround.getY());
                if (snailBottomOrientation==Orientation.LEFT) {
                    x-= 4*SnailGame.SCREEN_PIXEL_RATIO;
                } else if (snailBottomOrientation==Orientation.RIGHT) {
                    x+= 4*SnailGame.SCREEN_PIXEL_RATIO;
                }
                facing = inputDirection;
                snailBottomOrientation=Orientation.BOTTOM;
                currentImage.setRotation(0);
                currentAppearance = Appearance.UNCURLING;
                break;
            case CRAWLING:
                if (facing == inputDirection) {
                    crawl();
                } else {
                    velocityX=0;
                    curl();
                }
                break; 
            default:
                break;
        }
    }

    private Point nudge(Point p, int indexTo, int amount){
        int dx = nudgeX(indexTo, amount);
        int dy = nudgeY(indexTo, amount);
        return p.add(new Point(dx,dy));
    }

    private int nudgeY(int indexTo, int amount){
        int dy = 0;
        switch (modulo8(indexTo)) {
            case 0:
                dy=-1;
                break;
            case 1:
                dy=-1;
                break;
            case 2:
                dy=-1;
                break;
            case 4:
                dy=1;
                break;
            case 5:
                dy=1;
                break;
            case 6:
                dy=1;
                break;
        }
        return dy*amount*SnailGame.SCREEN_PIXEL_RATIO;
    }
    private int nudgeX(int indexTo, int amount){
        int dx = 0;
        switch (modulo8(indexTo)) {
            case 0:
                dx=-1;
                break;
            case 2:
                dx=1;
                break;
            case 3:
                dx=1;
                break;
            case 4:
                dx=1;
                break;
            case 6:
                dx=-1;
                break;
            case 7:
                dx=-1;
                break;
        }
        return dx*amount*SnailGame.SCREEN_PIXEL_RATIO;
    }

    // moves the snail according to its orientation
    private void crawl() {
        crawled = true;
        
        int snailBottomIndex = orientationIndexMap(snailBottomOrientation);
        int facingCornerIndex = modulo8(snailBottomIndex + (facing==Orientation.LEFT ? 1 : -1));        
        if (currentLevel.checkCollision(getSnailFace(facingCornerIndex))) {
            turnInner();
            return;
        }
        
        int facingIndex = rotateOrientation(snailBottomOrientation, facing);
        if (!currentLevel.checkCollision(nudge(getBoundaryPoint(snailBottomIndex),facingIndex,1))) {
            turnOuter();
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
    }

    // curls the snail and sets it to falling
    private void curl() {
        currentAppearance = Appearance.CURLING;
        falling=true;
        velocity=0;
    }

    //moves the snail down as much as it can
    private void fall(int velocity) {
        sideCollisions();
        if (checkBelow(velocity)) {
            falling = false;
            velocity = belowFinder(velocity-1)+1;
            // velocityX = checkAside(velocityX) ? asideFinder(velocity) : 0;
            if (velocity<0) {
                return;
            }
        }
        y += velocity *SnailGame.SCREEN_PIXEL_RATIO;
        x += velocityX*SnailGame.SCREEN_PIXEL_RATIO;
    }

    private int belowFinder(int distance) {
        if (checkBelow(distance)) {
            distance=belowFinder(distance-1);
        }
        return distance;
    }

    private void sideCollisions() {
        List<Point> ps = getShellPoints();
        velocityX += currentLevel.checkCollision(ps.get(0)) ? 1 : 0;
        velocityX += currentLevel.checkCollision(ps.get(2)) ? 1 : 0;
        velocityX += currentLevel.checkCollision(ps.get(1)) ? -1 : 0;
        velocityX += currentLevel.checkCollision(ps.get(3)) ? -1 : 0;
    }
    private Boolean checkBelow(int distance) {
        Point distanceVector = new Point(0, distance*SnailGame.SCREEN_PIXEL_RATIO);
        List<Point> ps = getShellPoints();
        Point p = (ps.get(0).subtract(ps.get(1))).scale(.5).add(ps.get(1));
        return currentLevel.checkCollision(p.add(distanceVector));
    }

    /**
     * @return bl br tl tr
     */
    public List<Point> getShellPoints() {
        return List.of(convertAbsolute(bottomLeftShell()), convertAbsolute(bottomRightShell()), convertAbsolute(topLeftShell()), convertAbsolute(topRightShell()));
    }



    private void turnInner() {
        if (facing==Orientation.LEFT) {
            currentImage.rotateBy(90);   
        } else {
            currentImage.rotateBy(-90);
        }
        snailBottomOrientation = indexOrientationMap(absoluteFacingIndex());
        int facingCornerIndex = modulo8(orientationIndexMap(snailBottomOrientation) + (facing==Orientation.LEFT ? 1 : -1));        
        x += nudgeX(facingCornerIndex+4,4);
        y += nudgeY(facingCornerIndex+4, 4);
    }

    private void turnOuter() {
        Tile t = currentLevel.getCollidableTileAt(getBoundaryPoint(3));
            if (t!=null&&!t.canStickToSide()) {
                velocityX = 0;
                velocityX = (facing==Orientation.LEFT ? -1 : 1);
                curl();
                return;
            }

        if (facing==Orientation.LEFT) {
            currentImage.rotateBy(-90);
        } else {
            currentImage.rotateBy(90);
        }
        int facingCornerIndex = modulo8(orientationIndexMap(snailBottomOrientation) + (facing==Orientation.LEFT ? 1 : -1)); 
        snailBottomOrientation = indexOrientationMap(reverseFacingIndex());
        x += nudgeX(facingCornerIndex,16);
        y += nudgeY(facingCornerIndex, 16);

    }  

    
    // sets the graphic
    private void updateAnimation() {
        String path = "Snail/";
        if (currentAppearance == lastAppearance) {
            currentFrame = ( currentFrame + 1 ) % 8; //cycles the frame if state didn't change
        } else {
            currentFrame = 0;
        }

        switch (currentAppearance) {
            case CRAWLING:
                path += "Crawl/";
                crawled=false;
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
            case EXITING:
                path += "Exit/";
                if(currentFrame == 7){      //idt this is necessary since each level should have a new snail
                    currentAppearance = Appearance.CRAWLING;
                    falling=false;
                }
        }
        path += "snail" + currentFrame + ".png";
        currentImage.setImagePath(path);
        currentImage.setPosition(x,y);
        
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

    private Point getBoundary(int i){
        return bounds.get(modulo8(i));
    }

    private Point getBoundaryPoint(int i) {
        return convertAbsolute(getBoundary(i));
    }


    private Point getSnailFace(int i){
        return convertAbsolute(nudge(getBoundary(i), i+4,4));
    }
    private Point convertAbsolute(Point p) {
        return p.add(new Point(x,y));
    }

     private int modulo8(int i) {
        return (((i%8) + 8)%8);
    }

    private int orientationIndexMap(Orientation o) {
        switch (o) {
            case BOTTOM:
                return 5;
            case RIGHT:
                return 3;
            case LEFT:
                return 7;
            case TOP:
                return 1;
            default:
                throw new Error("whoops");
        }
    }

    private Orientation indexOrientationMap(int i) {
        switch (i) {
            case 1:
                return Orientation.TOP;
            case 3:
                return Orientation.RIGHT;
            case 5:
                return Orientation.BOTTOM;
            case 7:
                return Orientation.LEFT;
            default:
                throw new Error("oops");
        }
    }

    private int absoluteFacingIndex(){
        return rotateOrientation(snailBottomOrientation, facing);
    }

    private int reverseFacingIndex(){
        return rotateOrientation(snailBottomOrientation, facing==Orientation.LEFT ? Orientation.RIGHT : Orientation.LEFT);
    }

    private int rotateOrientation(Orientation o, Orientation t) { //idk what it gives you if you rotate around bottom or top
        return modulo8((orientationIndexMap(o) - orientationIndexMap(t) + 1));
    }

    private Point bottomLeftShell(){
        int px = 0;
        int py = 0;
        switch (snailBottomOrientation) {
            case BOTTOM:
                py=32;
                px = (facing==Orientation.LEFT ? 11 : 6);
                break;
            case LEFT:
                px=1;
                py = 32 - (facing==Orientation.LEFT ? 6 : 11);
                break;
            case TOP:
                py = 13;
                px = (facing==Orientation.LEFT ? 6 : 11);
                break;
            case RIGHT:
                px= 19;
                py = 32 - (facing==Orientation.LEFT ? 11 : 6);
                break;
        }
        return new Point(px*SnailGame.SCREEN_PIXEL_RATIO, py*SnailGame.SCREEN_PIXEL_RATIO);
    }
    
    private Point bottomRightShell(){
        int px = 0;
        int py = 0;
        switch (snailBottomOrientation) {
            case BOTTOM:
                py=32;
                px = 32 - (facing==Orientation.LEFT ? 6 : 11);
                break;
            case LEFT:
                px=13;
                py = 32 - (facing==Orientation.LEFT ? 6 : 11);
                break;
            case TOP:
                py = 13;
                px = 32 - (facing==Orientation.LEFT ? 11 : 6);
                break;
            case RIGHT:
                px= 31;
                py = 32 - (facing==Orientation.LEFT ? 11 : 6);
                break;
        }
        return new Point(px*SnailGame.SCREEN_PIXEL_RATIO, py*SnailGame.SCREEN_PIXEL_RATIO);
    }

    private Point topLeftShell(){
        int px = 0;
        int py = 0;
        switch (snailBottomOrientation) {
            case BOTTOM:
                py=19;
                px = (facing==Orientation.LEFT ? 11 : 6);
                break;
            case LEFT:
                px=1;
                py = (facing==Orientation.LEFT ? 11 : 6);
                break;
            case TOP:
                py = 1;
                px = (facing==Orientation.LEFT ? 6 : 11);
                break;
            case RIGHT:
                px= 19;
                py = (facing==Orientation.LEFT ? 6 : 11);
                break;
        }
        return new Point(px*SnailGame.SCREEN_PIXEL_RATIO, py*SnailGame.SCREEN_PIXEL_RATIO);
    }
    
    private Point topRightShell(){
        int px = 0;
        int py = 0;
        switch (snailBottomOrientation) {
            case BOTTOM:
                py=19;
                px = 32 - (facing==Orientation.LEFT ? 6 : 11);
                break;
            case LEFT:
                px=13;
                py = (facing==Orientation.LEFT ? 11 : 6);
                break;
            case TOP:
                py = 1;
                px = 32 - (facing==Orientation.LEFT ? 11 : 6);
                break;
            case RIGHT:
                px= 31;
                py = (facing==Orientation.LEFT ? 6 : 11);
                break;
        }
        return new Point(px*SnailGame.SCREEN_PIXEL_RATIO, py*SnailGame.SCREEN_PIXEL_RATIO);
    }
    
}
