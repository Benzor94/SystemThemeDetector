package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;

public class MacOsAccentColorDetectorTest {

    @Test
    void testAccentColor() {
        MacOsAccentColorDetector detector = new MacOsAccentColorDetector();
        Optional<AccentColor> result = detector.outputLineToThemeMap("0.752941 0.964706 0.678431 Green");

        Assertions.assertEquals(new AccentColor(192, 246, 173), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testDefaultColor() {
        MacOsAccentColorDetector detector = new MacOsAccentColorDetector();
        Optional<AccentColor> result = detector.outputLineToThemeMap("");

        Assertions.assertEquals(new AccentColor(0, 122, 255), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testInvalidLine() {
        MacOsAccentColorDetector detector = new MacOsAccentColorDetector();
        Optional<AccentColor> result1 = detector.outputLineToThemeMap("Sanyi");
        Optional<AccentColor> result2 = detector.outputLineToThemeMap("0.45, 0.95, 0.12 Herp");
        Optional<AccentColor> result3 = detector.outputLineToThemeMap("1.45 3.12 0.98 Supergreen");

        Stream.of(result1, result2, result3).forEach(r -> Assertions.assertTrue(r.isEmpty()));
    }
}
