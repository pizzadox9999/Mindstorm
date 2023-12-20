package marvin.le;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BezierCurve {
    public String id;
    public ArrayList<Point2D.Double> points;
    
    public BezierCurve(String id, ArrayList<Point2D.Double> points) {
        this.id = id;
        this.points = points;
    }
    
    public boolean equals(BezierCurve bezierCurve) {
        return id.equals(bezierCurve.id);
    }
}
