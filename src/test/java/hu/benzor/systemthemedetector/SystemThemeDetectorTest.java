package hu.benzor.systemthemedetector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.theme.Theme.Color;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import hu.benzor.systemthemedetector.theme.Theme.Mode;

public class SystemThemeDetectorTest {
    @Test
    void testGetCurrentFont() {

        Optional<Font> font = SystemThemeDetector.getCurrentFont();
        assertTrue(font.isPresent());
        Font actualFont = font.get();
        String fontName = actualFont.name();
        String fontSize = actualFont.size();
        assertEquals("Ubuntu", fontName);
        assertEquals("10", fontSize);

    }

    @Test
    void testGetCurrentMode() {

        Mode mode = SystemThemeDetector.getCurrentMode();
        assertEquals(Mode.DARK, mode);

    }

    @Test
    void testGetCurrentAccentColor() {

        Optional<Color> color = SystemThemeDetector.getCurrentAccentColor();
        assertTrue(color.isPresent());
        Color actualColor = color.get();
        if (actualColor instanceof Color(int r, int g, int b)) {
            assertEquals(79, r);
            assertEquals(90, g);
            assertEquals(49, b);
        } else {
            assertFalse(true);
        }
    }

    @Test
    void testOnFontChange() throws InterruptedException {

        var handle = SystemThemeDetector.onFontChange(optFont -> {
                if (optFont.isEmpty()) {
                    System.out.println("Font was changed but could not be determined.");
                } else {
                    Font font = optFont.get();
                    System.out.println("Font was changed to " + font + ".");
                }
            }
        );
        Thread.sleep(20_000);
        handle.stop();

    }

    @Test
    void testOnModeChange() throws InterruptedException {

        var handle = SystemThemeDetector.onModeChange(
            mode -> {
                System.out.println("Mode was changed: " + mode);
            }
        );
        Thread.sleep(20_000);
        handle.stop();

    }

    @Test
    void testOnAccentColorChange() throws InterruptedException {
        var handle = SystemThemeDetector.onAccentColorChange(
            optColor -> {
                if (optColor.isEmpty()) {
                    System.out.println("Accent color was changed but could not be determined");
                } else {
                    Color color = optColor.get();
                    System.out.println("Accent color was changed to " + color + ".");
                }
            }
        );
        Thread.sleep(20_000);
        handle.stop();
    }
}
