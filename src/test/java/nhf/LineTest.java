package nhf;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

public class LineTest {

    @Test
    void testColorToHexConversion() {
        // Test case: Color with alpha
        Color color = new Color(255, 128, 64, 192); // RGBA (255, 128, 64, 192)
        String hex = Line.getColorHex(color);
        assertEquals("#c0ff8040", hex, "Hex string should match the expected value.");
    }

    @Test
    void testHexToColorConversion() {
        // Test case: Hex string with alpha
        String hex = "#c0ff8040"; // ARGB (192, 255, 128, 64)
        Color color = Line.getColorFromHex(hex);

        assertEquals(192, color.getAlpha(), "Alpha value should match.");
        assertEquals(255, color.getRed(), "Red value should match.");
        assertEquals(128, color.getGreen(), "Green value should match.");
        assertEquals(64, color.getBlue(), "Blue value should match.");
    }

    @Test
    void testRoundTripConversion() {
        // Test case: Ensure round-trip consistency
        Color originalColor = new Color(34, 56, 78, 90); // RGBA (34, 56, 78, 90)
        Color convertedColor = Line.getColorFromHex(Line.getColorHex(originalColor));

        assertEquals(originalColor.getAlpha(), convertedColor.getAlpha(), "Alpha value should match after round-trip.");
        assertEquals(originalColor.getRed(), convertedColor.getRed(), "Red value should match after round-trip.");
        assertEquals(originalColor.getGreen(), convertedColor.getGreen(), "Green value should match after round-trip.");
        assertEquals(originalColor.getBlue(), convertedColor.getBlue(), "Blue value should match after round-trip.");
    }
}
