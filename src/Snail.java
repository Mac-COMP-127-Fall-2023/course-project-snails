import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

//JOIN THE ZOOM!!!
//https://us04web.zoom.us/j/79639985390?pwd=EidCbXO8abNWapFrzdHlQO5SvX6Vvc.1

public class Snail {
    private int x, y;
    private Level currentLevel;
    private Image currentImage;
    private int currentFrame = 0; // updateAnimation increments by 1
    private final List<Point> BOUNDS = List.of(
        new Point(0,0),
        new Point(16*SnailGame.SCREEN_PIXEL_RATIO,0),
        new Point(32*SnailGame.SCREEN_PIXEL_RATIO,0),
        new Point(32*SnailGame.SCREEN_PIXEL_RATIO,16*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(32*SnailGame.SCREEN_PIXEL_RATIO,32*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(16*SnailGame.SCREEN_PIXEL_RATIO,32*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(0,32*SnailGame.SCREEN_PIXEL_RATIO),
        new Point(0,16*SnailGame.SCREEN_PIXEL_RATIO)
    );
    //current animation state
    public static enum Appearance {
        CRAWLING, ROLLING,
        CURLING, UNCURLING,
        INSHELL, EXITING
    }
    // the side that the snail is attached to, can also be represented with an integer mod 8, 0 top left, clockwise
    public static enum Orientation {
        BOTTOM, LEFT,
        TOP, RIGHT
    }
    private Appearance currentAppearance = Appearance.CRAWLING;
    private Appearance lastAppearance;
    private Boolean falling = false;
    private Boolean crawled = false;
    private Orientation snailBottomOrientation = Orientation.BOTTOM; //the side that the snail is attached to
    private Orientation facing = Orientation.RIGHT; // the side that the snail is facing relative to it's surface
    private int velocity = 0; //velocity for falling
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

    /**
     * Update the snail's position and movement based on 
     * @param keysPressed and its current orientation, ways it can move, etc.
     */
    public boolean move(Set<Key> keysPressed){
        if(falling) {
            velocity += (velocity < 14) ? 2 : 0;
            fall(velocity);
        } else if (!keysPressed.isEmpty()) {
            handleInputs(keysPressed);
        }
        if (!crawled&&currentAppearance==Appearance.CRAWLING) {
            return false;
        }
        if (currentLevel.getCompleted()) {
            exit();
            SnailGame.win();
        }
        updateAnimation();
        return true;
    }

    public void exit(){
        currentAppearance = Appearance.EXITING;
    }

    /**
     * main logic that determines what happens when not falling
     */
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

    /**
     * first checks for inner corners, then checks outer corners, then moves if all is well
     */
    private void crawl() {
        crawled = true;
        
        int snailBottomIndex = orientationIndexMap(snailBottomOrientation);
        int facingCornerIndex = modulo8(snailBottomIndex + (facing==Orientation.LEFT ? 1 : -1));        
        if (currentLevel.checkCollision(getSnailFace(facingCornerIndex))) {
            turnInner();
            return;
        }
        int m = (facing==Orientation.LEFT ? -1 : 1); 
        m *= SnailGame.SCREEN_PIXEL_RATIO;
        int facingIndex = rotateOrientation(snailBottomOrientation, facing);
        Point p = getBoundaryPoint(snailBottomIndex);
        if (!currentLevel.checkCollision(p)) { //can rarely occur if it fell sideways near an edge
            velocityX=0;
            x+=3*m;
            curl();
            return;
        } else if (!currentLevel.checkCollision(nudge(p,facingIndex,1))) {
            turnOuter();
            return;
        }
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

    /**
     * velocityX is set elsewhere because curl is called after to start falling, and we need to set
     * an initial velocity if falling off platforms
     */
    private void curl() {
        currentAppearance = Appearance.CURLING;
        falling=true;
        velocity=0;
    }

    /**
     * way too much work to make sure it falls just enough and no more, probs could be better but it works
     * and will allow the falling snail to react to hitting walls and crap and bounce a bit
     * @param velocity tbh idk if this needs velocity as a parameter now i originally had a recursive thing
     */
    private void fall(int velocity) {
        sideCollisions();
        if (checkBelow(velocity)) {
            falling = false;
            velocity = belowFinder(velocity-1)+1; //a bit weird but it checks out
            if (velocity<0) {
                return;
            }
        }
        y += velocity *SnailGame.SCREEN_PIXEL_RATIO;
        x += velocityX*SnailGame.SCREEN_PIXEL_RATIO;
    }

    /**
     * @param distance to fall
     * @return if that collides
     */
    private Boolean checkBelow(int distance) {
        Point distanceVector = new Point(0, distance*SnailGame.SCREEN_PIXEL_RATIO);
        Point p = belowPoint();
        return currentLevel.checkCollision(p.add(distanceVector));
    }

    /**
     * helper for fall, recursively finds the first point that won't collide with the bottom center of the shell
     */
    private int belowFinder(int distance) {
        if (checkBelow(distance)) {
            distance=belowFinder(distance-1);
        }
        return distance;
    }

    /**
     * updates velocityX if the shell is colliding with a tile
     */
    private void sideCollisions() {
        List<Point> ps = getShellPoints();
        if (currentLevel.checkCollision(ps.get(0))) {
            velocityX += 1;
        } else if (currentLevel.checkCollision(ps.get(2))) {
            velocityX += 1;
        } else if (currentLevel.checkCollision(ps.get(1))) {
            velocityX -= 1;
        } else if (currentLevel.checkCollision(ps.get(3))) {
            velocityX -= 1;
        }
    }

    /**
     * @return the point at the bottom (absolute) center of the shell
     */
    public Point belowPoint() {
        List<Point> ps = getShellPoints();
        return (ps.get(0).subtract(ps.get(1))).scale(.5).add(ps.get(1));
    }

    /**
     * @return list of four corners of shell in some of the sprites in order bottom left, bottom right, top left, top right
     */
    public List<Point> getShellPoints() {
        return List.of(convertAbsolute(bottomLeftShell()), convertAbsolute(bottomRightShell()), convertAbsolute(topLeftShell()), convertAbsolute(topRightShell()));
    }

    /**
     * turns the snail sprite and orientation and adjusts the position accordingly for inner corners
     */
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

    /**
     * turns the snail sprite and orientation and adjusts the position accordingly for outer corners
     */
    private void turnOuter() {
        if (snailBottomOrientation==Orientation.BOTTOM) { //some junk for going off platforms
            Tile t = currentLevel.getCollidableTileAt(getBoundaryPoint(5));
            if (t!=null&&!t.canStickToSide()) {
                velocityX = 0;
                velocityX = (facing==Orientation.LEFT ? -2 : 2);
                curl();
            }
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

    
    /**
     * only ever called at the end of move if it's necessary, updates the sprite animation frame
     * its position, and if its reflected or not
     */ 
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
            currentImage.setScale(1, 1);
        }
        lastAppearance = currentAppearance;
    }

    public Image getGraphics() {
        return currentImage;
    }

    /**
     * @param i 0 top left, cw
     * @return relative point for snail sprite
     */
    private Point getBoundary(int i){
        return BOUNDS.get(modulo8(i));
    }

    /**
     * @param i 0 top left, cw
     * @return absolute point for snail sprite
     */
    private Point getBoundaryPoint(int i) {
        return convertAbsolute(getBoundary(i));
    }

    /**
     * @param i 0 top left, cw, the corner that's closest to the snails face
     * @return a point inside the snails beautiful face
     */
    private Point getSnailFace(int i){
        return convertAbsolute(nudge(getBoundary(i), i+4,4));
    }

    /**
     * @param p a point relative to the snail's top left
     * @return the absolute position of the point
     */
    private Point convertAbsolute(Point p) {
        return p.add(new Point(x,y));
    }

    /**
     * @param i 0 top left, cw
     * @return i mod 8 (i hate java)
     */
     private int modulo8(int i) {
        return (((i%8) + 8)%8);
    }

    /**
     * @param o Orientation
     * @return 0 top left, cw
     */
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

    /**
     * @param i 0 top left, cw, ONLY VALID IF i is odd, positive, less than 8! we only have 4 orientations
     * @return orientation
     */
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

    //helper for turning
    private int absoluteFacingIndex(){
        return rotateOrientation(snailBottomOrientation, facing);
    }

    //helper for turning
    private int reverseFacingIndex(){
        return rotateOrientation(snailBottomOrientation, facing==Orientation.LEFT ? Orientation.RIGHT : Orientation.LEFT);
    }

    /**
     * rotates the orientation by t. top rotated right gives you left. a snail attached to the left
     * going left is looking upwards, so left rotated left gives you up, etc...
     * @param o Orientation to rotate
     * @param t Orientation to rotate by, idk what the return value would mean if t wasn't left or right
     * @return an index, 0 top left, cw
     */
    private int rotateOrientation(Orientation o, Orientation t) { //idk what it gives you if you rotate around bottom or top
        return modulo8((orientationIndexMap(o) - orientationIndexMap(t) + 1));
    }

    /**
     * @return the absolute bottom left point on the shell in relative coordinates
     */
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
    
    /**
     * @return the absolute bottom right point on the shell in relative coordinates
     */
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

    /**
     * @return the absolute top left point on the shell in relative coordinates
     */
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
    
    /**
     * @return the absolute top right point on the shell in relative coordinates
     */
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
     * mostly a helper for nudge
     * @param indexTo 0 top left, cw
     * @param amount ingame pixels 
     */
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

    /**
     * mostly a helper for nudge
     * @param indexTo 0 top left, cw
     * @param amount ingame pixels
     */
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

    /**
     * nudges the point towards a direction, mostly used to correct the snail's position while turning
     * @param indexTo 0 top left, cw
     * @param amount in ingame pixels
     */
    private Point nudge(Point p, int indexTo, int amount){
        int dx = nudgeX(indexTo, amount);
        int dy = nudgeY(indexTo, amount);
        return p.add(new Point(dx,dy));
    }
}

