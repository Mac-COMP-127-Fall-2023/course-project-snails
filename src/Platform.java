import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

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
        return new Point(x + width, y);
    }
    public Point getBottomLeftCorner(){
        return new Point(x, y);
    }
    public boolean checkCollision(Point point) {
        return image.testHit(point.getX(), point.getY() + width);
    }
    public Image getImage() {
        return image;
    }
}