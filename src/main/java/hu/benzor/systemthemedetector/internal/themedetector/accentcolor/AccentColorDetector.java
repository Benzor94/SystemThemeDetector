package hu.benzor.systemthemedetector.internal.themedetector.accentcolor;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;

public sealed abstract class AccentColorDetector extends ThemeDetector<AccentColor>
    permits LinuxAccentColorDetector, WindowsAccentColorDetector, MacOsAccentColorDetector, FallbackAccentColorDetector {

}
