import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

class Block implements Tile{
    Image image;
    int x;
    int y;
    int width = Level.SCREEN_PIXELS_PER_TILE;
    int height = Level.SCREEN_PIXELS_PER_TILE;
    boolean isCollidable;

    public Block(Point topLeftPos, char key, boolean isCollidable) {
        image = new Image(topLeftPos.getX(), topLeftPos.getY(), !IMAGE_PATH_MAP.get(key).isEmpty() ? IMAGE_PATH_MAP.get(key) : "Tiles/empty.png");
        image.setScale((double)SnailGame.SCREEN_PIXEL_RATIO / 6);

        this.isCollidable = isCollidable;

        x = (int)topLeftPos.getX();
        y = (int)topLeftPos.getY();
    }

    public boolean isCollidable(){
        return isCollidable;
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
