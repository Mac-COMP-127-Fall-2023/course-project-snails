import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;

import java.util.List;
import java.util.stream.Collectors;
import java.awt.Toolkit;

public class SnailGame {
    private CanvasWindow canvas;
    private int ticks = 0;

    public static final double scale = Math.min(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1920, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1080);

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel

    private Snail snail;

    private Level currentLevel;
    private GraphicsGroup graphics;

    private int transitionIndex;
    private boolean beginningOfRound;

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
    private Image transition;

    private static List<Level> levels = List.of( 
        new Level("""
４＿＿あ＿＿＿あ＿＿＿＿＿＿あ＿＿＿＿＋
「　　あ　　　＿　　　　　　あ　　　　」
「　　＿　　　　　　　　　　あ　　　　」
「　　　　　　　　　ひび　　ー　　　プ」
「花　　　　＿＿　　　　　　　　　　ブ」
「　ずす　　　あ　　　　　　　　　ルフ」
「　すす　　ロあ　　　＿　　　　＿＿＿あ
あ＿＿＿＿＿＿あ　　　ー　　　　」　　」
「　　　　　　　　　　　　　　　　　　」
「　　　　　　　　　　　　Ⓕ　　　　　」
「　　　あ　　　　　　　　￣　　　　　」
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
    
    private int levelIndex;

    public SnailGame() {
        transition = new Image(transitionAnimPaths.get(7));
        transitionIndex = 6;
        beginningOfRound = true;
        levelIndex = 0;

        canvas = new CanvasWindow("Snails", 1920, 1080);
        currentLevel = levels.get(levelIndex);
        setUpLevel();
        handleSnailMovement();
    }

    /*
     * sets up the canvas with the graphics of the current level
     */
    private void setUpLevel(){
        canvas.removeAll();
        graphics = currentLevel.getGraphics();
        graphics.setPosition(0,0);
        snail = currentLevel.getSnail();
        currentLevel.updateAttachedTileOfSnail();
        graphics.add(snail.getGraphics());
        graphics.setAnchor(graphics.getPosition());
        graphics.add(transition);
        graphics.setScale(scale);//THIS IS THE ULTIMATE SCALE FACTOR THIS IS THE ONLY THING YOU CAN CHANGE FOR SCALING THINGS
        canvas.add(graphics);
    }

    //TODO: move snail to center of endpoint when win
    //TODO: make a win screen when all levels have been won
    private void handleSnailMovement(){
        canvas.animate(() -> {
            if (ticks % 4 == 0){ //animate at 15 fps instead of 60
                //Transition in at the beginning
                if(beginningOfRound == true){
                    if(transitionIndex == 1){
                        beginningOfRound = false; //once transition is over, stop
                    }
                    else{
                        transition();
                    }
                }
                else if(winRound()){
                    transition.setScale(1);
                    snail.exit();
                    if(transitionIndex == 6){
                        levelIndex++;
                        beginningOfRound = true;
                        currentLevel = levels.get(levelIndex);
                        setUpLevel();
                    }
                    transition();
                }
                else{
                    snail.setHitPoints(snail.getBoundaryPoints()
                                    .stream()
                                    .map(point -> currentLevel.checkCollision(point))
                                    .collect(Collectors.toList()));
                    snail.move(canvas.getKeysPressed()); 
                    currentLevel.updateAttachedTileOfSnail();
                }
            }
            transition.setImagePath(transitionAnimPaths.get(transitionIndex));
            ticks++;
        });
    }

    private void transition(){
        if (transitionIndex >= 13) {
            transition.setScale(0);
            transitionIndex = 0;
        }
        transitionIndex++;
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