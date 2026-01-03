package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.internal.detector.ThemeDetector;

public abstract sealed class AccentColorDetector extends ThemeDetector<AccentColor>
permits
    LinuxAccentColorDetector,
    MacOsAccentColorDetector,
    WindowsAccentColorDetector
{

    @Override
    protected final Class<AccentColor> type() {
        return AccentColor.class;
    }

}
