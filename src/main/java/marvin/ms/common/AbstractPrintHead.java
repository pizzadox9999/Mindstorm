package marvin.ms.common;

abstract public class AbstractPrintHead implements PrintHead {
    protected PrintHeadConfig defaultPrintHeadConfig;
    private Function linearFunction;
    
    protected AbstractPrintHead(boolean smooth, boolean blackAndWhite) {
        this(new PrintHeadConfig());
    }
    
    protected AbstractPrintHead(PrintHeadConfig defaultPrintHeadConfig) {
        this.defaultPrintHeadConfig = defaultPrintHeadConfig;
        linearFunction = new LinearFunction();
    }
    
    public void drawPoint(Point point) {
        drawPoint(point, defaultPrintHeadConfig);
    }
    
    public void drawPoint(Point point, PrintHeadConfig printHeadConfig) {
        
    }
    
    public void drawLine(Point start, Point end) {
        drawLine(start, end, defaultPrintHeadConfig);
    }
    
    public void drawLine(Point start, Point end, PrintHeadConfig printHeadConfig) {
        drawFunction(linearFunction, start, end, printHeadConfig);
    }
    
    public void drawFunction(Function function, Point start, Point end) {
        drawFunction(function, start, end, defaultPrintHeadConfig);
    }
    
    public void drawFunction(Function function, Point start, Point end, PrintHeadConfig printHeadConfig) {
        
    }
    
    public void drawTriangle(Point p1, Point p2, Point p3) {
        drawTriangle(p1, p2, p3, defaultPrintHeadConfig);
    }
    
    public void drawTriangle(Point p1, Point p2, Point p3, PrintHeadConfig printHeadConfig) {
        
    }
    
    public void drawCircle(Point point, float radius, Color color) {
        drawCircle(point, radius, color, defaultPrintHeadConfig);
    }
    
    public void drawCircle(Point point, float radius, Color color, PrintHeadConfig printHeadConfig) {
        
    }
}
