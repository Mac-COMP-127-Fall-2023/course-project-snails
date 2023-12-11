import java.util.List;
import java.util.Set;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

public class Snail {
    private int x, y;

    private Image currentImage;
    private int currentFrame = 0; // update animation increments by 1

     //the Tile that the snail is currently attached to. Needed for rotation around said Tile.
    private Tile attachedTile;

    /*
     * Corresponding to the 8 border points of the snail,
     * true if hitting something.
     */
    private List<Boolean> hitPoints;

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
    Point middleOfOrientation; //the midpoint of the side of the snail that is attached
    Orientation facing; // the side that the snail is facing relative to it's surface

    // current velocity while falling
    private int velocity = 0;

    public Snail(Point snailPos) {
        x = (int)snailPos.getX();
        y = (int)snailPos.getY();

        currentImage = new Image(x, y);
        currentImage.setScale((double)SnailGame.SCREEN_PIXEL_RATIO / 6);

        currentAppearance = Appearance.CRAWLING;
        facing = Orientation.RIGHT;
        currentMovement = Movement.CRAWL;

        setOrientation(Orientation.BOTTOM);
        
        updateAnimation();
    }

    public void updateAttachedTile(Tile newTile){
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

    public void setHitPoints(List<Boolean> hitPoints){
        this.hitPoints = hitPoints;
    }

    public Point getMiddleOfOrientation(){
        return middleOfOrientation;
    }

    /**
     * Update the snail's position and movement based on 
     * @param keysPressed and its current orientation, ways it can move, etc.
     */
    public void move(Set<Key> keysPressed){
        //this ensures that the snail does not fall *into* a Tile more than it should
        if(snailBottomOrientation == Orientation.BOTTOM && !canMoveDirection(Orientation.BOTTOM)){
            y = (int)(attachedTile.getTopLeftCorner().getY() - currentImage.getHeight());
            currentImage.setPosition(x,y);
        }
        if(currentMovement==Movement.FALL) {
            if(canMoveDirection(Orientation.BOTTOM)){
                fall();
            }
           else{
                turn(Orientation.BOTTOM);
                currentMovement = Movement.CRAWL;
           }
        } 
        else if (currentAppearance == Appearance.CURLING) {
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
        setOrientation(snailBottomOrientation);
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
                if(facing == Orientation.RIGHT){
                    //if on the edge of something, rotate to stay on
                    if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getTopRightCorner(), Orientation.LEFT);
                    }
                    else if(canMoveDirection(Orientation.RIGHT)){
                          x+=m;
                    }
                    //if hitting something, move onto it
                    else{
                        turn(Orientation.RIGHT);
                    }
                }  
                else if(facing == Orientation.LEFT){
                   if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getTopLeftCorner(), Orientation.RIGHT);
                    }
                    else if(canMoveDirection(Orientation.LEFT)){
                          x+=m;
                    }
                    else{
                        turn(Orientation.LEFT);
                    }
                }
                break;

            case LEFT:
                if(facing == Orientation.RIGHT){ //going down
                    if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getBottomRightCorner(), Orientation.TOP);
                    }
                    else if(canMoveDirection(Orientation.BOTTOM)){
                            y+=m;
                    }
                    else{
                        turn(Orientation.BOTTOM);
                    }
                }
                else if(facing == Orientation.LEFT){  //going up
                    if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getTopRightCorner(), Orientation.BOTTOM);
                    }
                    else if(canMoveDirection(Orientation.TOP)){
                            y+=m;
                    }
                    else{
                        turn(Orientation.TOP);
                    }
                }
                break;
            case TOP:
                if(facing == Orientation.RIGHT){
                    if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getBottomLeftCorner(), Orientation.RIGHT);
                    }
                    else if(canMoveDirection(Orientation.LEFT)){
                         x-=m;
                    }
                    else{
                        turn(Orientation.LEFT);
                    }
                }
                else if(facing == Orientation.LEFT){
                    if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getBottomRightCorner(), Orientation.LEFT);
                    }
                    else if(canMoveDirection(Orientation.RIGHT)){
                        x-=m;
                    }
                    else{
                        turn(Orientation.RIGHT);
                    }
                }
                break;

            case RIGHT:
                if(facing == Orientation.RIGHT){ //going up
                    if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getTopLeftCorner(), Orientation.BOTTOM);
                    }
                    else if(canMoveDirection(Orientation.TOP)){
                        y-=m;
                    }
                    else{
                        turn(Orientation.TOP);
                    }
                }
                else if(facing == Orientation.LEFT){
                     if(!attachedTile.checkCollision(middleOfOrientation)){
                        rotateToNewTile(attachedTile.getBottomLeftCorner(), Orientation.TOP);
                    }
                    if(canMoveDirection(Orientation.BOTTOM)){
                        y-=m;
                    }
                    else{
                        turn(Orientation.BOTTOM);
                    }
                }
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

    /**
     * Updates middleOfOrientation and snailBottomOrientation based on
     * @param newOrientation
     */
    public void setOrientation(Orientation newOrientation){
        this.middleOfOrientation = middleOfSide(newOrientation);
        snailBottomOrientation = newOrientation;
    }

    /**
     * Rotates currentImage depending on the current orientation
     * and @param newOrientation
     */
    private void turn(Orientation newOrientation){
        if(currentMovement == Movement.FALL && !canMoveDirection(Orientation.BOTTOM)){   
            if(snailBottomOrientation == Orientation.LEFT){
                currentImage.rotateBy(-90);
            }
            else if(snailBottomOrientation == Orientation.RIGHT){
                currentImage.rotateBy(90);
            }
            else if(snailBottomOrientation == Orientation.TOP){
                currentImage.rotateBy(180);
            }
        }
        else if(snailBottomOrientation != newOrientation){
            if(facing == Orientation.RIGHT){
                currentImage.rotateBy(-90);
            }
            else{
                currentImage.rotateBy(90);
            }
        }
        setOrientation(newOrientation);
    }

     /**
     * Move the snail according the the new orientation and midpoint, the newSideMidpoint
     * being the midpoint of the side that is the newOrientation.
     * 
     * @param newSideMidpoint should be the corner of the block snail is on
     */
    private void rotateToNewTile(Point newSideMidpoint, Orientation newOrientation){
        if(newOrientation == Orientation.LEFT && snailBottomOrientation == Orientation.BOTTOM){
            x = (int)newSideMidpoint.getX();
            y = (int)(newSideMidpoint.getY() - currentImage.getHeight()/2);
            currentImage.rotateBy(90);
        }
        else if(newOrientation == Orientation.BOTTOM && snailBottomOrientation == Orientation.LEFT){
            x = (int)(newSideMidpoint.getX() - currentImage.getWidth()/2);
            y = (int)(newSideMidpoint.getY() - currentImage.getHeight());
            currentImage.rotateBy(-90);
        }
        else if (newOrientation == Orientation.TOP && snailBottomOrientation == Orientation.LEFT){
            x = (int)(newSideMidpoint.getX() - currentImage.getWidth()/2);
            y = (int)(newSideMidpoint.getY());
            currentImage.rotateBy(90);
        }
        else if (newOrientation == Orientation.LEFT && snailBottomOrientation == Orientation.TOP){
            x = (int)newSideMidpoint.getX();
            y = (int)(newSideMidpoint.getY()-currentImage.getHeight()/2);
            currentImage.rotateBy(-90);
        }
        else if (newOrientation == Orientation.RIGHT && snailBottomOrientation == Orientation.BOTTOM){
            x = (int)(newSideMidpoint.getX() - currentImage.getWidth());
            y = (int)(newSideMidpoint.getY() - currentImage.getHeight()/2);
            currentImage.rotateBy(-90);
        }
        else if (newOrientation == Orientation.BOTTOM && snailBottomOrientation == Orientation.RIGHT){
            x = (int)(newSideMidpoint.getX() - currentImage.getWidth()/2);
            y = (int)(newSideMidpoint.getY() - currentImage.getHeight());
            currentImage.rotateBy(90);
        }
        else if(newOrientation == Orientation.TOP && snailBottomOrientation == Orientation.RIGHT){
            x = (int)(newSideMidpoint.getX() - currentImage.getWidth()/2);
            y = (int)(newSideMidpoint.getY());
            currentImage.rotateBy(-90);
        }
        else if(newOrientation == Orientation.RIGHT && snailBottomOrientation == Orientation.TOP){
            x = (int)(newSideMidpoint.getX()- currentImage.getWidth());
            y = (int)(newSideMidpoint.getY() - currentImage.getHeight()/2);
            currentImage.rotateBy(90);
        }
        currentImage.setPosition(x,y);
        setOrientation(newOrientation);
    }

    /**
     * determines if the snail can move in the given direction, based
     * on obstacles and rules of the game (e.g., if you're on the bottom you can't move up)
     * @return whether or not the snail is able to move in @param direction based on 
     * snailBottomOrientation, such that the snail stays attached to a Tile (unless falling)
     */
    private boolean canMoveDirection(Snail.Orientation direction){
        if(snailBottomOrientation == Snail.Orientation.BOTTOM){
            if(direction == Snail.Orientation.TOP){
                return false;
            }
        }

        if(snailBottomOrientation == Snail.Orientation.LEFT){
            if(direction == Snail.Orientation.RIGHT){
                return false;
            }
        }

         if(snailBottomOrientation == Snail.Orientation.RIGHT){
            if(direction == Snail.Orientation.LEFT){
                return false;
            }
        }

        if(snailBottomOrientation == direction){
            return false;
        }

        if(hitPoints.get(getBoundaryPoints().indexOf(middleOfSide(direction)))){
            return false;
        }

        return true;
    }

    /**
     * @return the midpoint of @param orientation, the given side of the snail
     */
    public Point middleOfSide(Orientation orientation){
        if(orientation == Orientation.BOTTOM){
            return getBoundaryPoints().get(5);
        }
        else if(orientation == Orientation.TOP){
            return getBoundaryPoints().get(1);
        }
        else if(orientation == Orientation.LEFT){
            return getBoundaryPoints().get(7);
        }
        else{
            return getBoundaryPoints().get(3);
        }
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

    /**
     * @return a list of points on the snail in this order:
     * top left, top middle, top right, right middle, bottom right, bottom middle, bottom left, left middle
     */
    public List<Point> getBoundaryPoints(){
        Point topLeft = new Point (x, y);
        Point top = new Point(x+currentImage.getWidth()/2, y);
        Point topRight = new Point(x+currentImage.getWidth(), y);
        Point right = new Point(x+currentImage.getWidth(), y+currentImage.getHeight()/2);
        Point bottomRight = new Point(x+currentImage.getWidth(), y+currentImage.getHeight());
        Point bottom = new Point(x+currentImage.getWidth()/2, y+currentImage.getHeight());
        Point bottomLeft =  new Point(x, y+currentImage.getHeight());
        Point left = new Point(x, y+currentImage.getHeight()/2);

        return List.of(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left);
    }

    public List<Point> getInnerBoundaryPoints(){
         Point topLeft;
         Point top;
         Point topRight;
         Point right;
         Point bottomRight;
         Point bottom;
         Point bottomLeft;
         Point left;

        if(snailBottomOrientation == Orientation.TOP){
            topLeft = new Point(x + currentImage.getWidth()/3, y);
            top = new Point(x + currentImage.getWidth()/2, y);
            topRight = new Point(x + currentImage.getWidth()*2/3, y);
            right = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()*1/6);
            bottomRight = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()/3);
            bottom = new Point(x + currentImage.getWidth()/2, y + currentImage.getHeight()/3);
            bottomLeft = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()/3);
            left = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()*1/6);
        }
        else if(snailBottomOrientation == Orientation.LEFT){
            topLeft = new Point(x, y + currentImage.getHeight()/3);
            top = new Point(x + currentImage.getWidth()/6, y + currentImage.getHeight()/3);
            topRight = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()/3);
            right = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()/2);
            bottomRight = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()*2/3);
            bottom = new Point(x + currentImage.getWidth()/6, y + currentImage.getHeight()*2/3);
            bottomLeft = new Point(x, y + currentImage.getHeight()*2/3);
            left = new Point(x, y + currentImage.getHeight()/2);
        }
        else if(snailBottomOrientation == Orientation.RIGHT){
            topLeft = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()/3);
            top = new Point(x + currentImage.getWidth()*5/6, y + currentImage.getHeight()/3);
            topRight = new Point(x + currentImage.getWidth(), y + currentImage.getHeight()/3);
            right = new Point(x + currentImage.getWidth(), y + currentImage.getHeight()/2);
            bottomRight = new Point(x + currentImage.getWidth(), y + currentImage.getHeight()*2/3);
            bottom = new Point(x + currentImage.getWidth()*5/6, y + currentImage.getHeight()*2/3);
            bottomLeft = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()*2/3);
            left = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()/2);
        }
        else{
            topLeft = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()*2/3);
            top = new Point(x + currentImage.getWidth()/2, y + currentImage.getHeight()*2/3);
            topRight = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()*2/3);
            right = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight()*5/6);
            bottomRight = new Point(x + currentImage.getWidth()*2/3, y + currentImage.getHeight());
            bottom = new Point(x + currentImage.getWidth()/2, y + currentImage.getHeight());
            bottomLeft = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight());
            left = new Point(x + currentImage.getWidth()/3, y + currentImage.getHeight()*5/6);
        }

        return List.of(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left);
    }

}
