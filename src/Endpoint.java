import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public class Endpoint implements Tile{
    Image image;
    int x;
    int y;
    int width = Level.SCREEN_PIXELS_PER_TILE;
    int height = Level.SCREEN_PIXELS_PER_TILE;

    public Endpoint(Point topLeftPos) {
        image = new Image("Tiles/grass2.png"); //temp
        image.setScale((double)SnailGame.SCREEN_PIXEL_RATIO / 6);

        x = (int)topLeftPos.getX();
        y = (int)topLeftPos.getY();
        image.setPosition(x,y);
    }

    public Point getTopLeftCorner() {
        return new Point(x, y);
    }

    public Point getTopRightCorner() {
        return new Point(x + width, y);
    }

    public Point getBottomRightCorner() {
        return new Point(x + width, y + height);
    }

    public Point getBottomLeftCorner() {
        return new Point(x, y + height);
    }

    public boolean checkCollision(Point point) {
        return image.testHit(point.getX(), point.getY());
    }

    public Image getImage() {
        return image;
    }
    
    public boolean isCollidable(){
        return false;
    }
}
