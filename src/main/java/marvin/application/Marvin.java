package marvin.application;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import marvin.application.geom.Rectangle;
import marvin.le.BezierCurveEditor;
import marvin.web.MarvinWebserver;

public class Marvin {
    public static Printer printer;
    
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
	    //setup one printer
	    printer = new Printer();
	    
	    //start webserver
	    MarvinWebserver.main(args);
	    
	    //should only execute after the webserver was shutdown
	    printer.finish();
	    System.exit(0);
	    
	    //Acceleration and deceleration test
	    /*EV3LargeRegulatedMotor paperEngine = new EV3LargeRegulatedMotor(MotorPort.B);
	    
	    paperEngine.setSpeed(paperEngine.getSpeed());
	    
	    paperEngine.forward();
	    
	    System.out.println("first wait start");
	    waitFor(2d);
	    System.out.println("first wait end");
	    
	    
	    paperEngine.setAcceleration(10);
	    paperEngine.setSpeed(0);
	    
	    
	    System.out.println("second wait start");
	    waitFor(5d);
	    System.out.println("second wait end");
	    
	    paperEngine.stop();
	    System.out.println("motor should have stopped");
	    
	    System.exit(0);
	    */
	    //draw circle
		
		Printer printer = new Printer();
		
		//printer.drawLine(10, 57, 150, 77);
		//printer.drawLine(300, 70, 150, 70);
		Rectangle rectangle = new Rectangle(80, 10, 40, 40);
		rectangle.draw(printer);
		rectangle = new Rectangle(80, 60, 45, 45);
		rectangle.draw(printer);
		printer.finish();
		
		System.exit(0);
		
		printer.moveToPosition(100, 100);
		
		waitFor(1000);
		
		printer.drawCircle(20);
		//printer.drawCircle(20, 2);
		//printer.drawCircle(20, 3);
		
		waitFor(1000);
		
		printer.finish();
		
		
		System.exit(0);
	    ArrayList<Point2D.Double> points = new ArrayList<>();
	    points.add(new Point2D.Double(120, 30));
	    points.add(new Point2D.Double(75, 120));
	    points.add(new Point2D.Double(30, 30));
	    
	    printer = new Printer();
	    
	    printer.moveToPosition(100, 100);
	    
	    
		
	    drawBezier(points, bezierSegmentLength, printer);
		
		printer.finish();
		
		
	}
}