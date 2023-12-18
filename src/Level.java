import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

class Level {
    private GraphicsGroup terrainLayer = new GraphicsGroup();
    private  GraphicsGroup decorationLayer = new GraphicsGroup();
    private GraphicsGroup background = new GraphicsGroup();
    private Endpoint endpoint;
    private Map<Point, Tile> tileMap = new HashMap<>();

    public static final int PIXELS_PER_TILE = 16; //the number of pixels that make up one tile/unit

    public static final int SCREEN_PIXELS_PER_TILE = PIXELS_PER_TILE * SnailGame.SCREEN_PIXEL_RATIO;

    private List<Character> blockKeys = List.of('￣', '＿', '「', '」', '・', '／', '４', '＋', '～','＝','｛','｝','⊕','⊛','✚','＊', 'あ', '上', '下', '左', '右','ー','｜','四');
    private List<Character> decorationKeys = List.of('フ','ブ','プ','ラ','ル','ロ');
    private List<Character> platformKeys = List.of('ヒ', 'ビ', 'ピ', 'ひ', 'び');
    private Character endpointKey = 'Ⓕ';

    private Snail snail;
    private boolean won=false;

    public Level(String mapStr, String backgroundString) {
        parseKana(mapStr, true);
        parseKana(backgroundString, false);
    }

    public GraphicsGroup getGraphics() {
        GraphicsGroup group = new GraphicsGroup();
        group.add(decorationLayer);
        group.add(terrainLayer);
        group.add(endpoint.getImage());
        return group;
    }

    public GraphicsGroup getBackground() {
        return background;
    }

    private void parseKana(String str, Boolean map){
        int tileX = 0;
        int tileY = 0;
        for (String tileRow : str.split("\\r?\\n")) { 
            for (char tileKey : tileRow.toCharArray()) { 
                Point topLeftPos = new Point(tileX * SCREEN_PIXELS_PER_TILE, tileY * SCREEN_PIXELS_PER_TILE);
                Tile newTile;
                if (map) {
                    newTile = parseMapTile(topLeftPos, tileKey);
                    tileMap.put(new Point(tileX, tileY), newTile);
                } else {
                    parseBgTile(topLeftPos, tileKey);
                }
                tileX++;
            }
            tileX = 0;
            tileY++;
        }
    }

    private Tile parseMapTile(Point p, char c) {
        Tile newTile;
        if (c=='す'||c == 'ず') {
            if (c=='ず') {
                snail = new Snail(this, p); //set the position of the snail to the top left of the current tile
            }
            c = ' ';
        }
        if (c == endpointKey) {
            newTile = new Endpoint(p);
            endpoint = (Endpoint)newTile;
        } else if (platformKeys.contains(c)) {
            newTile = new Platform(p, c);
            terrainLayer.add(newTile.getImage());
        } else if (blockKeys.contains(c)) {
            newTile = new Block(p, c); 
            terrainLayer.add(newTile.getImage());
        } else if (decorationKeys.contains(c)) {
            newTile = new Decoration(p, c);
            decorationLayer.add(newTile.getImage());
        } else {
            newTile = new Empty(p);
            decorationLayer.add(newTile.getImage());
        }
        return newTile;
    }

    private void parseBgTile(Point p, char c) {
        Decoration bgt = new Decoration(p, c);
        background.add(bgt.getImage());
    }

    public boolean checkCollision(Point p){
        return terrainLayer.testHit(p.getX(), p.getY());
    }

    public Snail getSnail() {
        return snail;
    }
    
    public Tile getCollidableTileAt(Point p){
        for (Tile tile : tileMap.values()) {
            if (tile.checkCollision(p)) {
                return tile;
            }
        }
      return null;
    }

    //only triggers once, called by snail at the end of move() every frame
    public boolean getCompleted(){
        if (won){
            return false;
        }
        Image g = endpoint.getImage();
        for (Point p : snail.getShellPoints()){
            if(g.testHit(p.getX(), p.getY())){
                won = true;
            }
        }
        return won;
    }
}