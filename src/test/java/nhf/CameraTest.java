package nhf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CameraTest {

    @Test
    void testRotationGetterSetter() {
        Camera camera = new Camera();

        // Test initial state (default value)
        double initialRotation = camera.getRotation();
        assertEquals(0.0, initialRotation, "Initial rotation should be 0.0");

        // Set a new rotation value
        double newRotation = 45.0;
        camera.setRotation(newRotation);

        // Verify the getter retrieves the updated value
        assertEquals(newRotation, camera.getRotation(), "Rotation should match the value set");
    }
}