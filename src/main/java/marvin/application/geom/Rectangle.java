package marvin.application.geom;

import marvin.application.Printer;

public class Rectangle implements Shape {
    public static final String ID = "RECTANGLE_SHAPE";
    
    private double startX;
    private double startY;
    private double width;
    private double height;
    
    public Rectangle(double startX, double startY, double width, double height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }
    
    public void draw(Printer printer) {
        double p0x = startX;
        double p0y = startY;
        double p1x = p0x + width;
        double p1y = startY;        
        double p2x = p1x;
        double p2y = p1y + height;
        double p3x = p0x;
        double p3y = p0y + height;
        
        printer.moveToPosition(startX, startY);
        printer.startLineSegment();
        printer.drawLineSegment(p0x, p0y, p1x, p1y);
        printer.drawLineSegment(p1x, p1y, p2x, p2y);
        printer.drawLineSegment(p2x, p2y, p3x, p3y);
        printer.drawLineSegment(p3x, p3y, p0x, p0y);
        printer.endLineSegment();
        
        printer.resetAcceleration();
        printer.resetSpeed();
    }
    
    @Override
    public String toString() {
        return ID + "{startX: " + startX + " startY : " + startY + " width: " + width + " height: " + height + "}";
    }
}
