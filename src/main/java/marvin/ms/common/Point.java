package marvin.ms.common;

public class Point {
    private float x;
    private float y;
    private Color color;
    
    public Point(float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public Color getColor() {
        return color;
    }
}
