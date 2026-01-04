package hu.benzor.systemthemedetector.internal.detector;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.LinuxAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.MacOsAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.WindowsAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.LinuxAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.MacOsAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.WindowsAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.internal.detector.font.MacOsFontDetector;
import hu.benzor.systemthemedetector.internal.detector.font.WindowsFontDetector;
import hu.benzor.systemthemedetector.internal.environment.EnvironmentDetector;

@ExtendWith(MockitoExtension.class)
public class DetectorFactoryTest {

    @Mock
    private EnvironmentDetector environmentDetector;

    @InjectMocks
    private DetectorFactory detectorFactory;

    @ParameterizedTest
    @EnumSource(value = Platform.class, names = {"UNKNOWN"}, mode = EnumSource.Mode.EXCLUDE)
    void testDetectorFactory(Platform platform) {
        setEnvironment(platform, DesktopEnvironment.KDE);

        Platform resultPlatform = detectorFactory.getPlatform();
        DesktopEnvironment resultDesktop = detectorFactory.getDesktop();
        Optional<ThemeDetector<AccentColor>> accentColorDetector = detectorFactory.createAccentColorDetector();
        Optional<ThemeDetector<Appearance>> appearanceDetector = detectorFactory.createAppearanceDetector();
        Optional<ThemeDetector<Font>> fontDetector = detectorFactory.createFontDetector();

        DesktopEnvironment expectedDesktop = platform == Platform.LINUX ? DesktopEnvironment.KDE : DesktopEnvironment.UNKNOWN;
        Class<? extends ThemeDetector<AccentColor>> expectedAccColDtor = switch (platform) {
            case LINUX -> LinuxAccentColorDetector.class;
            case MACOS -> MacOsAccentColorDetector.class;
            case WINDOWS -> WindowsAccentColorDetector.class;
            case UNKNOWN -> null;
        };
        Class<? extends ThemeDetector<Appearance>> expectedAppDtor = switch (platform) {
            case LINUX -> LinuxAppearanceDetector.class;
            case MACOS -> MacOsAppearanceDetector.class;
            case WINDOWS -> WindowsAppearanceDetector.class;
            case UNKNOWN -> null;
        };
        Class<? extends ThemeDetector<Font>> expectedFontDtor = switch (platform) {
            case LINUX -> LinuxFontDetector.class;
            case MACOS -> MacOsFontDetector.class;
            case WINDOWS -> WindowsFontDetector.class;
            case UNKNOWN -> null;
        };

        Class<?> resultAccColDtor = accentColorDetector.map(x -> x.getClass()).orElse(null);
        Class<?> resultAppDtor = appearanceDetector.map(x -> x.getClass()).orElse(null);
        Class<?> resultFontDtor = fontDetector.map(x -> x.getClass()).orElse(null);

        Assertions.assertEquals(platform, resultPlatform);
        Assertions.assertEquals(expectedDesktop, resultDesktop);
        Assertions.assertTrue(expectedAccColDtor.isAssignableFrom(resultAccColDtor));
        Assertions.assertTrue(expectedAppDtor.isAssignableFrom(resultAppDtor));
        Assertions.assertTrue(expectedFontDtor.isAssignableFrom(resultFontDtor));
    }

    private void setEnvironment(Platform platform, DesktopEnvironment desktop) {
        when(environmentDetector.getPlatform()).thenReturn(platform);
        if (platform == Platform.LINUX) {
            when(environmentDetector.getDesktop()).thenReturn(desktop);
        }
    }

}
