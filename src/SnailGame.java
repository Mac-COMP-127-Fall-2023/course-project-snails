import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;

import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class SnailGame {
    private CanvasWindow canvas;
    private int ticks = 0;
    private final double SCALE = .5;
    private boolean playing = false;
    private static int shakeFrame=10;
    private static int shake;

    public static final int SCREEN_PIXEL_RATIO = 6; //the size, in screen pixels, of a single in-game pixel
    private GraphicsGroup background;
    private Image backbackground = new Image("GUI/snail_background.png");
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
「　ずすぎ　　　　　　　ひび　　　　ブ」あああああああああああああああ
「　すすが　ロ上　　　　　　　　　ルフ」あああああああああああああああ
あーーーーーー✚花　右左　　　　⊛ーーああああああああああああああああ
「　　　　　　　　　　　　　　　下　　」あああああああああああああああ
「　　　　　　　　　　　　　　　　　　」あああああああああああああああ
「　　　　　　　ひび　　　Ⓕ　　　　　」あああああああああああああああ
「　　　上　　　　　　　　上　　　　　」あああああああああああああああ
「　　　下　　上　　　　　下　　　　　」あああああああああああああああ
「　　　　　　下　　ぎ　　　　上　　　」あああああああああああああああ
「　　　　　上　　　が　　　　｜　　　」あああああああああああああああ
・￣￣￣￣￣あ￣￣￣￣￣￣￣￣あ￣￣￣／あああああああああああああああ
あああああああああああああああああああああああああああああああああああ
あああああああああああああああああああああああああああああああああああ
あああああああああああああああああああああああああああああああああああ
あああああああああああああああああああああああああああああああああああ""",

"""
　　　　　　
プ　　ぎ　　
ブ　　が　」
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

    private boolean won;

    public SnailGame() {
        won = false;
        transitionIndex = 6;        // the initial transition call will increment both these
        levelIndex = -1;            // values and then set up the level accordingly
        canvas = new CanvasWindow("Snails", 1920, 1080);
        titleScreen();
    }

    /**
     * sets up the canvas with the graphics of the current level
     * and plays the game
     */
    private void titleScreen() {
        canvas.setBackground(Color.BLACK);
        background = currentLevel.getBackground();
        background.setAnchor(new Point(0,0));
        background.setScale(SCALE*3); //relative size of background tiles compared to normal tiles
        background.setPosition(-16*SCALE*9,2*16*SCALE*6 );
        canvas.add(background);
        
        Rectangle overlay = new Rectangle(0,0,1920,1080);
        overlay.setFillColor(new Color(46*5,25*5,28*5,102));
        canvas.add(overlay);
        canvas.draw();
        String font = "Pixellari";
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/Fonts/Pixellari.ttf")));
        } catch (IOException|FontFormatException e){
            font = "arial narrow";
        }
        List<GraphicsText> title = List.of(new GraphicsText("Slimy"), 
                                            new GraphicsText("Navigational"), 
                                            new GraphicsText("Adventure"),
                                            new GraphicsText("Involving"),
                                            new GraphicsText("L'escargot"));
        int i = 0;
        for (GraphicsText word : title) {
            // word.setFont(new Font("Pixellari", 0, (3*32))); //leaving this just in case
            word.setFont(font,FontStyle.PLAIN,3*32);
            word.setPosition(72+56*i, 128+93*i);
            word.setFillColor(new Color(144,163,83,0));
            canvas.add(word);
            i++;
        }
        GraphicsText keytext = new GraphicsText("press any key\nto");
        GraphicsText start = new GraphicsText("start");
        start.setFont(font, FontStyle.PLAIN, (3*16));
        start.setFillColor(new Color(88,141,190,0));
        start.setPosition(1200,777);
        keytext.setFont(font, FontStyle.PLAIN, (3*16));
        keytext.setFillColor(new Color(232,116,105,0));
        keytext.setPosition(1150, 727);
        canvas.add(keytext);
        canvas.add(start);
        canvas.animate(()->{
            ticks++;
            if (!playing) {
                preload(ticks-20);
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
                if (ticks % 1 == 0){
                    transition();
                    snail.move(canvas.getKeysPressed());
                    shake();
                }
            }
        });
    }

    /**
     * sets up the current level by removing the previous and adding graphics
     * and the snail
     */
    private void setUpLevel(){
        canvas.removeAll();
        canvas.setBackground(Color.BLACK);

        background = currentLevel.getBackground();
        background.setScale(SCALE*3);
        background.setAnchor(new Point(0,0));
        background.setPosition(16*SCALE*6,2*16*SCALE*9);
        canvas.add(background);

        backbackground.setScale(SCALE * 1.5);
        backbackground.setAnchor(new Point(0,0));
        backbackground.setPosition(-3 * SCALE, 0);
        canvas.add(backbackground);

        Rectangle overlay = new Rectangle(0,0,1920,1080);
        overlay.setFillColor(new Color(46*4,25*4,28*4,127));
        canvas.add(overlay);

        graphics = new GraphicsGroup();

        snail = currentLevel.getSnail();
        graphics.add(snail.getGraphics());

        graphics.add(currentLevel.getGraphics());
        graphics.setPosition(0,0);

        transition.setScale(2);
        graphics.add(transition);

        graphics.setAnchor(graphics.getPosition());
        graphics.setScale(SCALE);
        canvas.add(graphics);
    }

    /**
     * increments transition, going back to the beginning if it's run out of images
     * increments level as appropriate, and calls winGame() when all levels are finished
     */
    private void transition(){
        if (transitionIndex==0) {
            return;
        }
        if(won){
            transitionIndex = 7;
        }
        else{
            transitionIndex = (transitionIndex + 1) % 14;
            transition.setImagePath(transitionPath(transitionIndex));
        }

        switch (transitionIndex) {
            case 0:
                transition.setScale(0);
                break;                           //have to check again to see if we're now at 0 because its mod 14 
            case 7:
                if(levelIndex < levels.size()-1){
                    currentLevel = levels.get(++levelIndex); 
                    setUpLevel();
                }
                else{
                    winGame();
                    won = true;
                }
                break;
        }
        transition.setCenter(snail.getGraphics().getCenter());
    }

    /**
     * Show the player a win screen
     */
    private void winGame(){
        transition.setImagePath(transitionPath(transitionIndex));

        GraphicsText winMessage = new GraphicsText("YOU WIN!");
        winMessage.setFillColor(Color.WHITE);
        winMessage.setFont(FontStyle.BOLD, 60);
        winMessage.setCenter(currentLevel.getGraphics().getCenter());
        canvas.add(winMessage);
    }

    /**
     * set transition back such that the next level will begin
     */
    public static void winLevel(){
        transitionIndex = 1;
        transition.setScale(2);
    }

    public static void shake(int velocity){
        if (velocity>5) {
            shakeFrame=0;
            shake=velocity-5;
        }
    }
    private void shake(){
        if (shakeFrame<4) {
            shakeFrame++;
            graphics.setPosition(Math.cos(shakeFrame*Math.PI/2)*shake*shakeFrame*(4-shakeFrame)/7f,0);
        } else {
            shake=0;
        }
    }

    private void preload(int i) {
        if (i>=0) {
            if (i<14) {
                Image img = new Image("GUI/Transition/screenwipe" + i + ".png");
            } else if (i<22) {
                Image img = new Image("Snail/Crawl/snail" + (i-14) + ".png");
            }
        }
    }

    private static String transitionPath(int i) {
        return "GUI/Transition/screenwipe" + i + ".png";
    }

    public static void main(String[] args) {
        SnailGame game = new SnailGame();
        
    }
} 