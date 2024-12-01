package nhf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class WorkAreaPanel extends JPanel {
   public Scene currentScene;
   private Line selectedLine = null;
   public double zoomFactor = 1.0;
   public Vector2 offset = new Vector2(0,0);
   public Color paintColor = Color.BLACK;
   private EditorInputHandler inputHandler;

   public WorkAreaPanel(Scene scene) {
      this.currentScene = scene;
      this.inputHandler = new EditorInputHandler(this);
      setBackground(Color.WHITE);

      addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            inputHandler.handleMousePress(e);
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            inputHandler.handleMouseRelease(e);
         }
      });

      addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            inputHandler.handleMouseDrag(e);
         }
      });

      addMouseWheelListener(inputHandler::handleMouseWheel);
   }

   // Getters and setters for zoomFactor, offsetX, offsetY, selectedLine, and currentScene

   public Scene getCurrentScene() { return currentScene; }
   public void setSelectedLine(Line line) { this.selectedLine = line; }
   public Line getSelectedLine() { return selectedLine; }
   public Vector2 pointFromViewToSceneSpace(Vector2 p) {
      return new Vector2(-offset.x + (p.x) / zoomFactor,
            -offset.y + (p.y) / zoomFactor);
   }
   public Vector2 pointFromSceneToViewSpace(Vector2 p) {
      return new Vector2(offset.x + (p.x) / zoomFactor, offset.y + (p.y) / zoomFactor);
   }

   public static double pointToLineDistance(double x1, double y1, double x2, double y2, double x3, double y3) {
      double dx = x2 - x3;
      double dy = y2 - y3;
      double denominator = dx * dx + dy * dy;

      // if the line is a point
      if (dx == 0 && dy == 0) {
         return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      }

      // Calculate the projection factor of point (x1, y1) onto the line segment
      double t = ((x1 - x3) * dx + (y1 - y3) * dy) / denominator;

      // Clamp t to the range [0, 1] to find the nearest point on the line segment
      t = Math.max(0, Math.min(1, t));

      // Find the closest point on the line segment to (x1, y1)
      double closestX = x3 + t * dx;
      double closestY = y3 + t * dy;

      // Return the distance from the point to the closest point on the line segment
      return Math.sqrt((closestX - x1) * (closestX - x1) + (closestY - y1) * (closestY - y1));
   }
   public double pointToLineDistance(Vector2 p, Line l) {
      return pointToLineDistance(p.x, p.y, l.p1.x, l.p1.y, l.p2.x, l.p2.y);
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      for (Line line : currentScene.getLines()) {
         g2d.setStroke(line == selectedLine ? new BasicStroke(4) : new BasicStroke(2));
         g2d.setColor(line.color);
         g2d.drawLine((int) ((line.p1.x + offset.x) * zoomFactor),
                      (int) ((line.p1.y + offset.y) * zoomFactor),
                      (int) ((line.p2.x + offset.x) * zoomFactor),
                      (int) ((line.p2.y + offset.y) * zoomFactor));
      }
   }
}
