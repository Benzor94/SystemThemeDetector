package hu.benzor.systemthemedetector.internal.themedetector.mode;

import hu.benzor.systemthemedetector.api.theme.Theme.Mode;
import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;

public sealed abstract class ModeDetector extends ThemeDetector<Mode>
    permits LinuxModeDetector, WindowsModeDetector, MacOsModeDetector, FallbackModeDetector {

}
