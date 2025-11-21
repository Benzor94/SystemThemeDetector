package hu.benzor.systemthemedetector.internal.themedetector.font;

import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;
import hu.benzor.systemthemedetector.theme.Theme.Font;

public sealed abstract class FontDetector extends ThemeDetector<Font>
    permits LinuxFontDetector, WindowsFontDetector, MacOsFontDetector, FallbackFontDetector {
    

}
