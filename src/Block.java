import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

class Block implements Tile{
    Image image;
    int x;
    int y;
    int width = Level.SCREEN_PIXELS_PER_TILE;
    int height = Level.SCREEN_PIXELS_PER_TILE;

    public Block(Point topLeftPos, char key) {
        image = new Image(topLeftPos.getX(), topLeftPos.getY(), IMAGE_PATH_MAP.get(key) == null ? "Tiles/empty.png" : IMAGE_PATH_MAP.get(key));
        image.setScale((double)SnailGame.SCREEN_PIXEL_RATIO / 6);
        x = (int)topLeftPos.getX();
        y = (int)topLeftPos.getY();
    }

    public boolean isCollidable(){
        return true;
    }

    public Point getTopLeftCorner(){
        return new Point(x, y);
    }

    public Point getTopRightCorner(){
        return new Point(x + width, y);
    }

    public Point getBottomRightCorner(){
        return new Point(x + width, y + height);
    }

    public Point getBottomLeftCorner(){
        return new Point(x, y + height - SnailGame.SCREEN_PIXEL_RATIO);
    }

    public boolean checkCollision(Point point) {
       return image.testHit(point.getX(), point.getY());
    }

    public Image getImage() {
        return image;
    }
}
