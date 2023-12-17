import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.Key;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.awt.Color;
import java.awt.Toolkit;

public class SnailGame {
    private CanvasWindow canvas;
    private int ticks = 0;
    private final double SCALE = .55;

    private int parallaxFrom =16;
    private int parallaxTo =16;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel
    private GraphicsGroup background;
    private Snail snail;
    private Level currentLevel;
    private GraphicsGroup graphics;

    private static int transitionIndex;

    private static Image transition = new Image(transitionPath(1));

    private static List<Level> levels = List.of( 
        new Level("""
４＿＿あ＿＿＿あ＿＿＿＿＿＿あ＿＿＿＿＋ああああああああああ
「　　あ　　　＿　　　　　　あ　　　　」ああああああああああ
「　　＿　　　　　　　　　　あ　　　　」ああああああああああ
「　　　　　　　　　　　　　ー　　　　」ああああああああああ
「花　　　　　　　　　　　　　　　　プ」ああああああああああ
「　ずす　　　　　　　　ひび　　　　ブ」ああああああああああ
「　すす　　ロ「　　　　　　　　　ルフ」ああああああああああ
あ＿＿＿＿＿＿「花　＿＿　　　　＿＿＿あああああああああああ
「　　　　　　　　　　　　　　　」　　」ああああああああああ
「　　　　　　　　　　　　　　　　　　」ああああああああああ
「　　　　　　　ひび　　　Ⓕ　　　　　」ああああああああああ
「　　　あ　　　　　　　　￣　　　　　」ああああああああああ
「　　　あ　　￣　　　　　＿　　　　　」ああああああああああ
「　　　　　　あ　　　　　　　あ　　　」ああああああああああ
「　　　　　あ　　　　　　　　あ　　　」ああああああああああ
・￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣／ああああああああああ""",
"""
　　　　　　
プ　　g　　
ブ　　G　」
フ　　￣￣あ
￣￣￣あああ"""),

        new Level("""
　　ずす　　
　　すす　Ⓕ
￣￣￣￣￣￣
        """,
"""
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
        canvas.setBackground(Color.BLACK);

        background = currentLevel.getBackground();
        background.setScale(SCALE*3);
        background.setAnchor(new Point(0,0));
        background.setPosition((32-parallaxFrom)*SCALE*6,2*16*SCALE*9);
        canvas.add(background);

        Rectangle overlay = new Rectangle(0,0,1920,1080);
        overlay.setFillColor(new Color(46*4,25*4,28*4,127));
        canvas.add(overlay);

        graphics = currentLevel.getGraphics();
        graphics.setPosition(0,0);

        snail = currentLevel.getSnail();
        graphics.add(snail.getGraphics());

        transition.setScale(2);
        graphics.add(transition);

        graphics.setAnchor(graphics.getPosition());
        graphics.setScale(SCALE);
        canvas.add(graphics);
    }

    private void play(){

        canvas.animate(() -> {
            //debugging stuff
            int framerate = 1;
            // if (canvas.getKeysPressed().contains(Key.SHIFT)) {
            //     framerate=6;
            // }
            // if (canvas.getKeysPressed().contains(Key.R)) {
            //     Ellipse e = new Ellipse(0, 0, 6, 6);
            //     e.setCenter(snail.getGraphics().getCenter().add(new Point(0,16*6)));
            //     e.setFillColor(Color.RED);
            //     graphics.add(e);
            // }
            // if (canvas.getKeysPressed().contains(Key.G)) {
            //     Ellipse e = new Ellipse(0, 0, 6, 6);
            //     e.setCenter(snail.belowPoint());
            //     e.setFillColor(Color.GREEN);
            //     graphics.add(e);
            // }
            if (ticks % framerate == 0){ //animate at 15 fps instead of 60
                transition();
                boolean moved = snail.move(canvas.getKeysPressed());
                // if (moved) {
                //     parallaxTo = snail.getX()/16;
                // }
            }
            ticks++;
        });
    }

    /**
     * increments transition, going back to the beginning if it's run out of images
     */
    private void transition(){
        // if (parallaxTo>parallaxFrom) {
        //     if (Math.random()>.8) {
        //         background.setPosition((32-++parallaxFrom)*SCALE*6,2*16*SCALE*9);
        //     }
        // } else if (parallaxTo<parallaxFrom) {
        //     if (Math.random()>.8) {
        //         parallaxFrom--;
        //         background.setPosition((32-parallaxFrom)*SCALE*6,2*16*SCALE*9 );
        //     }
        // }

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
                currentLevel = levels.get(++levelIndex); 
                setUpLevel();
                break;
        }
        transition.setCenter(snail.getGraphics().getCenter());
    }

    //transition handles the level transition, so if we call this once it'll start the process that makes it setup the next
    //level
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