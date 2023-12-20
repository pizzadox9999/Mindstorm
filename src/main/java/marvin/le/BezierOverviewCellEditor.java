package marvin.le;

import java.awt.Component;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

public class BezierOverviewCellEditor extends DefaultTreeCellEditor {
    
    private JPanel editorPanel;
    private JLabel icon;
    private JTextField textField;
    
    public BezierOverviewCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
        
        editorPanel = new JPanel();
        icon = new JLabel(LEUtile.scaleIconTo(new ImageIcon("./img/flat_icon_pencil.png"), 24, 24));
        textField = new JTextField("test");
        
        editorPanel.add(icon);
        editorPanel.add(textField);
    }
    
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
            boolean leaf, int row) {
        
        Container container = (Container) super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row).getParent();
        
        System.out.println(container.getClass().getName());
        
        if( !(container instanceof JTextField)) {
            return container;
        }
        
        ((JTextField) container).setText("test");
        
        
        
        return container;
    }

}
