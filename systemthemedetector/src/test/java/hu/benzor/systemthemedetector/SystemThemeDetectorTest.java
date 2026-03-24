package hu.benzor.systemthemedetector;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.api.listener.ListenerHandle;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.detector.DetectorFactory;
import hu.benzor.systemthemedetector.internal.detector.ThemeDetector;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.LinuxAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.LinuxAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.font.LinuxFontDetector;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SystemThemeDetectorTest {

    @Mock
    private DetectorFactory detectorFactory;

    private SystemThemeDetector systemThemeDetector;

    @BeforeEach
    void setUpMocks() {
        ThemeDetector<AccentColor> accentColorDetector = mock(LinuxAccentColorDetector.class);

        ThemeDetector<Appearance> appearanceDetector = mock(LinuxAppearanceDetector.class);

        ThemeDetector<Font> fontDetector = mock(LinuxFontDetector.class);

        when(detectorFactory.platform()).thenReturn(Platform.LINUX);
        when(detectorFactory.desktop()).thenReturn(DesktopEnvironment.GNOME);
        when(detectorFactory.createAccentColorDetector()).thenReturn(Optional.of(accentColorDetector));
        when(detectorFactory.createAppearanceDetector()).thenReturn(Optional.of(appearanceDetector));
        when(detectorFactory.createFontDetector()).thenReturn(Optional.of(fontDetector));

        when(accentColorDetector.getTheme()).thenReturn(Optional.of(new AccentColor(255, 255, 125)));
        when(appearanceDetector.getTheme()).thenReturn(Optional.of(Appearance.DARK));
        when(fontDetector.getTheme()).thenReturn(Optional.of(new Font("Ubuntu", 11)));

        when(accentColorDetector.registerCallback(any()))
                .thenReturn(new ListenerHandle<>(AccentColor.class, getMockFuture()));
        when(appearanceDetector.registerCallback(any()))
                .thenReturn(new ListenerHandle<>(Appearance.class, getMockFuture()));
        when(fontDetector.registerCallback(any())).thenReturn(new ListenerHandle<>(Font.class, getMockFuture()));

        systemThemeDetector = new SystemThemeDetector(detectorFactory);
    }

    @Test
    void testGetTheme() {
        Optional<AccentColor> accentColor = systemThemeDetector.getAccentColor();
        Optional<Appearance> appearance = systemThemeDetector.getAppearance();
        Optional<Font> font = systemThemeDetector.getFont();

        Assertions.assertEquals(new AccentColor(255, 255, 125), accentColor.orElseThrow(() -> new AssertionError()));
        Assertions.assertEquals(Appearance.DARK, appearance.orElseThrow(() -> new AssertionError()));
        Assertions.assertEquals(new Font("Ubuntu", 11), font.orElseThrow(() -> new AssertionError()));
    }

    @Test
    void testRegisteringCallbacks() {
        ListenerHandle<AccentColor> accentColorHandle = systemThemeDetector.onAccentColorChange((col) -> {
        });
        ListenerHandle<Appearance> appearanceHandle = systemThemeDetector.onAppearanceChange((app) -> {
        });
        ListenerHandle<Font> fontHandle = systemThemeDetector.onFontChange((font) -> {
        });

        Assertions.assertEquals(AccentColor.class, accentColorHandle.type());
        Assertions.assertEquals(Appearance.class, appearanceHandle.type());
        Assertions.assertEquals(Font.class, fontHandle.type());
    }

    @Test
    void testEnvironmentGetting() {
        Platform platform = systemThemeDetector.getPlatform();
        DesktopEnvironment desktop = systemThemeDetector.getDesktop();

        Assertions.assertEquals(Platform.LINUX, platform);
        Assertions.assertEquals(DesktopEnvironment.GNOME, desktop);
    }

    @Test
    void testStopAllListeners() {
        systemThemeDetector.onAccentColorChange((col) -> {
        });
        systemThemeDetector.onAppearanceChange((app) -> {
        });
        systemThemeDetector.onFontChange((font) -> {
        });

        Assertions.assertEquals(3, systemThemeDetector.inspectHandles().size());

        systemThemeDetector.stopAllListeners();

        Assertions.assertTrue(systemThemeDetector.inspectHandles().isEmpty());
    }

    @Test
    void testStopSpecificListeners() {
        systemThemeDetector.onAccentColorChange((col) -> {
        });
        systemThemeDetector.onAppearanceChange((app) -> {
        });
        systemThemeDetector.onFontChange((font) -> {
        });

        Assertions.assertEquals(3, systemThemeDetector.inspectHandles().size());

        systemThemeDetector.stopAllListeners(Font.class);

        Assertions.assertEquals(2, systemThemeDetector.inspectHandles().size());
        Assertions.assertTrue(
                systemThemeDetector.inspectHandles().stream().filter(h -> h.type() == Font.class).toList().isEmpty());
    }

    ScheduledFuture<?> getMockFuture() {
        return mock(ScheduledFuture.class);
    }
}
