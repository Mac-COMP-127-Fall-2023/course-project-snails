import java.util.Map;
import static java.util.Map.entry;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public interface Tile{
    public Point getTopRightCorner();
    public Point getBottomRightCorner();
    public Point getTopLeftCorner();
    public Point getBottomLeftCorner();

    public Image getImage();

<<<<<<< Updated upstream
    public Point getTopLeftCorner(){
        return getPosition();
    }

    public Point getTopRightCorner(){
        return new Point(getX() + getWidth(), getY());
    }

    public Point getBottomRightCorner(){
        return new Point(getX() + getWidth(), getY() + getHeight());
    }

    public Point getBottomLeftCorner(){
        return new Point(getX(), getY() + getHeight());
    }

    public boolean checkCollision(Point p){
        return testHit(p.getX(), p.getY());
    }

    private void setupImagePathMap() {
        IMAGE_PATH_MAP.put(' ', "Tiles/empty.png"); //not a space, \u0020
        IMAGE_PATH_MAP.put('-', "Tiles/block_i_dirt_b.png");
        IMAGE_PATH_MAP.put('_', "Tiles/block_i_dirt_t.png");
        IMAGE_PATH_MAP.put('[', "Tiles/block_i_dirt_r.png");
        IMAGE_PATH_MAP.put(']', "Tiles/block_i_dirt_l.png");
        IMAGE_PATH_MAP.put('\\', "Tiles/block_i_dirt_tr.png");
        IMAGE_PATH_MAP.put('/', "Tiles/block_i_dirt_lt.png");
        IMAGE_PATH_MAP.put('4', "Tiles/block_i_dirt_rb.png");
        IMAGE_PATH_MAP.put('+', "Tiles/block_i_dirt_lb.png"); 
        IMAGE_PATH_MAP.put('フ', "Tiles/mushroom1.png");
        IMAGE_PATH_MAP.put('ブ', "Tiles/mushroom2.png");
        IMAGE_PATH_MAP.put('プ', "Tiles/mushroom3.png");
        IMAGE_PATH_MAP.put('ラ', "Tiles/rocks_small1.png"); 
        IMAGE_PATH_MAP.put('ル', "Tiles/rocks_small2.png"); 
        IMAGE_PATH_MAP.put('ロ', "Tiles/rocks_large.png"); 
        IMAGE_PATH_MAP.put('花', "Tiles/flower_wall_left.png"); 
    }
=======
    public static final Map<Character, String> IMAGE_PATH_MAP = Map.ofEntries(
        entry(' ', "Tiles/empty.png"), //not a space, \u0020
        entry('-', "Tiles/block_i_dirt_b.png"),
        entry('_', "Tiles/block_i_dirt_t.png"),
        entry('[', "Tiles/block_i_dirt_r.png"),
        entry(']', "Tiles/block_i_dirt_l.png"),
        entry('\\', "Tiles/block_i_dirt_tr.png"),
        entry('/', "Tiles/block_i_dirt_lt.png"),
        entry('4', "Tiles/block_i_dirt_rb.png"),
        entry('+', "Tiles/block_i_dirt_lb.png"),
        entry('フ', "Tiles/mushroom1.png"),
        entry('ブ', "Tiles/mushroom2.png"),
        entry('プ', "Tiles/mushroom3.png"),
        entry('ラ', "Tiles/rocks_small1.png"),
        entry('ル', "Tiles/rocks_small2.png"),
        entry('ロ', "Tiles/rocks_large.png"),
        entry('花', "Tiles/flower_wall_left.png"),
        entry('F', "Tiles/finish.png")
    );
>>>>>>> Stashed changes
}
