import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

class Level {
    private final Map<Character, String> IMAGE_PATH_MAP = new HashMap<>();

    GraphicsGroup group = new GraphicsGroup();
    Map<Point, Tile> tileMap = new HashMap<>();

    private final int PIXELS_PER_UNIT = 16;

    public Level(String mapStr) {
        setupImageCodeMap();
        int xi = 0;
        int yi = 0;
        for (String tileRow : mapStr.split("\\r?\\n")) {
            for (char tileCode : tileRow.toCharArray()) {
                Tile newTile = new Tile(xi * PIXELS_PER_UNIT * SnailGame.PIXEL_RATIO, yi * PIXELS_PER_UNIT * SnailGame.PIXEL_RATIO, IMAGE_PATH_MAP.get(tileCode));
                tileMap.put(new Point(xi, yi), newTile);
                group.add(newTile);

                xi += PIXELS_PER_UNIT * SnailGame.PIXEL_RATIO;
            }
            yi += PIXELS_PER_UNIT * SnailGame.PIXEL_RATIO;
        }   

    }

    public Tile getTile(int x, int y) {
        return tileMap.get(new Point((int)(x / PIXELS_PER_UNIT / SnailGame.PIXEL_RATIO), (int) (y / PIXELS_PER_UNIT / SnailGame.PIXEL_RATIO)));
    }

    public GraphicsGroup getGraphics() {
        return group;
    }

    private void setupImageCodeMap() {
        IMAGE_PATH_MAP.put('░', "Tiles\\empty.png");
        IMAGE_PATH_MAP.put('█', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▀', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▄', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▌', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▐', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▙', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▟', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▛', "Tiles\\block_i_dirt_rb.png");
        IMAGE_PATH_MAP.put('▜', "Tiles\\block_e_dirt.png"); 
        IMAGE_PATH_MAP.put('▘', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▝', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▘', "Tiles\\block_e_dirt.png");
        IMAGE_PATH_MAP.put('▝', "Tiles\\block_e_dirt.png"); 
    }
}