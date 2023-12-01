import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.events.Key;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

public class SnailGame {
    CanvasWindow canvas;
    int ticks = 0;

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_PIXEL_RATIO = (int) SCREEN_SIZE.getWidth() / 320; //the size, in screen pixels, of a single in-game pixel 

    private Snail snail;

    private static List<Level> levels = List.of(
               new Level("░S░░░░"), 
                new Level("""
                            ▍
                            ▍
                            ▍
                            ▍S░░░░░░░
                            ▙▃▃▃▃▃▃▃▟"""));


    public SnailGame() {
        canvas = new CanvasWindow("Snails", (int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight());

        for(Level curLevel : levels){
            playRound(curLevel);
        }     
    }

    /*
     * plays the game for 1 level
     * returns true if won
     */
    private boolean playRound(Level curLevel){
        canvas.removeAll();
        canvas.add(curLevel.getGraphics());
        snail = new Snail(curLevel.getSnailPos());
        canvas.add(snail.getGraphics());
        canvas.draw();

        handleSnailMovement();

        return false; //temporary
    }

    private void handleSnailMovement(){
        canvas.animate(() -> {
            if (ticks % 6 == 0){ //animate at 10 fps instead of 60
                if(canvas.getKeysPressed().contains(Key.RIGHT_ARROW)){
                    snail.updateAnimation();
                    snail.moveRight();
                    checkCollisions();
                }
                if(canvas.getKeysPressed().contains(Key.LEFT_ARROW)){
                    snail.updateAnimation();
                    snail.moveLeft();
                    checkCollisions();
                }
                if(canvas.getKeysPressed().contains(Key.SPACE)){
                    snail.updateAnimation();
                    snail.curl();
                    checkCollisions();
                }
            }
            ticks++;
        });
    }

    /**
     * called after our snail moves or falls, should check collisions
     * and update its orientation if necessary. **We also need to check
     * if one pixel below the center of the snail bottom is no longer
     * colliding with a tile, this means that it is most of the way up
     * a convex portion of the level, so we should rotate it the rest
     * of the way so it doesn't look weird.** 
     * idk if im explaining that poorly lol and there's probably a 
     * better way to do that
     * 
     */
    private void checkCollisions() {

    }

    /*
     * return true if the snail has reached the endpoint
     */
    private boolean winRound(){
        return false; //temporary
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }

    //For reference: my code for detecting collisions
    /*
    public boolean updatePosition(CanvasWindow canvas) {
        double newX = getCenter().getX();
        double newY = getCenter().getY();

        GraphicsObject obstacle;

        //move in increments of 10%, checking for collisions
        for (double i = 0; i < 10; i++) {
            newX = getCenter().getX() + .1 * dx;
            newY = getCenter().getY() + .1 * dy;

            //check if ball hit side of canvas
            if (newX - RADIUS <= 0 || newX + RADIUS >= canvas.getWidth()) {
                bounce(true);
            }
            
            //check if ball hit top of canvas
            else if (newY <= 0) {
                bounce(false);
            }

            //check if ball hit the left or right side of a brick
            obstacle = canvas.getElementAt(newX + (dx / Math.abs(dx)) * RADIUS, newY);
            if (obstacle != null) {
                bounce(true);
            }

            //check if ball hit the top or bottom side of a brick
            obstacle = canvas.getElementAt(newX, newY + (dy / Math.abs(dy)) * RADIUS);
            if (obstacle != null) {
                bounce(false);
            }

            //check if ball hit bottom of canvas
            if (newY >= canvas.getHeight()) {
                return false; //return false to indicate that a life should be lost
            }

            
            if (obstacle != null) {
                //if ball hit a block, break the block
                if (obstacle.getClass() == Brick.class) {
                    Brick brick = (Brick) obstacle;
                    brick.setBroken();
                }

                //if ball hit the paddle, change the velocity based on where on the paddle it hits
                if (obstacle.getClass() == Paddle.class) {
                    calculateInitialVelocity(getCenter().getX());
                }
            }

            setCenter(newX, newY); //update position
        }
        return true;  //return true to indicate that a life should not be lost
    } */
}