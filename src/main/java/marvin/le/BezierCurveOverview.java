package marvin.le;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class BezierCurveOverview extends JPanel {
    
    private DefaultMutableTreeNode topLevelTreeNode;
    private DefaultTreeModel treeModel;
    private JTree tree;
    
    
    public BezierCurveOverview() {        
        tree = new JTree();
        tree.setEditable(true);
        BezierOverviewTreeCellRenderer bezierOverviewTreeCellRenderer = new BezierOverviewTreeCellRenderer();
        tree.setCellRenderer(bezierOverviewTreeCellRenderer);
        tree.addTreeSelectionListener(new TreeSelectionListener() {   
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                tree.getLastSelectedPathComponent();
            }
        });
        tree.setCellEditor(new BezierOverviewCellEditor(tree, bezierOverviewTreeCellRenderer));
        add(tree);
        
        
        topLevelTreeNode = new DefaultMutableTreeNode(new RenderComponent(RenderComponent.LETTER, "Untitled Letter"));        
        
        treeModel = new DefaultTreeModel(topLevelTreeNode);
        tree.setModel(treeModel);
        
        validate();
    }
    
    public DefaultMutableTreeNode getTopLevelTreeNode() {
        return topLevelTreeNode;
    }
    
    public JTree getTree() {
        return tree;
    }
    
    public DefaultMutableTreeNode getLastSelectedPathComponent() {
        return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    }
    
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }
}
