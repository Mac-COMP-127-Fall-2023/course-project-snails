import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;

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
４＿＿あ＿＿＿あ＿＿＿＿＿＿あ＿＿＿＿＋
「　　あ　　　＿　　　　　　あ　　　　」
「　　＿　　　　　　　　　　あ　　　　」
「　　　　　　　　　ひび　　ー　　　」あ
「花　　　　＿＿　　　　　　　　　　　」
「　ずす　　　あ　　　　　　　　　　　」
「　すす　　　あ　　　＿　　　　＿＿＿あ
あ＿＿＿＿＿＿あ　　　ー　　　　」　　」
「　　　　　　　　　　　　　　　　　　」
「　　　　　　　　　　　　Ⓕ　　　　　」
「　　　あ　　　　　ヒピ　　　　　　　」
「　　　あ　　あ　　　　　　　　　　　」
「　　　　　あ　　　　　　　　あ　　　」
「　　　　　あ　　　　　　　　あ　　　」
・￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣／""")
);


    public SnailGame() {
        canvas = new CanvasWindow("Snails", 1920, 1080);
        currentLevel = levels.get(0);
        playRound();
    }

    /*
     * plays the game for 1 level
     * returns true if won
     */
    private void playRound(){
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
    }

    private void handleSnailMovement(){
        canvas.animate(() -> {
            if (ticks % 4 == 0){ //animate at 15 fps instead of 60
                snail.setHitPoints(snail.getBoundaryPoints()
                                    .stream()
                                    .map(point -> currentLevel.checkCollision(point))
                                    .collect(Collectors.toList()));
                snail.move(canvas.getKeysPressed()); 
                currentLevel.updateAttachedTileOfSnail();
            }
            ticks++;
            if(winRound()){
                GraphicsText win = new GraphicsText("You win!");
                win.setCenter(canvas.getWidth()/4, canvas.getHeight()/5);
                win.setFont(FontStyle.BOLD, 30);
                canvas.add(win);
                canvas.draw();
                canvas.pause(10000);
               canvas.closeWindow();
            }
        });
    }

    /*
     * return true if the snail has reached the endpoint
     */
    private boolean winRound(){
        if(currentLevel.getCompleted()){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
    }
}