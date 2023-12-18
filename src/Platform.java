import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

/***
 * Basic building block of levels
 * collidable, snail can't attach to sides
 */
class Platform implements Tile{
    Image image;
    int x;
    int y;
    int width = Level.SCREEN_PIXELS_PER_TILE;
    int height = Level.SCREEN_PIXELS_PER_TILE;

    public Platform(Point topLeftPos, char key) {
        String imagePath = IMAGE_PATH_MAP.get(key);
        if (imagePath.substring(imagePath.length() - 5, imagePath.length() - 4) == "l") {
            topLeftPos = new Point((int)topLeftPos.getX() - Level.SCREEN_PIXELS_PER_TILE + width, topLeftPos.getX());
        }
        image = new Image(topLeftPos.getX(), topLeftPos.getY(), IMAGE_PATH_MAP.get(key));
        image.setScale((double)SnailGame.SCREEN_PIXEL_RATIO / 6);
        x = (int)topLeftPos.getX();
        y = (int)topLeftPos.getY();
    }
    @Override
    public boolean isCollidable(){
        return true;
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
        return new Point(x + width, y);
    }
     @Override
    public Point getBottomLeftCorner(){
        return new Point(x, y);
    }
     @Override
    public boolean checkCollision(Point point) {
        return image.testHit(point.getX(), point.getY() + width);
    }
     @Override
    public Image getImage() {
        return image;
    }
}