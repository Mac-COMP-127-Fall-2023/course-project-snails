import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;

import java.util.List;
import java.util.stream.Collectors;

public class SnailGame {
    CanvasWindow canvas;
    int ticks = 0;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel

    private Snail snail;

    private Level currentLevel;
    private GraphicsGroup graphics;

    int transitionIndex = 6;
    boolean won = true;

    private List<String> transitionAnimPaths = List.of("GUI/Transition/screenwipe1.png", 
                                                    "GUI/Transition/screenwipe2.png", 
                                                    "GUI/Transition/screenwipe3.png",
                                                    "GUI/Transition/screenwipe4.png", 
                                                    "GUI/Transition/screenwipe5.png", 
                                                    "GUI/Transition/screenwipe6.png", 
                                                    "GUI/Transition/screenwipe7.png", 
                                                    "GUI/Transition/screenwipe8.png",
                                                    "GUI/Transition/screenwipe9.png", 
                                                    "GUI/Transition/screenwipe10.png", 
                                                    "GUI/Transition/screenwipe11.png",
                                                    "GUI/Transition/screenwipe12.png", 
                                                    "GUI/Transition/screenwipe13.png", 
                                                    "GUI/Transition/screenwipe14.png");
    private Image transition = new Image(transitionAnimPaths.get(0));

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
「　　　あ　　　　　　　　　　　　　　」
「　　　あ　　あ　　　　　　　　　　　」
「　　　　　あ　　　　　　　　あ　　　」
「　　　　　あ　　　　　　　　あ　　　」
・￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣／"""),
        new Level("""
　　ずす　　
　　すす　Ⓕ
￣￣￣￣￣￣
        """)
    );
    
    int levelIndex = 0;

    public SnailGame() {
        canvas = new CanvasWindow("Snails", 1920, 1080);
        currentLevel = levels.get(levelIndex);
        playRound();
        handleSnailMovement();
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
        graphics.add(transition);
        graphics.setScale(0.5);//THIS IS THE ULTIMATE SCALE FACTOR THIS IS THE ONLY THING YOU CAN CHANGE FOR SCALING THINGS
        canvas.add(graphics);
        canvas.draw();
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
                if(winRound()){
                    transitionIndex = 0;
                    transition.setScale(1);
                    won = true;
                }
                if (won) {
                    if (transitionIndex == 5) {
                        levelIndex++;
                        currentLevel = levels.get(levelIndex);
                        playRound();
                    }
                    if (transitionIndex >= 13) {
                        transition.setScale(0);
                        won = false;
                        transitionIndex = 0;
                    }
                    transitionIndex++;
                }
                canvas.draw();
            }
            transition.setImagePath(transitionAnimPaths.get(transitionIndex));
            ticks++;

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