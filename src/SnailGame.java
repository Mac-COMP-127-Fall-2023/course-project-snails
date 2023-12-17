import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;
import java.awt.Toolkit;

public class SnailGame {
    private CanvasWindow canvas;
    private int ticks = 0;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel

    private Snail snail;

    private Level currentLevel;
    private GraphicsGroup graphics;

    private static int transitionIndex;

    private static Image transition = new Image(transitionPath(1));

    private static List<Level> levels = List.of( 
        new Level("""
４＿＿あ＿＿＿あ＿＿＿＿＿＿あ＿＿＿＿＋
「　　あ　　　＿　　　　　　あ　　　　」
「　　＿　　　　　　　　　　あ　　　　」
「　　　　　　　　　　　　　ー　　　　」
「花　　　　＿＿　　　　　　　　　　プ」
「　ずす　　　あ　　　　ひび　　　　ブ」
「　すす　　ロあ　　　　　　　　　ルフ」
あ＿＿＿＿＿＿あ　　　＿　　　　＿＿＿あ
「　　　　　　　　　　ー　　　　」　　」
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
        transitionIndex = 6;        // the initial transition call will increment both these
        levelIndex = -1;            // values and then set up the level accordingly
        canvas = new CanvasWindow("Snails", 1920, 1080);
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
        // for (Point p : snail.getShellPoints()) {
        //     Ellipse e = new Ellipse(0,0,6,6);
        //     e.setFillColor(Color.RED);
        //     e.setCenter(p);
        //     graphics.add(e);
        // }
        graphics.add(snail.getGraphics());
        graphics.setAnchor(graphics.getPosition());
        transition.setScale(2);
        graphics.add(transition);
        graphics.setScale(.55);
        canvas.add(graphics);

    }

    private void play(){

        canvas.animate(() -> {
            int framerate = 1;
            if (canvas.getKeysPressed().contains(Key.SHIFT)) {
                framerate=6;
            }
            if (canvas.getKeysPressed().contains(Key.R)) {
                Ellipse e = new Ellipse(0, 0, 6, 6);
                e.setCenter(snail.getGraphics().getCenter().add(new Point(0,16*6)));
                e.setFillColor(Color.RED);
                graphics.add(e);
            }
            if (canvas.getKeysPressed().contains(Key.G)) {
                Ellipse e = new Ellipse(0, 0, 6, 6);
                e.setCenter(snail.belowPoint());
                e.setFillColor(Color.GREEN);
                graphics.add(e);
            }

            if (ticks % framerate == 0){ //animate at 15 fps instead of 60
                transition();
                snail.move(canvas.getKeysPressed()); 
            }
            ticks++;
        });
    }

    /**
     * increments transition, going back to the beginning if it's run out of images
     */
    private void transition(){
        if (transitionIndex==0) {
            return;
        }
        transitionIndex = (transitionIndex + 1) % 14;
        transition.setImagePath(transitionPath(transitionIndex));

        switch (transitionIndex) {
            case 0:
                transition.setScale(0);
                break;                           //have to check again to see if we're now at 0 because its mod 14 
            case 7:
                currentLevel = levels.get(++levelIndex); //++i returns i+1 and increments i 
                setUpLevel();
                break;
        }
        transition.setCenter(snail.getGraphics().getCenter());
    }

    public static void win(){
        transitionIndex = 1;
        transition.setScale(2);
    }

    private static String transitionPath(int i) {
        return "GUI/Transition/screenwipe" + i + ".png";
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
        
    }
} 