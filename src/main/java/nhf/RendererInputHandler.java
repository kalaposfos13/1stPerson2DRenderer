package nhf;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class RendererInputHandler implements KeyListener {
   private Map<Integer, Boolean> keyStates = new HashMap<>();
   private Camera camera;

   public RendererInputHandler(Camera camera) {
      this.camera = camera;
      initializeKeyStates();
   }

   private void initializeKeyStates() {
      int[] keys = {KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_Q, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_F,
            KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_SPACE};
      for (int key : keys) {
         keyStates.put(key, false);
      }
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
         camera.setFlipState(!camera.getFlipState());
      }
      keyStates.put(e.getKeyCode(), true);
   }

   @Override
   public void keyReleased(KeyEvent e) {
      keyStates.put(e.getKeyCode(), false);
   }

   @Override
   public void keyTyped(KeyEvent e) {}

   public Map<Integer, Boolean> getKeyStates() {
      return keyStates;
   }
}
