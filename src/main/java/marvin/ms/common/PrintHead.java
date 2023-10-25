package marvin.ms.common;

public interface PrintHead {
    public void drawPoint(Point point);
    public void drawPoint(Point point, PrintHeadConfig printHeadConfig);
    public void drawLine(Point start, Point end);
    public void drawLine(Point start, Point end, PrintHeadConfig printHeadConfig);
    public void drawFunction(Function function, Point start, Point end);
    public void drawFunction(Function function, Point start, Point end, PrintHeadConfig printHeadConfig);
    public void drawTriangle(Point p1, Point p2, Point p3);
    public void drawTriangle(Point p1, Point p2, Point p3, PrintHeadConfig printHeadConfig);
    public void drawCircle(Point point, float radius, Color color);
    public void drawCircle(Point point, float radius, Color color, PrintHeadConfig printHeadConfig);
}
