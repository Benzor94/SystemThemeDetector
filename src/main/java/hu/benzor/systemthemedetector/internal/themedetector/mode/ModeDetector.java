package hu.benzor.systemthemedetector.internal.themedetector.mode;

import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;
import hu.benzor.systemthemedetector.theme.Theme.Mode;

public sealed abstract class ModeDetector extends ThemeDetector<Mode>
    permits LinuxModeDetector, WindowsModeDetector, MacOsModeDetector, FallbackModeDetector {

}
