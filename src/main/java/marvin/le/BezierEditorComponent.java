package marvin.le;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class BezierEditorComponent extends JPanel {
    private BezierEditorToolbar bezierEditorToolbar;
    private BezierCurveEditor bezierCurveEditor;
    private BezierCurveOverview bezierCurveOverview;

    public BezierEditorComponent() {
        // implement look
        setLayout(new BorderLayout());

        bezierEditorToolbar = new BezierEditorToolbar();
        add(bezierEditorToolbar, BorderLayout.NORTH);

        bezierCurveEditor = new BezierCurveEditor();
        add(bezierCurveEditor, BorderLayout.CENTER);

        bezierCurveOverview = new BezierCurveOverview();
        add(bezierCurveOverview, BorderLayout.WEST);

        validate();

        // implement logic
        final AtomicInteger curveCounter = new AtomicInteger();

        final DefaultMutableTreeNode topLevelTreeNode = bezierCurveOverview.getTopLevelTreeNode();

        JButton tButtonCreate = bezierEditorToolbar.getCreateButton();
        tButtonCreate.addMouseListener(new MouseAdapter() {
            private DefaultMutableTreeNode lastSelected = null;
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
                DefaultMutableTreeNode lastSelectedTreeNode = bezierCurveOverview.getLastSelectedPathComponent();
                
                if (lastSelectedTreeNode == null) {
                    if(lastSelected != null) {
                        lastSelectedTreeNode = lastSelected;
                    } else {
                        return;
                    }
                }
                lastSelected = lastSelectedTreeNode;
                
                Object userObject = lastSelectedTreeNode.getUserObject();
                if (!(userObject instanceof RenderComponent)) {
                    return;
                }
                
                RenderComponent renderComponent = (RenderComponent) userObject;
                String defaultCurveName = "Curve " + curveCounter.get();
                if (renderComponent.id == RenderComponent.LETTER) {
                    DefaultMutableTreeNode insertedNote = new DefaultMutableTreeNode(new RenderComponent(RenderComponent.CURVE, defaultCurveName));
                    topLevelTreeNode.add(insertedNote);
                } /*else if(renderComponent.id == RenderComponent.CURVE) {
                    DefaultMutableTreeNode insertedNote = new DefaultMutableTreeNode(new RenderComponent(RenderComponent.POINT, "Point"));
                    lastSelectedTreeNode.add(insertedNote);
                }*/ else {
                    return;
                }                
                bezierCurveOverview.getTreeModel().reload();
                curveCounter.incrementAndGet();
                
                //add curve to BezierCurveEditor
                bezierCurveEditor.addBezierCurve(defaultCurveName);
            }
        });

        JButton tButtonRemove = bezierEditorToolbar.getRemoveButton();
        //bezierCurveOverview.getTree().getSelectionPath().getLastPathComponent()
        
        
        bezierCurveOverview.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //update the current curve in the MouseInputEventHandler
                TreePath treePath = e.getNewLeadSelectionPath();
                if(treePath == null) {
                    return;
                }
                DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
                
                RenderComponent renderComponent = (RenderComponent) selectedTreeNode.getUserObject();
                
                if(renderComponent.id != RenderComponent.CURVE)
                    return;
                
                String selectedCurveId = renderComponent.content; //the content is equal to the curveID from the BeziereCurve class
                
                bezierCurveEditor.getMouseInputHandler().setCurrentCurve(
                        bezierCurveEditor.getBezierCurve(selectedCurveId)
                        );
            }
        });
    }
}
