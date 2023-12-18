import java.util.Map;
import static java.util.Map.entry;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

/***
 * Interface for all tiles in a level
 */
public interface Tile{
    //get bounds of tile
    public Point getTopRightCorner(); 
    public Point getBottomRightCorner();
    public Point getTopLeftCorner();
    public Point getBottomLeftCorner();
    
    public boolean checkCollision(Point point); //whether given point is within bounds of the tile
    public boolean isCollidable(); 
    public boolean canStickToSide(); //whether the snail can attach to tile

    public Image getImage();

    //Map of paths to all tile images, with the corresponding character as the key
    public static final Map<Character, String> IMAGE_PATH_MAP = Map.ofEntries(
        entry('　', "Tiles/empty.png"),
        entry('あ', "Tiles/block_e_dirt.png"),
        entry('＿', "Tiles/block_i_dirt_b.png"),
        entry('￣', "Tiles/block_i_dirt_t.png"),
        entry('「', "Tiles/block_i_dirt_r.png"),
        entry('」', "Tiles/block_i_dirt_l.png"),
        entry('・', "Tiles/block_i_dirt_tr.png"),
        entry('／', "Tiles/block_i_dirt_lt.png"),
        entry('４', "Tiles/block_i_dirt_rb.png"),
        entry('＋', "Tiles/block_i_dirt_lb.png"),
        entry('～', "Tiles/block_e_dirt_b.png"),
        entry('＝', "Tiles/block_e_dirt_t.png"),
        entry('｛', "Tiles/block_e_dirt_r.png"),
        entry('｝', "Tiles/block_e_dirt_l.png"),
        entry('⊕', "Tiles/block_e_dirt_tr.png"),
        entry('⊛', "Tiles/block_e_dirt_lt.png"),
        entry('✚', "Tiles/block_e_dirt_rb.png"),
        entry('＊', "Tiles/block_e_dirt_lb.png"),
        entry('上', "Tiles/block_e_dirt_ltr.png"),
        entry('右', "Tiles/block_e_dirt_tlb.png"),
        entry('左', "Tiles/block_e_dirt_trb.png"),
        entry('下', "Tiles/block_e_dirt_lbr.png"),
        entry('四', "Tiles/block_e_dirt_ltrb.png"),
        entry('ー', "Tiles/block_e_dirt_tb.png"),
        entry('｜', "Tiles/block_e_dirt_lr.png"),
        entry('フ', "Tiles/mushroom1.png"),
        entry('ブ', "Tiles/mushroom2.png"),
        entry('プ', "Tiles/mushroom3.png"),
        entry('ヒ', "Tiles/platform_thick_dirt_l.png"),
        entry('ビ', "Tiles/platform_thick_dirt_m.png"),
        entry('ピ', "Tiles/platform_thick_dirt_r.png"),
        entry('ひ', "Tiles/platform_thin_dirt_l.png"),
        entry('び', "Tiles/platform_thin_dirt_r.png"),
        entry('ラ', "Tiles/rocks_small1.png"),
        entry('ル', "Tiles/rocks_small2.png"),
        entry('ロ', "Tiles/rocks_large.png"),
        entry('花', "Tiles/flower_wall_left.png"),
        entry('え', "Tiles/finish.png"),
        entry('が', "Tiles/grass1_tall1.png"),
        entry('ぎ', "Tiles/grass1_tall2.png")
    );
}
