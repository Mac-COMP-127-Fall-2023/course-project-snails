import java.util.Map;
import static java.util.Map.entry;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public interface Tile{
    public Point getTopRightCorner();
    public Point getBottomRightCorner();
    public Point getTopLeftCorner();
    public Point getBottomLeftCorner();
    
    public boolean checkCollision(Point point);
    public boolean isCollidable();

    public Image getImage();

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
}
