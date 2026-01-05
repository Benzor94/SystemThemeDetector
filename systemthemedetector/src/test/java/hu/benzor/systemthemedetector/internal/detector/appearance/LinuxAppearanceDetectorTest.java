package hu.benzor.systemthemedetector.internal.detector.appearance;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;

public class LinuxAppearanceDetectorTest {

    @Test
    void testDark() {
        LinuxAppearanceDetector appearanceDetector = new LinuxAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("(<uint32 1>,)");

        Assertions.assertEquals(Appearance.DARK, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testLight() {
        LinuxAppearanceDetector appearanceDetector = new LinuxAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("(<uint32 2>,)");

        Assertions.assertEquals(Appearance.LIGHT, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testAppPrefers() {
        LinuxAppearanceDetector appearanceDetector = new LinuxAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("(<uint32 0>,)");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testInvalidLine() {
        LinuxAppearanceDetector appearanceDetector = new LinuxAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("Hi, I am a potato");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testInvalidAppearanceNumber() {
        LinuxAppearanceDetector appearanceDetector = new LinuxAppearanceDetector();
        Optional<Appearance> result = appearanceDetector.outputLineToThemeMap("(<uint32 9>,)");

        Assertions.assertTrue(result.isEmpty());
    }    
}
