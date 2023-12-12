import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public class Empty implements Tile{
    Image image;
    int x;
    int y;
    int width = Level.SCREEN_PIXELS_PER_TILE;
    int height = Level.SCREEN_PIXELS_PER_TILE;

    public Empty(Point topLeftPos) {
        image = new Image(topLeftPos.getX(), topLeftPos.getY(), "Tiles/empty.png");
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
       return false;
    }

    public Image getImage() {
        return image;
    }
}