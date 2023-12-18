import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

/*
 * DO TO: 
 *  add a finishing point visual of some kind (** this will also
 *  be used in SnailGame to see if the snail should proceed to the 
 *  next level)
 */
class Level {
    private GraphicsGroup terrainLayer = new GraphicsGroup();
    private  GraphicsGroup decorationLayer = new GraphicsGroup();
    private Endpoint endpoint;
    private Map<Point, Tile> tileMap = new HashMap<>();

    public static final int PIXELS_PER_TILE = 16; //the number of pixels that make up one tile/unit

    public static final int SCREEN_PIXELS_PER_TILE = PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO;

    private List<Character> blockKeys = List.of('￣', '＿', '「', '」', '・', '／', '４', '＋', '～','＝','｛','｝','⊕','⊛','✚','＊', 'あ', '上', '下', '左', '右','ー','｜','四');
    private List<Character> decorationKeys = List.of('フ','ブ','プ','ラ','ル','ロ');
    private List<Character> platformKeys = List.of('ヒ', 'ビ', 'ピ', 'ひ', 'び');
    private Character endpointKey = 'Ⓕ';

    private Snail snail; 

    public Level(String mapStr) {
        int tileX = 0; //x position in the unit of tiles (16*16 game pixels, 96*96 screen pixels)
        int tileY = 0; //y position in tiles
        for (String tileRow : mapStr.split("\\r?\\n")) { //iterate through each line of the multiline string
            for (char tileKey : tileRow.toCharArray()) { //iterate through each char in line
                Point topLeftPos = new Point(
                    tileX * SCREEN_PIXELS_PER_TILE,
                    tileY * SCREEN_PIXELS_PER_TILE);
                Tile newTile;

                if (tileKey=='す'||tileKey == 'ず') {
                    if (tileKey=='ず') {
                        snail = new Snail(topLeftPos); //set the position of the snail to the top left of the current tile
                    }
                    tileKey = ' '; //place an empty tile in the bg
                }
                if (tileKey == endpointKey) {
                    newTile = new Endpoint(topLeftPos);
                    endpoint = (Endpoint)newTile;
                }
                else if (platformKeys.contains(tileKey)) {
                    newTile = new Platform(topLeftPos, tileKey); //create a new tile based on the character it reads
                    terrainLayer.add(newTile.getImage());
                } 
                else if (blockKeys.contains(tileKey)) {
                    newTile = new Block(topLeftPos, tileKey); 
                    terrainLayer.add(newTile.getImage());
                }
                else if (decorationKeys.contains(tileKey)) {
                    newTile = new Decoration(topLeftPos, tileKey);
                    decorationLayer.add(newTile.getImage());
                }
                else {
                    newTile = new Empty(topLeftPos);
                }
                tileMap.put(new Point(tileX, tileY), newTile); //key for each tile is its x and y coordinates in tiles
                tileX++;
            }
            tileX = 0;
            tileY++;
        }   
        snail.setOrientation(Snail.Orientation.BOTTOM);
    }

    public GraphicsGroup getGraphics() {
        GraphicsGroup group = new GraphicsGroup();
        group.add(decorationLayer);
        group.add(terrainLayer);
        group.add(endpoint.getImage());
        return group;
    }

    public boolean checkCollision(Point p){
        return terrainLayer.testHit(p.getX(), p.getY());
    }

    public Snail getSnail() {
        return snail;
    }

    public void updateAttachedTileOfSnail(){
        Tile newTile = getCollidableTileAt(snail.getMiddleOfOrientation());
        if(newTile != null){
             snail.updateAttachedTile(newTile);
        }
    }
    
    private Tile getCollidableTileAt(Point p){
        for (Tile tile : tileMap.values()) {
            if (tile.checkCollision(p)) {
                return tile;
            }
        }
      return null;
    }

    public boolean getCompleted(){
        for(Point p : snail.getInnerBoundaryPoints()){
            if(endpoint.getImage().testHit(p.getX(), p.getY())){
                return true;
            }
        }
        return false;
    }
}