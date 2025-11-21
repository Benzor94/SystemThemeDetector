package hu.benzor.systemthemedetector.internal.themedetector.accentcolor;

import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;
import hu.benzor.systemthemedetector.theme.Theme.AccentColor;

public sealed abstract class AccentColorDetector extends ThemeDetector<AccentColor>
    permits LinuxAccentColorDetector, WindowsAccentColorDetector, MacOsAccentColorDetector, FallbackAccentColorDetector {

}
