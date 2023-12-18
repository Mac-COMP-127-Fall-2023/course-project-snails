import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;

import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;

public class SnailGame {
    private CanvasWindow canvas;
    private int ticks = 0;

    public static double scale;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel

    private Snail snail;

    private Level currentLevel;
    private GraphicsGroup graphics;

    private int transitionIndex;
    private boolean beginningOfRound;
    private boolean wonGame;

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
４＿＿あ＿＿＿あ＿＿＿＿＿＿＿あ＿＿＿＿＿＿＿＿＿＿＋
「　　｜　　　下　　　　　　　｜花　　　　　　　　　」
「　　下　　　　　　　　　　　｜　　　　　　　　　　」
「　　　　　　　　　　ひび　　下　　　　　　　　　プ」
「花　　　　右⊕　　　　　　　　　　　　　　　　　ブ」
「　ずす　　　｜　　　　ラ　　　　　　　　　　　ルフ」
「　すす　　ロ｜　　　　上　右ー左　　　　　　⊛ーーあ
あーーーーーー✚　　　　下　　　　　　　　　　下　　」
「　　　　　　　　　　　　　　　　　　　　　　　　　」
「　　　ラ　　　　　　　　　Ⓕ　　　　　　　　　　　」
「　　　上花　ル　　　　　　四　　　　　　　　　　　」
「　　　下　　四プ　　　　　　　　　　　　　　　　　」
「　　　　　上　ブ　　　　　　　上　　　　　　　　　」
「　　　　　｜　フ　　　　　　　｜　　　　　　　　　」
・￣￣￣￣￣あ￣￣￣￣￣￣￣￣￣あ￣￣￣￣￣￣￣￣￣／"""),
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
        wonGame = false;

        canvas = new CanvasWindow("Snails", 1920, 1080);
        canvas.setBackground(new Color(93, 59, 65));
        currentLevel = levels.get(levelIndex);
        scale = Math.min(currentLevel.getGraphics().getWidth(), currentLevel.getGraphics().getHeight()) / 1920;
        setUpLevel();
        play();
    }

    /**
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
    private void play(){
        canvas.animate(() -> {
            if (ticks % 4 == 0){ //animate at 15 fps instead of 60
                //transition to win screen if the game is over
                if(wonGame){
                    if(transitionIndex == 7){
                        winGame();
                    }
                    else{
                        transition();
                    }
                }
                //Transition in at the beginning of the round
                else if(beginningOfRound == true){
                    if(transitionIndex == 1){
                        beginningOfRound = false; //once transition is over, stop
                    }
                    else{
                        transition();
                    }
                }
                //transition out when you've won a level
                else if(winRound()){
                    transition.setScale(1);
                    snail.exit();

                    wonGame = winRound() && levels.indexOf(currentLevel) == levels.size() -1;

                    if(transitionIndex == 6 && !wonGame){
                        levelIndex++;
                        beginningOfRound = true;
                        currentLevel = levels.get(levelIndex);
                        setUpLevel();
                    }
                    transition();
                }
                //handle snail movement during level
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

    /**
     * increments transition, going back to the beginning if it's run out of images
     */
    private void transition(){
        if (transitionIndex >= 13) {
            transition.setScale(0);
            transitionIndex = 0;
        }
        transitionIndex++;
    }

    /**
     * Show the player a win screen
     */
    private void winGame(){
        transition.setImagePath(transitionAnimPaths.get(transitionIndex));

        GraphicsText winMessage = new GraphicsText("YOU WIN!");
        winMessage.setFillColor(Color.WHITE);
        winMessage.setFont(FontStyle.BOLD, 60);
        winMessage.setCenter(currentLevel.getGraphics().getCenter());
        canvas.add(winMessage);
    }
 
    /**
     * @return true if the snail has reached the endpoint, false otherwise
     */
    private boolean winRound(){
        return currentLevel.getCompleted();
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
        
    }
} 