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

    @Override
    public boolean isCollidable(){
        return false;
    }
    @Override
    public boolean canStickToSide(){
        return false;
    }
    @Override
    public Point getTopLeftCorner(){
        return new Point(x, y);
    }

    @Override
    public Point getTopRightCorner(){
        return new Point(x + width, y);
    }

    @Override
    public Point getBottomRightCorner(){
        return new Point(x + width, y + height);
    }

    @Override
    public Point getBottomLeftCorner(){
        return new Point(x, y + height - SnailGame.SCREEN_PIXEL_RATIO);
    }

    @Override
    public boolean checkCollision(Point point) {
       return false;
    }

    @Override
    public Image getImage() {
        return image;
    }
}