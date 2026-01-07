package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;

public class LinuxFontDetectorTest {
    @Test
    void testSingleWordInteger() {

        LinuxFontDetector fontDetector = new LinuxFontDetector(DesktopEnvironment.KDE);
        Optional<Font> result = fontDetector.outputLineToThemeMap("'Ubuntu 10'");

        Assertions.assertEquals(new Font("Ubuntu", 10), result.orElseThrow(() -> new AssertionError()));
        /*
        Optional<Font> resultMultiWordInteger = fontDetector.outputLineToThemeMap("'Fira Sans Medium 11'");
        Optional<Font> resultMultiWordDecimal = fontDetector.outputLineToThemeMap("'Noto Sans 11.5'");
        Optional<Font> resultMultiWordDecimalComma = fontDetector.outputLineToThemeMap("'Adwaita Sans, 9.5'");
         */
    }

    @Test
    void testMultiWordInteger() {
        LinuxFontDetector fontDetector = new LinuxFontDetector(DesktopEnvironment.KDE);
        Optional<Font> result = fontDetector.outputLineToThemeMap("'Fira Sans Medium 11'");

        Assertions.assertEquals(new Font("Fira Sans Medium", 11), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testMultiWordDecimal() {
        LinuxFontDetector fontDetector = new LinuxFontDetector(DesktopEnvironment.KDE);
        Optional<Font> result = fontDetector.outputLineToThemeMap("'Adwaita Sans 11.5'");

        Assertions.assertEquals(new Font("Adwaita Sans", 11.5), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testMultiWordDecimalComma() {
        LinuxFontDetector fontDetector = new LinuxFontDetector(DesktopEnvironment.KDE);
        Optional<Font> result = fontDetector.outputLineToThemeMap("'Noto Sans, 9.5'");

        Assertions.assertEquals(new Font("Noto Sans", 9.5), result.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testNonAsciiLetters() {
        LinuxFontDetector fontDetector = new LinuxFontDetector(DesktopEnvironment.KDE);
        Optional<Font> extendedLatin = fontDetector.outputLineToThemeMap("'Árvíztűrő Tükörfúrógép, 12'");
        Optional<Font> cyrillic = fontDetector.outputLineToThemeMap("'Русский Шрифт 10.5'");
        Optional<Font> emoji = fontDetector.outputLineToThemeMap("'😀🔥Font✨ 18'");
        Optional<Font> arabic = fontDetector.outputLineToThemeMap("'خط عربي, 14.25'");

        Assertions.assertEquals(new Font("Árvíztűrő Tükörfúrógép", 12), extendedLatin.orElseThrow(() -> new AssertionError()));
        Assertions.assertEquals(new Font("Русский Шрифт", 10.5), cyrillic.orElseThrow(() -> new AssertionError()));
        Assertions.assertEquals(new Font("😀🔥Font✨", 18), emoji.orElseThrow(() -> new AssertionError()));
        Assertions.assertEquals(new Font("خط عربي", 14.25), arabic.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testFailure() {
        LinuxFontDetector fontDetector = new LinuxFontDetector(DesktopEnvironment.KDE);
        Optional<Font> resultNoSingleQuotes = fontDetector.outputLineToThemeMap("Noto Sans, 9.5");
        Optional<Font> resultNumbersInName = fontDetector.outputLineToThemeMap("'Fira 9 Sans 13'");
        Optional<Font> resultMultipleNumbers = fontDetector.outputLineToThemeMap("'Droid Sans, 13.5, 12.4'");
        Optional<Font> resultNumberTextReversed = fontDetector.outputLineToThemeMap("'221B Baker Street'");

        for (var font : List.of(resultNoSingleQuotes, resultNumbersInName, resultMultipleNumbers, resultNumberTextReversed)) {
            Assertions.assertTrue(font.isEmpty());
        }
    }
}
