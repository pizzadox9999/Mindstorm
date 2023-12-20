package marvin.le;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

public class MouseInputHandler implements MouseListener, MouseMotionListener {
    public int mouseX = 0;
    public int mouseY = 0;
    public boolean mousePressed = false;
    
    private BezierCurve currentCurve = null;
    
    public void setCurrentCurve(BezierCurve curve) {
        currentCurve = curve;
    }
    
    public BezierCurve getCurrentCurve() {
        return currentCurve;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        if(currentCurve == null) 
            return;
        
        currentCurve.points.add(new Point2D.Double(e.getX(), e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
}
