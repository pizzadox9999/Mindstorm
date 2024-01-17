package marvin.le;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

public class LetterEditor {
    private JFrame frame = new JFrame("LETTER_EDITOR - Version 1.0");
    
    public LetterEditor() {
        this(800, 600);
    }
    
    public LetterEditor(int width, int height) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        //compose the ui
        //JMenuBar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JMenuItem fileMenuLoad = new JMenuItem("Load file");
        fileMenu.add(fileMenuLoad);
        
        JMenuItem fileMenuSave = new JMenuItem("Save file");
        fileMenu.add(fileMenuSave);
        
        JMenuItem fileMenuSaveAs = new JMenuItem("Save as file");
        fileMenu.add(fileMenuSaveAs);
        
        //BezierCurveEditor
        BezierEditorComponent bezierEditorComponent = new BezierEditorComponent();
        frame.add(bezierEditorComponent, BorderLayout.CENTER);
        
        frame.validate();
        
        final JPopupMenu popupMenu = new JPopupMenu("testlabel");
        popupMenu.add(new JMenuItem("MenuItem1"));
        
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(frame, e.getX(), e.getY());
                    
                }
                System.out.println("mouse clicked");
                
                System.out.println("popupMenu.isShowing(): " + popupMenu.isShowing());
                System.out.println("popupMenu.isVisible(): " + popupMenu.isVisible());
            }
        });
    }
    
    public static void main(String[] args) {
        new LetterEditor();
    }
}
