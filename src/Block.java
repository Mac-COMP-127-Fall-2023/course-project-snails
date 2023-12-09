import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public class Block implements Tile{
    Image image;
    int x;
    int y;
    int width = Level.SCREEN_PIXELS_PER_TILE;
    int height = Level.SCREEN_PIXELS_PER_TILE;

    public Block(Point topLeftPos, char key) {
        image = new Image(topLeftPos.getX(), topLeftPos.getY(), !IMAGE_PATH_MAP.get(key).isEmpty() ? IMAGE_PATH_MAP.get(key) : "Tiles/empty.png");
        image.setScale((double)SnailGame.SCREEN_PIXEL_RATIO / 6);

        x = (int)topLeftPos.getX();
        y = (int)topLeftPos.getY();
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
        return new Point(x, y + height);
    }

    public Image getImage() {
        return image;
    }
}
