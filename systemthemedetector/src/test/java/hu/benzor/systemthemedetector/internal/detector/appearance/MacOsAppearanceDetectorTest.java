package hu.benzor.systemthemedetector.internal.detector.appearance;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;

public class MacOsAppearanceDetectorTest {

    @Test
    void testDark() {
        MacOsAppearanceDetector detector = new MacOsAppearanceDetector();
        Optional<Appearance> result = detector.outputLineToThemeMap("Dark");

        Assertions.assertEquals(Appearance.DARK, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testLight() {
        MacOsAppearanceDetector detector = new MacOsAppearanceDetector();
        Optional<Appearance> result = detector.outputLineToThemeMap("");

        Assertions.assertEquals(Appearance.LIGHT, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testInvalidLine() {
        MacOsAppearanceDetector detector = new MacOsAppearanceDetector();
        Optional<Appearance> result = detector.outputLineToThemeMap("I am a potato");

        Assertions.assertTrue(result.isEmpty());
    }
}
