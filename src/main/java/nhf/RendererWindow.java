package nhf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


public class RendererWindow extends JFrame {
   public RendererWindow(Scene scene) {
      setTitle("Renderer");
      setSize(800, 600);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      // Top toolbar with a close button
      JToolBar toolbar = new JToolBar();
      JButton closeButton = new JButton("Close");
      closeButton.addActionListener(e -> dispose());
      toolbar.add(closeButton);

      // OpenGL RendererPanel for rendering content
      RendererPanel rendererPanel = new RendererPanel(scene);

      // Add toolbar and panel to the frame
      setLayout(new BorderLayout());
      add(toolbar, BorderLayout.NORTH);
      add(rendererPanel, BorderLayout.CENTER);

      // Add a window listener to stop the timer and exit the program when closing
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            rendererPanel.frameTimer.stop();
         }

         @Override
         public void windowClosed(WindowEvent e) {
            //System.exit(0); // Ensures the program exits fully
         }
      });

      setVisible(true);
   }
   public static void main(String[] args) {
      //new RendererWindow();
      RendererWindow r = new RendererWindow(SceneFileHandler.loadSceneFromXML(new File("src/main/resources/nhf/treeScene.xml")));
   }
}
