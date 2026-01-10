package hu.benzor.systemthemedetector.internal.detector.appearance;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;

public class WindowsAppearanceDetectorTest {

    @Test
    void testDark() {
        WindowsAppearanceDetector appearanceDetector = new WindowsAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("AppsUseLightTheme    REG_DWORD    0x0");

        Assertions.assertEquals(Appearance.DARK, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testLight() {
        WindowsAppearanceDetector appearanceDetector = new WindowsAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("AppsUseLightTheme    REG_DWORD    0x1");

        Assertions.assertEquals(Appearance.LIGHT, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testInvalidLine() {
        WindowsAppearanceDetector appearanceDetector = new WindowsAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("Hi, I am a potato!");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testInvalidAppearanceNumber() {
        WindowsAppearanceDetector appearanceDetector = new WindowsAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("AppsUseLightTheme    REG_DWORD    0x2");

        Assertions.assertTrue(result.isEmpty());
    }
}
