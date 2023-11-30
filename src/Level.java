import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

class Level {
    GraphicsGroup group = new GraphicsGroup();
    Map<Point, Tile> tileMap = new HashMap<>();

    private final int PIXELS_PER_TILE = 16; //the number of pixels that make up one tile/unit

    public Level(String mapStr) {
        int tileX = 0;
        int tileY = 0;
        for (String tileRow : mapStr.split("\\r?\\n")) { //iterate through each line of the multiline string
            for (char tileKey : tileRow.toCharArray()) { //iterate through each char in line
                Tile newTile = new Tile(tileX * PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO, tileY * PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO, tileKey); //create a new tile based on the character it reads
                tileMap.put(new Point(tileX, tileY), newTile);
                group.add(newTile);

                tileX++;
            }
            tileX = 0;
            tileY++;
        }   

    }

    public Tile getTile(int screenX, int screenY) {
        return tileMap.get(new Point((int)(screenX / PIXELS_PER_TILE / SnailGame.SCREEN_PIXEL_RATIO), (int) (screenY / PIXELS_PER_TILE / SnailGame.SCREEN_PIXEL_RATIO))); //converts screen coordinates to tile coordinates, uses tile coords as key
    }

    public GraphicsGroup getGraphics() {
        return group;
    }
}