import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;

import java.awt.Color;

/*
 * DO TO:
 * add a finishing point visual of some kind (** this will also
 * be used in SnailGame to see if the snail should proceed to the 
 * next level)
 */
class Level {
    private GraphicsGroup foreground; 
    private GraphicsGroup background; 
    private Point snailPos; 

    private GraphicsObject firstElementForSnail;

    public Level(double width, double height) {
        foreground = new GraphicsGroup();
        background = new GraphicsGroup();
        
        //sides
        addLedge(0, 0, width, height/10);
        addLedge(0, height-height/10, width, height/10);
        addLedge(0, 0, width/10, height);
        addLedge(width - width/10, 0, width/10, height);

        //startingLedge
        firstElementForSnail = addLedge(width/2 - 40, height/2 + 40, 100, 30);
    }   

    private Rectangle addLedge(double x, double y, double width, double height){
        Rectangle rect = new Rectangle(x, y, width, height);
        rect.setFillColor(Color.GRAY);
        rect.setStroked(false);
        foreground.add(rect);

        return rect;
    }

    public GraphicsObject getFirstElement(){
        return firstElementForSnail;
    }

    public GraphicsGroup getGraphics() {
        GraphicsGroup group = new GraphicsGroup();
        group.add(background);
        group.add(foreground);
        return group;
    }

    public boolean checkCollision(Point p){
        return foreground.testHit(p.getX(), p.getY());
    }

    public Point getSnailPos() {
        return snailPos;
    }
}