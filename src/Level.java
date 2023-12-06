import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

/*
 * DO TO: 
 * 1. add 2 layers (2 different graphics groups): the 
 *    background, and the top layer that the snail can interact with
 * 2. add a finishing point visual of some kind (** this will also
 *    be used in SnailGame to see if the snail should proceed to the 
 *    next level)
 */
class Level {
    GraphicsGroup collidableGroup = new GraphicsGroup();
    GraphicsGroup background = new GraphicsGroup();
    Map<Point, Tile> tileMap = new HashMap<>();
    double scale = (double)SnailGame.SCREEN_PIXEL_RATIO / 6;

    private final int PIXELS_PER_TILE = 16; //the number of pixels that make up one tile/unit

    private List<Character> collidableKeys = List.of('-', '_', '[', ']', '\\', '/', '4', '+', '▘', '▝', '▝');

    private Snail snail; 

    public Level(String mapStr) {
        int tileX = 0; //x position in the unit of tiles (16*16 game pixels, 96*96 screen pixels)
        int tileY = 0; //y position in tiles
        for (String tileRow : mapStr.split("\\r?\\n")) { //iterate through each line of the multiline string
            for (char tileKey : tileRow.toCharArray()) { //iterate through each char in line
                if (tileKey == 'S') {
                    tileKey = ' '; //place an empty tile
                    snail = new Snail(new Point((tileX - 1 + Math.pow(scale, 2)) * PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO, (tileY - 2 + Math.pow(scale, 2)) * PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO), scale); //set the position of the snail to the bottom of the current tile
                }
                Tile newTile = new Tile((tileX - 1 + scale) * PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO, (tileY - 1 + scale) * PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO, tileKey); //create a new tile based on the character it reads
                newTile.setScale(scale);
                tileMap.put(new Point(tileX, tileY), newTile); //key for each tile is its x and y coordinates in tiles
                
                if(collidableKeys.contains(tileKey)){
                     collidableGroup.add(newTile);
                }
                else{
                    background.add(newTile);
                }
               
                tileX++;
            }
            tileX = 0;
            tileY++;
        }   

    }

    /**
     * @param screenX X coordinate within canvas
     * @param screenY Y coordinate within canvas
     * @return Tile object at given coordinates
     */
    public Tile getTile(int screenX, int screenY) {
        return tileMap.get(new Point((int)(screenX / PIXELS_PER_TILE / SnailGame.SCREEN_PIXEL_RATIO), (int) (screenY / PIXELS_PER_TILE / SnailGame.SCREEN_PIXEL_RATIO))); //converts screen coordinates to tile coordinates, uses tile coords as key
    }

    public GraphicsGroup getGraphics() {
        GraphicsGroup group = new GraphicsGroup();
        group.add(background);
        group.add(collidableGroup);
        return group;
    }

    public boolean checkCollision(Point p){
        return collidableGroup.testHit(p.getX(), p.getY());
    }

    public Snail getSnail() {
        return snail;
    }
}