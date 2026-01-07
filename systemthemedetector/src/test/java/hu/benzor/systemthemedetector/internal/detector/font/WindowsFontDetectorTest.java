package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;

public class WindowsFontDetectorTest {

    @Test
    void testWindowsFontDetector() {
        WindowsFontDetector fontDetector = new WindowsFontDetector();
        Optional<Font> result = fontDetector.getTheme();

        Assertions.assertEquals(new Font("Segoe UI", 9), result.orElseThrow(() -> new AssertionError()));
    }
}
