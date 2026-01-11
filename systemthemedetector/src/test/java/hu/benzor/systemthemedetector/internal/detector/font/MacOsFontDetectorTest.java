package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;

public class MacOsFontDetectorTest {

    @Test
    void testMacosFontDetector() {
        MacOsFontDetector detector = new MacOsFontDetector();
        Optional<Font> result = detector.getTheme();

        Assertions.assertEquals(new Font(".SF NS Text", 13), result.orElseThrow(() -> new AssertionError()));
    }
}
