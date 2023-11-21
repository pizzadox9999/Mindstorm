package marvin.le;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BezierCurveEditor extends JPanel implements Runnable {
    private Thread editorThread;
    private Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    private MouseInputHandler mouseInputHandler;
    private ArrayList<BezierCurve> bezierCurves = new ArrayList<>();
    private double bezierSegmentLength = 0.0001d;

    public BezierCurveEditor() {

        editorThread = new Thread(this);
        editorThread.start();

        setFocusable(true);

        mouseInputHandler = new MouseInputHandler();

        addMouseListener(mouseInputHandler);
        addMouseMotionListener(mouseInputHandler);
    }

    public void addBezierCurve(String id) {
        addBezierCurve(new BezierCurve(id, new ArrayList()));
    }

    public void addBezierCurve(BezierCurve bezierCurve) {
        bezierCurves.add(bezierCurve);
    }

    public BezierCurve getBezierCurve(String id) {
        for (BezierCurve bezierCurve : bezierCurves) {
            if (bezierCurve.id.equals(id)) {
                return bezierCurve;
            }
        }
        return null;
    }

    public MouseInputHandler getMouseInputHandler() {
        return mouseInputHandler;
    }

    @Override
    public void run() {
        while (editorThread != null) {
            update();
            repaint();
        }
    }

    public void update() {

    }

    private Graphics g;
    private Graphics2D g2;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;
        g2 = (Graphics2D) g;

        for (BezierCurve bezierCurve : bezierCurves) {
            boolean colorChanged = false;
            if(mouseInputHandler.getCurrentCurve() != null && mouseInputHandler.getCurrentCurve().equals(bezierCurve)) {
                g2.setColor(Color.BLUE);
                colorChanged = true;
            }
            
            drawCircles(bezierCurve.points, 20, true);
            drawBezier(bezierCurve.points, bezierSegmentLength);
            
            if(colorChanged) {
                g2.setColor(Color.BLACK);
            }
        }

        g2.dispose();

    }

    public void drawBezier(ArrayList<Point2D.Double> points, double segmentLength) {
        if (points.isEmpty()) {
            return;
        }

        Point2D.Double oldPoint = points.get(0);

        for (float t = 0; t <= 1.00001f; t += segmentLength) {
            Point2D.Double p = calcBezier(points, t);

            drawLine(oldPoint, p);

            oldPoint = p;
        }
    }

    public void drawLine(Point2D.Double p0, Point2D.Double p1) {
        g2.drawLine((int) p0.x, (int) p0.y, (int) p1.x, (int) p1.y);
    }

    public void drawCircles(ArrayList<Point2D.Double> points, double radius, boolean filled) {
        for (Point2D.Double p : points) {
            drawCircle(p, radius, filled);
        }
    }

    public void drawCircle(Point2D.Double p, double radius, boolean filled) {
        drawCircle(p.x, p.y, radius, filled);
    }

    public void drawCircle(double x, double y, double radius, boolean filled) {
        Ellipse2D circle = new Ellipse2D.Double(x, y, radius, radius);
        if (filled)
            g2.fill(circle);
        else
            g2.draw(circle);
    }

    public static Point2D.Double calcBezier(ArrayList<Point2D.Double> points, double t) {
        if (points.isEmpty())
            return new Point2D.Double();

        ArrayList<Point2D.Double> calced = new ArrayList<>();

        while (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; i++) {
                Point2D.Double p0 = points.get(i);
                Point2D.Double p1 = points.get(i + 1);

                Point2D.Double p = new Point2D.Double(lerp(p0.x, p1.x, t), lerp(p0.y, p1.y, t));

                calced.add(p);
            }

            points = new ArrayList<>(calced);
            calced.clear();
        }

        return points.get(0);
    }

    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }
}
