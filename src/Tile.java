import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.Image;

public class Tile extends Image{
    private final Map<Character, String> IMAGE_PATH_MAP = new HashMap<>();

    public Tile(int x, int y, char key) {
        super(x, y);
        setupImageCodeMap();
        setImagePath(IMAGE_PATH_MAP.get(key));
    }

    private void setupImageCodeMap() {
        IMAGE_PATH_MAP.put('░', "Tiles/rocks_small1.png");
        IMAGE_PATH_MAP.put('□', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('◯', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▃', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▍', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('|', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▙', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▟', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▛', "Tiles/block_i_dirt_rb.png");
        IMAGE_PATH_MAP.put('▜', "Tiles/block_e_dirt.png"); 
        IMAGE_PATH_MAP.put('▘', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▝', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▘', "Tiles/block_e_dirt.png");
        IMAGE_PATH_MAP.put('▝', "Tiles/block_e_dirt.png"); 
    }
}
