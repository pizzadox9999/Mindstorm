package marvin.ms.common;

public interface PrintHead {
    public void drawPoint(Point point);
    public void drawLine(Point start, Point end);
    public void drawTriangle(Point p1, Point p2, Point p3);
    public void drawCircle(Point point, float radius, Color color);
}
