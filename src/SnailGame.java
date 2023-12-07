import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;

import java.util.List;
import java.util.stream.Collectors;

public class SnailGame {
    CanvasWindow canvas;
    int ticks = 0;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel

    private Snail snail;

    private Level currentLevel;
    private GraphicsGroup graphics;

    private static List<Level> levels = List.of( 
        new Level("""
4------------------+
[                  ]
[                  ]
[                  ]
[花                 ]
[                  ]
[                  ]
[               プ  ]
[Ss             ブ  ]
[ss   ラ   ロ     フ  ]
\\__________________/""")//s is just the little part of the snail we need to be aware of
);


    public SnailGame() {
        canvas = new CanvasWindow("Snails", 1920, 1080);

        for(Level level : levels){
            currentLevel = level;
            playRound(currentLevel);
        }     
    }

    /*
     * plays the game for 1 level
     * returns true if won
     */
    private boolean playRound(Level level){
        canvas.removeAll();
        graphics= level.getGraphics();
        graphics.setPosition(0,0);
        snail = currentLevel.getSnail();
        graphics.add(snail.getGraphics());
        graphics.setAnchor(graphics.getPosition());
        graphics.setScale(.5);//THIS IS THE ULTIMATE SCALE FACTOR THIS IS THE ONLY THING YOU CAN CHANGE FOR SCALING THINGS
        canvas.add(graphics);
        canvas.draw();
        currentLevel.updateAttachedTileOfSnail();

        handleSnailMovement();

        return false; //temporary
    }

    private void handleSnailMovement(){
        canvas.animate(() -> {
            if (ticks % 4 == 0){ //animate at 15 fps instead of 60
                currentLevel.updateAttachedTileOfSnail();
                snail.setHitPoints(snail.getBoundaryPoints()
                                    .stream()
                                    .map(point -> currentLevel.checkCollision(point))
                                    .collect(Collectors.toList()));
                snail.move(canvas.getKeysPressed());
                //checkCollisions();
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
     * I don't think we need this
     * 
     */
   // private void checkCollisions() {
        //order: top right, right middle, bottom right, bottom middle, bottom left, left middle, top left
        // List<Point> snailBoundaries = snail.getBoundryPoints();
        // boolean[] hitPoints = new boolean[snailBoundaries.size()];
        
        // for(int i = 0; i < snailBoundaries.size(); i++){
        //      hitPoints[i] =(currentLevel.checkCollision(snailBoundaries.get(i)));
        // }

        // if(snail.getFacing() == Snail.Direction.RIGHT){
        //     if(hitPoints[5] && hitPoints[6] && hitPoints[7]){
        //         snail.setOrientation(Snail.Orientation.RIGHT);
        //     }
        // }
        // else if(snail.getFacing() == Snail.Direction.LEFT){
        //     if(hitPoints[1] && hitPoints[2] && hitPoints[3]){
        //         snail.setOrientation(Snail.Orientation.LEFT);
        //     }
        // }
  //  }

    /*
     * return true if the snail has reached the endpoint
     */
    private boolean winRound(){
        return false; //temporary
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}