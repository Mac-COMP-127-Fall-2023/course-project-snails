import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.TextAlignment;
import edu.macalester.graphics.events.Key;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

public class SnailGame {
    private CanvasWindow canvas;
    private int ticks = 0;
    private final double SCALE = .5;
    private boolean playing = false;

    private int parallaxFrom =16;
    private int parallaxTo =16;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel
    private GraphicsGroup background;
    private Snail snail;
    private Level currentLevel = new Level("""
あ""",
"""
　　　　　　　　　ひび
　プ　　　　g　
　ブ　　　　G　」花
　フ　　　　￣￣／
￣￣￣￣￣￣／「
あああああああ「""");
    private GraphicsGroup graphics;

    private static int transitionIndex;

    private static Image transition = new Image(transitionPath(1));

    private static List<Level> levels = List.of( 
        new Level("""
４＿＿あ＿＿＿あ＿＿＿＿＿＿あ＿＿＿＿＋あああああああああああああああ
「　　｜　　　下　　　　　　｜　　　　」あああああああああああああああ
「　　下　　　　　　　　　　｜　　　　」あああああああああああああああ
「　　　　　　　　　　　　　下　　　　」あああああああああああああああ
「花　　　　　　　　　　　　　　　　プ」あああああああああああああああ
「　ずす　　　　　　　　ひび　　　　ブ」あああああああああああああああ
「　すす　　ロ上　　　　　　　　　ルフ」あああああああああああああああ
あーーーーーー✚花　右左　　　　⊛ーーああああああああああああああああ
「　　　　　　　　　　　　　　　下　　」あああああああああああああああ
「　　　　　　　　　　　　　　　　　　」あああああああああああああああ
「　　　　　　　ひび　　　Ⓕ　　　　　」あああああああああああああああ
「　　　上　　　　　　　　上　　　　　」あああああああああああああああ
「　　　下　　上　　　　　下　　　　　」あああああああああああああああ
「　　　　　　下　　　　　　　上　　　」あああああああああああああああ
「　　　　　上　　　　　　　　｜　　　」あああああああああああああああ
・￣￣￣￣￣あ￣￣￣￣￣￣￣￣あ￣￣￣／あああああああああああああああ
あああああああああああああああああああああああああああああああああああ
あああああああああああああああああああああああああああああああああああ
あああああああああああああああああああああああああああああああああああ
あああああああああああああああああああああああああああああああああああ""",

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
        titleScreen();
    }

    /**
     * sets up the canvas with the graphics of the current level
     */

    private void titleScreen() {
        canvas.setBackground(Color.BLACK);
        background = currentLevel.getBackground();
        background.setAnchor(new Point(0,0));
        background.setScale(SCALE*3);
        background.setPosition(-16*SCALE*9,2*16*SCALE*6 );
        canvas.add(background);
        
        Rectangle overlay = new Rectangle(0,0,1920,1080);
        overlay.setFillColor(new Color(46*5,25*5,28*5,102));
        canvas.add(overlay);
        canvas.draw();
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/Fonts/Pixellari.ttf")));
        } catch (IOException|FontFormatException e){
            
        }
        List<GraphicsText> title = List.of(new GraphicsText("Slimy"), 
                                            new GraphicsText("Navigational"), 
                                            new GraphicsText("Adventure"),
                                            new GraphicsText("Involving"),
                                            new GraphicsText("L'escargot"));
        int i = 0;
        for (GraphicsText word : title) {
            // word.setFont(new Font("Pixellari", 0, (3*32))); //leaving this just in case
            word.setFont("Pixellari",FontStyle.PLAIN,3*32);
            word.setPosition(72+56*i, 128+93*i);
            word.setFillColor(new Color(144,163,83,0));
            canvas.add(word);
            i++;
        }
        GraphicsText keytext = new GraphicsText("press any key\nto");
        GraphicsText start = new GraphicsText("start");
        start.setFont("Pixellari", FontStyle.PLAIN, (3*16));
        start.setFillColor(new Color(88,141,190,0));
        start.setPosition(1200,777);
        keytext.setFont("Pixellari", FontStyle.PLAIN, (3*16));
        keytext.setFillColor(new Color(232,116,105,0));
        keytext.setPosition(1150, 727);
        canvas.add(keytext);
        canvas.add(start);
        canvas.animate(()->{
            ticks++;
            if (!playing) {
                keytext.setFillColor(new Color(232,116,105,Math.min(ticks*64,255)));
                start.setFillColor(new Color(88,141,190,Math.min(ticks*64,255)));

                double clamp = Math.min(ticks/666f, 1.5);
                keytext.setPosition(1150 +3*Math.sin(ticks/37F)*clamp, 7*Math.sin(ticks/20F)*clamp+727);
                keytext.setRotation(Math.sin(ticks/50F)*clamp);
                start.setPosition(1200 +5*Math.sin(ticks/29F)*clamp, 7*Math.sin(ticks/24F)*clamp+777+5*clamp*Math.sin(ticks/51F));
                start.setRotation(Math.sin(ticks/32F)*3*clamp);
                int j=1;
                for (GraphicsText word : title) {
                    word.setPosition(72+56*(j-1)+9*Math.sin(ticks/90F*j)*clamp, 128+93*(j-1)-9*Math.sin(ticks/90F*j)*clamp);
                    word.setRotation(Math.sin(ticks/39F)*3*Math.sin(j)*clamp);
                    word.setFillColor(new Color(144,163,83,Math.min(ticks*64,255)));
                    j++;
                }
            }
            if (playing||!canvas.getKeysPressed().isEmpty()) {
                playing = true;
                int framerate = 1;    
                if (ticks % 1 == 0){
                    transition();
                    snail.move(canvas.getKeysPressed());
                }
            }
        });
    }

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