package marvin.le;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class BezierOverviewTreeCellRenderer extends DefaultTreeCellRenderer {
    private ImageIcon letterIcon;
    ImageIcon bezierIcon;
    ImageIcon pointIcon;

    public BezierOverviewTreeCellRenderer() {
        this(24, 24);
    }

    public BezierOverviewTreeCellRenderer(int iconWidth, int iconHeight) {
        super();
        // load icons
        letterIcon = new ImageIcon("./img/flat_icon_letters.png");
        bezierIcon = new ImageIcon("./img/flat_icon_bezier.png");
        pointIcon = new ImageIcon("./img/flat_icon_point.png");

        // scale icons
        letterIcon = LEUtile.scaleIconTo(letterIcon, iconWidth, iconHeight);
        bezierIcon = LEUtile.scaleIconTo(bezierIcon, iconWidth, iconHeight);
        pointIcon = LEUtile.scaleIconTo(pointIcon, iconWidth, iconHeight);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        
        //System.out.println(userObject.getClass().getName());
        
        if (!(userObject instanceof RenderComponent))
            return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        
        //still apply all the things from the default
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        
        RenderComponent renderComponent = ((RenderComponent) ((DefaultMutableTreeNode) value).getUserObject());

        Icon icon = null;
        if (renderComponent.id == RenderComponent.LETTER) {
            icon = letterIcon;
        } else if (renderComponent.id == RenderComponent.CURVE) {
            icon = bezierIcon;
        } else if (renderComponent.id == RenderComponent.POINT) {
            icon = pointIcon;
        }

        setIcon(icon);
        setText(renderComponent.content);

        return this;
    }

}