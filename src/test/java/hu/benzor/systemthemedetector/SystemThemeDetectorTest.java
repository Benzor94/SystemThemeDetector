package hu.benzor.systemthemedetector;

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.api.theme.Theme.Mode;

import static org.junit.jupiter.api.Assertions.*;

public class SystemThemeDetectorTest {

    @Test
    @Tag("manual")
    void testGetPlatform() {
        Platform platform = SystemThemeDetector.getPlatform();
        System.out.println("Platform: " + platform);
    }

    @Test
    @Tag("manual")
    void testGetDesktopEnvironment() {
        DesktopEnvironment de = SystemThemeDetector.getDesktopEnvironment();
        System.out.println("Desktop environment: " + de);
    }
    
    @Test
    @Tag("manual")
    void testGetCurrentFont() {

        Optional<Font> font = SystemThemeDetector.getCurrentFont();
        assertTrue(font.isPresent());
        Font actualFont = font.get();
        String fontName = actualFont.name();
        String fontSize = actualFont.size();
        System.out.println("Font name: " + fontName);
        System.out.println("Font size: " + fontSize);
    }

    @Test
    @Tag("manual")
    void testGetCurrentMode() {

        Optional<Mode> mode = SystemThemeDetector.getCurrentMode();
        assertTrue(mode.isPresent());
        Mode actualMode = mode.get();
        System.out.println("Mode: " + actualMode);
    }

    @Test
    @Tag("manual")
    void testGetCurrentAccentColor() {

        Optional<AccentColor> color = SystemThemeDetector.getCurrentAccentColor();
        assertTrue(color.isPresent());
        AccentColor actualColor = color.get();
        System.out.println("Accent color: " + actualColor);
    }

    @Test
    @Tag("manual")
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
    @Tag("manual")
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
    @Tag("manual")
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
