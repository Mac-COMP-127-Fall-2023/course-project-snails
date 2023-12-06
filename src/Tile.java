import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.Image;

public class Tile extends Image{
    private final Map<Character, String> IMAGE_PATH_MAP = new HashMap<>();

    public Tile(double x, double y, char key) {
        super(x, y);
        setupImagePathMap();
        setImagePath(!IMAGE_PATH_MAP.get(key).isEmpty() ? IMAGE_PATH_MAP.get(key) : "Tile/empty.png");
    }

    private void setupImagePathMap() {
        IMAGE_PATH_MAP.put(' ', "Tiles/empty.png"); //not a space, \u0020
        IMAGE_PATH_MAP.put('.', "Tiles/rocks_small1.png");
        IMAGE_PATH_MAP.put('-', "Tiles/block_i_dirt_b.png");
        IMAGE_PATH_MAP.put('_', "Tiles/block_i_dirt_t.png");
        IMAGE_PATH_MAP.put('[', "Tiles/block_i_dirt_r.png");
        IMAGE_PATH_MAP.put(']', "Tiles/block_i_dirt_l.png");
        IMAGE_PATH_MAP.put('\\', "Tiles/block_i_dirt_tr.png");
        IMAGE_PATH_MAP.put('/', "Tiles/block_i_dirt_lt.png");
        IMAGE_PATH_MAP.put('4', "Tiles/block_i_dirt_rb.png");
        IMAGE_PATH_MAP.put('+', "Tiles/block_i_dirt_lb.png"); 
        IMAGE_PATH_MAP.put('▘', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▝', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▘', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▝', "Tiles/block_e_dirt.png"); 
    }
}
