package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;

public class WindowsAccentColorDetectorTest {

    @Test
    void testAccentColor() {
        WindowsAccentColorDetector accentColorDetector = new WindowsAccentColorDetector();
        Optional<AccentColor> result = accentColorDetector.outputLineToThemeMap("ColorizationColor    REG_DWORD    0xAB3A944A");

        Assertions.assertEquals(new AccentColor(58, 148, 74), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testAccentColorWithNoAlphaString() {
        WindowsAccentColorDetector accentColorDetector = new WindowsAccentColorDetector();
        Optional<AccentColor> result = accentColorDetector.outputLineToThemeMap("ColorizationColor    REG_DWORD    0x3A944A");

        Assertions.assertEquals(new AccentColor(58, 148, 74), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testInvalidLines() {
        WindowsAccentColorDetector accentColorDetector = new WindowsAccentColorDetector();
        Optional<AccentColor> result1 = accentColorDetector.outputLineToThemeMap("ColorizationColor   0xAB3A944A");
        Optional<AccentColor> result2 = accentColorDetector.outputLineToThemeMap("ColorizationColor    REG_DWORD    zz9pzA944A");
        Optional<AccentColor> result3 = accentColorDetector.outputLineToThemeMap("Hi, I am a potato");

        Stream.of(result1, result2, result3).forEach(r -> Assertions.assertTrue(r.isEmpty()));
    }
}
