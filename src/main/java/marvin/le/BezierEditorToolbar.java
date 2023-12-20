package marvin.le;

import java.awt.Color;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

public class BezierEditorToolbar extends JToolBar {
    private JButton createBezierCurve;
    private JButton deleteBezierCurve;
    public BezierEditorToolbar() {
        setFloatable(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        Image plusImage = Toolkit.getDefaultToolkit().getImage("./img/flat_icon_plus.png");
        Image minusImage = Toolkit.getDefaultToolkit().getImage("./img/flat_icon_minus.png");
        ImageIcon plusIcon = new ImageIcon(plusImage);
        ImageIcon minusIcon = new ImageIcon(minusImage);
        
        //new Bezier curve
        createBezierCurve = new JButton();
        createBezierCurve.setIcon(LEUtile.scaleIconTo(plusIcon, 25, 25));
        add(createBezierCurve);
        
        //remove Bezier curve
        deleteBezierCurve = new JButton();
        deleteBezierCurve.setIcon(LEUtile.scaleIconTo(minusIcon, 25, 25));
        add(deleteBezierCurve);
        
        validate();
    }
    
    public JButton getCreateButton() {
        return createBezierCurve;
    }
    
    public JButton getRemoveButton() {
        return deleteBezierCurve;
    }
}
