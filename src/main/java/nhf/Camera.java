package nhf;

import java.awt.event.KeyEvent;
import java.util.Map;

public class Camera {
   private Vector2 position;
   private double rotation;
   private double fov;
   private boolean flipped;
   private boolean lineRender;

   public Camera(Vector2 position, double rotation, double fov) {
      this.position = position;
      this.rotation = rotation;
      this.fov = fov;
      this.flipped = false;
      this.lineRender = false;
   }

   public Camera() {
      this(new Vector2(0, 0), 0, Math.PI / 2);
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

   public void setFlipState(boolean f) {
      flipped = f;
   }
   public boolean get1DRendering() {
      return lineRender;
   }

   public void set1DRendering(boolean f) {
      lineRender = f;
   }

   public void updateCamera(Map<Integer, Boolean> keyStates) {
      double moveSpeed = 10.0;
      double rotateSpeed = 0.1;

      if (keyStates.getOrDefault(KeyEvent.VK_S, false)) {
         position = position.add(new Vector2(
               -moveSpeed * Math.cos(rotation + fov / 2),
               -moveSpeed * Math.sin(rotation + fov / 2)
         ));
      }
      if (keyStates.getOrDefault(KeyEvent.VK_W, false)) {
         position = position.add(new Vector2(
               moveSpeed * Math.cos(rotation + fov / 2),
               moveSpeed * Math.sin(rotation + fov / 2)
         ));
      }

      if (keyStates.getOrDefault(KeyEvent.VK_CONTROL, false)) {
         position = position.add(new Vector2(
               moveSpeed * Math.sin(rotation + fov / 2) * (flipped ? 1 : -1),
               -moveSpeed * Math.cos(rotation + fov / 2) * (flipped ? 1 : -1)
         ));
      }
      if (keyStates.getOrDefault(KeyEvent.VK_SHIFT, false)) {
         position = position.add(new Vector2(
               -moveSpeed * Math.sin(rotation + fov / 2) * (flipped ? 1 : -1),
               moveSpeed * Math.cos(rotation + fov / 2) * (flipped ? 1 : -1)
         ));
      }

      if (keyStates.getOrDefault(KeyEvent.VK_E, false)) {
         rotation -= rotateSpeed * (flipped ? 1 : -1);
      }
      if (keyStates.getOrDefault(KeyEvent.VK_Q, false)) {
         rotation += rotateSpeed * (flipped ? 1 : -1);
      }

      if (keyStates.getOrDefault(KeyEvent.VK_R, false)) {
         fov += 0.1;
      }
      if (keyStates.getOrDefault(KeyEvent.VK_F, false)) {
         fov -= 0.1;
      }
   }
}
