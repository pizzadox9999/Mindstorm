package marvin.application;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import marvin.le.BezierCurveEditor;

public class Marvin {
    
    private static double bezierSegmentLength = 0.004;
    
	public static void waitFor(long millis) {
	    try {
            Thread.sleep(Math.abs(millis));
        } catch (Exception e) {}
	}
	
	public static void waitFor(double seconds) {
	    waitFor(((long) seconds * 1000));
	}
	
	public static void drawBezier(ArrayList<Point2D.Double> points, double segmentLength, Printer printer) {
        if (points.isEmpty()) {
            return;
        }
        
        Point2D.Double oldPoint = points.get(0);
        printer.moveToPosition(oldPoint.x, oldPoint.y);
        printer.startLineSegment();
        for (float t = 0; t <= 1.00001f; t += segmentLength) {
            Point2D.Double p = BezierCurveEditor.calcBezier(points, t);

            printer.drawLineSegment(oldPoint.x, oldPoint.y, p.x, p.y);

            oldPoint = p;
        }
        printer.endLineSegment();
    }
	
	public static void main(String[] args) {
	    //draw circle
		
		/*Printer printer = new Printer();
	printer.moveToPosition(100, 100);
		waitFor(1000);
		printer.drawCircle(20);
		//printer.drawCircle(20, 2);
		//printer.drawCircle(20, 3);
		waitFor(1000);
		printer.finish();
		
		*/
		//System.exit(0);
	    ArrayList<Point2D.Double> points = new ArrayList<>();
	    points.add(new Point2D.Double(120, 30));
	    points.add(new Point2D.Double(75, 120));
	    points.add(new Point2D.Double(30, 30));
	    
	    Printer printer = new Printer();
	    
	    printer.moveToPosition(100, 100);
	    
	    
		
	    drawBezier(points, bezierSegmentLength, printer);
		
		printer.finish();
	}
}