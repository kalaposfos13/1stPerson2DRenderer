import java.awt.event.*;

public class RendererInputHandler implements MouseMotionListener, MouseListener {
   private float mouseX = 0.0f;
   private float mouseY = 0.0f;
   private boolean mouseClicked = false;

   @Override
   public void mouseMoved(MouseEvent e) {
      mouseX = (float) e.getX();
      mouseY = (float) e.getY();
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      mouseX = (float) e.getX();
      mouseY = (float) e.getY();
   }

   @Override
   public void mousePressed(MouseEvent e) {
      mouseClicked = true;
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      mouseClicked = false;
   }

   public float getNormalizedMouseX(int width) {
      //return (mouseX / width) * 2 - 1;
      return mouseX;
   }

   public float getNormalizedMouseY(int height) {
      //return (1 - (mouseY / height)) * 2 - 1;
      return height - mouseY;
   }

   public boolean isMouseClicked() {
      return mouseClicked;
   }

   @Override public void mouseClicked(MouseEvent e) {}
   @Override public void mouseEntered(MouseEvent e) {
      mouseClicked = e.getButton() == MouseEvent.BUTTON1;
   }
   @Override public void mouseExited(MouseEvent e) {
      mouseClicked = false;
   }
}
