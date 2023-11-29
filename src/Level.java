import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

class Level {
    private final Map<Character, Tile> tileCodesMap = new HashMap<>();

    GraphicsGroup group = new GraphicsGroup();
    Map<Point, Tile> tileMap = new HashMap<>();

    public Level(String mapStr) {
        setupCodeMap();
        int x = 0;
        int y = 0;
        for (String tileRow : mapStr.split("\\r?\\n")) {
            for (char tileCode : tileRow.toCharArray()) {
                tileMap.put(new Point(x, y), tileCodesMap.get(tileCode));
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tileMap.get(new Point(x, y));
    }

    private void setupCodeMap() {
        tileCodesMap.put('░', new Tile(0, 0, null));
        tileCodesMap.put('█', new Tile(0, 0, null));
        tileCodesMap.put('▀', new Tile(0, 0, null));
        tileCodesMap.put('▄', new Tile(0, 0, null));
        tileCodesMap.put('▌', new Tile(0, 0, null));
        tileCodesMap.put('▐', new Tile(0, 0, null));
        tileCodesMap.put('▙', new Tile(0, 0, null));
        tileCodesMap.put('▟', new Tile(0, 0, null));
        tileCodesMap.put('▛', new Tile(0, 0, null));
        tileCodesMap.put('▜', new Tile(0, 0, null)); 
        tileCodesMap.put('▘', new Tile(0, 0, null));
        tileCodesMap.put('▝', new Tile(0, 0, null)); 
    }
}