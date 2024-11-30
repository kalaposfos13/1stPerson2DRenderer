import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditorInputHandler {
   private final WorkAreaPanel workAreaPanel;
   private Vector2 firstPoint = null;
   private Vector2 mouseDelta = new Vector2();

   public EditorInputHandler(WorkAreaPanel workAreaPanel) {
      this.workAreaPanel = workAreaPanel;
   }

   public void handleMousePress(MouseEvent e) {
      if (e.isControlDown()) {
         mouseDelta = new Vector2(e.getPoint());
      }
      if (SwingUtilities.isLeftMouseButton(e)) {
         if (e.isShiftDown()) {
            if(firstPoint == null) {
               firstPoint = workAreaPanel.pointFromViewToSceneSpace(new Vector2(e.getPoint()));
               return;
            }
            System.out.println("Second point for new line selected.");
            Vector2 secondPoint = workAreaPanel.pointFromViewToSceneSpace(new Vector2(e.getPoint()));
            workAreaPanel.getCurrentScene().addLine(new Line(
                  firstPoint,
                  secondPoint,
                  workAreaPanel.paintColor
            ));
            firstPoint = null;
         } else {
            selectLineAtPoint(e.getPoint());
         }
         workAreaPanel.repaint();
      } else if (SwingUtilities.isRightMouseButton(e)) {
         if (e.isShiftDown()) {
            firstPoint = null;
         }
      } else if (SwingUtilities.isMiddleMouseButton(e)) {
         if (e.isShiftDown()) {
            selectLineAtPoint(e.getPoint());
            if (workAreaPanel.getSelectedLine() != null) {
               workAreaPanel.getCurrentScene().removeLine(workAreaPanel.getSelectedLine());
               workAreaPanel.setSelectedLine(null);
            }
         }
      }
      workAreaPanel.repaint();
   }

   public void handleMouseRelease(MouseEvent e) {
   }

   public void handleMouseDrag(MouseEvent e) {
      if (e.isControlDown()) {
         workAreaPanel.offset = workAreaPanel.offset.add(
               (new Vector2(e.getPoint()).sub(mouseDelta)).mul(1.0 / workAreaPanel.zoomFactor));
         mouseDelta = new Vector2(e.getPoint());
         workAreaPanel.repaint();
      }
   }

   public void handleMouseWheel(MouseWheelEvent e) {
      int notches = e.getWheelRotation();
      double zoomFactor = workAreaPanel.zoomFactor;
      workAreaPanel.zoomFactor = notches < 0 ? zoomFactor * 1.1 : zoomFactor / 1.1;
      workAreaPanel.repaint();
   }


   private void selectLineAtPoint(Point point) {
      Scene scene = workAreaPanel.getCurrentScene();
      workAreaPanel.setSelectedLine(null);
      double minDistance = 10.0 / workAreaPanel.zoomFactor;
      for (Line line : scene.getLines()) {
         double lineDistance = workAreaPanel.pointToLineDistance(
               workAreaPanel.pointFromViewToSceneSpace(new Vector2(point)),
               line);
         if (minDistance > lineDistance) {
            workAreaPanel.setSelectedLine(line);
            minDistance = lineDistance;
         }
      }
   }
}
