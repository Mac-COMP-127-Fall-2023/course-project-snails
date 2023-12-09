import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

import java.awt.Color;

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

    public static final int PIXELS_PER_TILE = 16; //the number of pixels that make up one tile/unit

    public static final int SCREEN_PIXELS_PER_TILE = PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO;

    private List<Character> collidableKeys = List.of('-', '_', '[', ']', '\\', '/', '4', '+', '▘', '▝', '▝');

    private Snail snail; 

    public Level(String mapStr) {
        int tileX = 0; //x position in the unit of tiles (16*16 game pixels, 96*96 screen pixels)
        int tileY = 0; //y position in tiles
        for (String tileRow : mapStr.split("\\r?\\n")) { //iterate through each line of the multiline string
            for (char tileKey : tileRow.toCharArray()) { //iterate through each char in line
                Point topLeftPos = new Point(
                    tileX * SCREEN_PIXELS_PER_TILE,
                    tileY * SCREEN_PIXELS_PER_TILE);
                    
                if (tileKey=='s'||tileKey == 'S') {
                    if (tileKey=='S') {
                        snail = new Snail(topLeftPos); //set the position of the snail to the top left of the current tile
                    }
                    tileKey = ' '; //place an empty tile in the bg
                }
                Tile newTile = new Block(topLeftPos, tileKey); //create a new tile based on the character it reads
                tileMap.put(new Point(tileX, tileY), newTile); //key for each tile is its x and y coordinates in tiles
                
                if(collidableKeys.contains(tileKey)){
                     collidableGroup.add(newTile.getImage());
                } else{
                    background.add(newTile.getImage());
                }
               
                tileX++;
            }
            tileX = 0;
            tileY++;
        }   
        snail.setOrientation(Snail.Orientation.BOTTOM);
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

    private Tile getCollidableTileAt(Point p){
        return (Tile)collidableGroup.getElementAt(p);
    }

    public void updateAttachedTileOfSnail(){
        Tile newTile = getCollidableTileAt(new Point(snail.getMiddleOfOrientation().getX(), snail.getMiddleOfOrientation().getY()));
        if(newTile != null){
             snail.setAttachedTile(newTile);
        }
    }
}