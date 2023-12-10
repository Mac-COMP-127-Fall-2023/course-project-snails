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
[  Ss           ブ  ]
[  ss ラ   ロ     フ  ]
[  ___             ]
[               プ  ]
[                  ]
\\__________________/""")//s is just the little part of the snail we need to be aware of
);


    public SnailGame() {
        canvas = new CanvasWindow("Snails", 1920, 1080);

        for(Level level : levels){
            currentLevel = level;
            playRound();
        }     
    }

    /*
     * plays the game for 1 level
     * returns true if won
     */
    private boolean playRound(){
        canvas.removeAll();
        graphics = currentLevel.getGraphics();
        graphics.setPosition(0,0);
        snail = currentLevel.getSnail();
        currentLevel.updateAttachedTileOfSnail();
        graphics.add(snail.getGraphics());
        graphics.setAnchor(graphics.getPosition());
        graphics.setScale(.5);//THIS IS THE ULTIMATE SCALE FACTOR THIS IS THE ONLY THING YOU CAN CHANGE FOR SCALING THINGS
        canvas.add(graphics);
        canvas.draw();

        handleSnailMovement();

        return false; //temporary
    }

    private void handleSnailMovement(){
        canvas.animate(() -> {
            if (ticks % 4 == 0){ //animate at 15 fps instead of 60
                snail.setHitPoints(snail.getBoundaryPoints()
                                    .stream()
                                    .map(point -> currentLevel.checkCollision(point))
                                    .collect(Collectors.toList()));
                currentLevel.updateAttachedTileOfSnail();
                snail.move(canvas.getKeysPressed()); 
            }
            ticks++;
        });
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
}