package hu.benzor.systemthemedetector;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import hu.benzor.systemthemedetector.theme.Theme.Mode;

import static org.junit.jupiter.api.Assertions.*;

public class SystemThemeDetectorTest {
    @Test
    void testGetCurrentFont() {

        Optional<Font> font = SystemThemeDetector.getCurrentFont();
        assertTrue(font.isPresent());
        Font actualFont = font.get();
        String fontName = actualFont.name();
        String fontSize = actualFont.size();
        System.out.println("Font name: " + fontName);
        System.out.println("Font size: " + fontSize);
        //assertEquals("Ubuntu", fontName);
        //assertEquals("10", fontSize);

    }

    @Test
    void testGetCurrentMode() {

        Optional<Mode> mode = SystemThemeDetector.getCurrentMode();
        assertTrue(mode.isPresent());
        Mode actualMode = mode.get();
        System.out.println("Mode: " + actualMode);
        //assertEquals(Mode.DARK, actualMode);

    }

    @Test
    void testGetCurrentAccentColor() {

        Optional<AccentColor> color = SystemThemeDetector.getCurrentAccentColor();
        assertTrue(color.isPresent());
        AccentColor actualColor = color.get();
        int r = actualColor.red();
        int g = actualColor.green();
        int b = actualColor.blue();
        System.out.println("Accent color: " + actualColor);
//        assertEquals(80, r);
//        assertEquals(91, g);
//        assertEquals(49, b);
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
                    AccentColor color = optColor.get();
                    System.out.println("Accent color was changed to " + color + ".");
                }
            }
        );
        Thread.sleep(20_000);
        handle.stop();
    }
}
