package nhf;
import com.jogamp.opengl.GL3;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;


public class Camera {
   private Vector2 position;
   private double rotation;
   private double fov;
   private boolean flipped;

   private final Map<Integer, Boolean> keyStates = new HashMap<>();

   public Camera(Vector2 position, double rotation, double fov) {
      this.position = position;
      this.rotation = rotation;
      this.fov = fov;
      this.flipped = false;
      initializeKeyStates();
   }

   public Camera() {
      this(new Vector2(0, 0), 0, Math.PI / 2);
   }

   private void initializeKeyStates() {
      // Initialize key states for relevant keys
      int[] keys = {KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_Q, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_F,
            KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL};
      for (int key : keys) {
         keyStates.put(key, false);
      }
   }

   public Vector2 getPosition() {
      return position;
   }

   public void setPosition(Vector2 position) {
      this.position = position;
   }

   public double getRotation() {
      return rotation;
   }

   public void setRotation(double rotation) {
      this.rotation = rotation;
   }

   public double getFov() {
      return fov;
   }

   public void setFov(double fov) {
      this.fov = fov;
   }
   public boolean getFlipState() {
      return flipped;
   }

   public void updateCamera() {
      double moveSpeed = 10.0;
      double rotateSpeed = 0.1;
      // Move forward/backward (W/S)
      if (keyStates.get(KeyEvent.VK_S)) {
         position = position.add(new Vector2(
               -moveSpeed * Math.cos(rotation + fov / 2),
               -moveSpeed * Math.sin(rotation + fov / 2)
         ));
      }
      if (keyStates.get(KeyEvent.VK_W)) {
         position = position.add(new Vector2(
               moveSpeed * Math.cos(rotation + fov / 2),
               moveSpeed * Math.sin(rotation + fov / 2)
         ));
      }


      // Move up/down (Shift/Ctrl) relative to camera direction
      if (keyStates.get(KeyEvent.VK_CONTROL)) {
         position = position.add(new Vector2(
               moveSpeed * Math.sin(rotation + fov / 2) * (flipped ? 1 : -1),
               -moveSpeed * Math.cos(rotation + fov / 2) * (flipped ? 1 : -1)
         ));
      }
      if (keyStates.get(KeyEvent.VK_SHIFT)) {
         position = position.add(new Vector2(
               -moveSpeed * Math.sin(rotation + fov / 2) * (flipped ? 1 : -1),
               moveSpeed * Math.cos(rotation + fov / 2) * (flipped ? 1 : -1)
         ));
      }

      // Rotate (Q/E)
      if (keyStates.get(KeyEvent.VK_E)) {
         rotation -= rotateSpeed * (flipped ? 1 : -1);
      }
      if (keyStates.get(KeyEvent.VK_Q)) {
         rotation += rotateSpeed * (flipped ? 1 : -1);
      }

      // Adjust FOV (R/F)
      if (keyStates.get(KeyEvent.VK_R)) {
         fov += 0.1;
      }
      if (keyStates.get(KeyEvent.VK_F)) {
         fov -= 0.1;
      }
   }

   public KeyListener getInputHandler() {
      return new KeyListener() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
               flipped = !flipped;
            }
            keyStates.put(e.getKeyCode(), true);
         }

         @Override
         public void keyReleased(KeyEvent e) {
            keyStates.put(e.getKeyCode(), false);
         }

         @Override
         public void keyTyped(KeyEvent e) {}
      };
   }
}
