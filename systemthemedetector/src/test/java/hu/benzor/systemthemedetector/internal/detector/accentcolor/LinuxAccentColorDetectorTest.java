package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;

public class LinuxAccentColorDetectorTest {

    @Test
    void testAccentColor() {
        LinuxAccentColorDetector accentColorDetector = new LinuxAccentColorDetector();
        Optional<AccentColor> result = accentColorDetector.outputLineToThemeMap("(<(0.22745098173618317, 0.58039218187332153, 0.29019609093666077)>,)");

        AccentColor expected = new AccentColor(58, 148, 74);
        Assertions.assertEquals(expected, result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testInvalidLine() {
        LinuxAccentColorDetector accentColorDetector = new LinuxAccentColorDetector();
        Optional<AccentColor> result = accentColorDetector.outputLineToThemeMap("(<(0.22, 0.58, 0.29, 0.32)>,)");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testOutOfRange() {
        LinuxAccentColorDetector accentColorDetector = new LinuxAccentColorDetector();
        Optional<AccentColor> result = accentColorDetector.outputLineToThemeMap("(<(1.22, 0.58, 0.29)>,)");

        Assertions.assertTrue(result.isEmpty());
    }
}
