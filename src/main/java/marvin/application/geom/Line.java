package marvin.application.geom;

import marvin.application.Printer;

public class Line implements Shape {
    public static final String ID = "LINE_SHAPE";
    
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    
    public Line(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public void draw(Printer printer) {
        printer.drawLine(startX, startY, endX, endY);
    }
    
    @Override
    public String toString() {
        return ID + "{startX: " + startX + " startY : " + startY + " endX: " + endX + " endY: " + endY + "}";
    }
}
