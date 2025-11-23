package hu.benzor.systemthemedetector.internal.themedetector.font;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;

public sealed abstract class FontDetector extends ThemeDetector<Font>
    permits LinuxFontDetector, WindowsFontDetector, MacOsFontDetector, FallbackFontDetector {
    
    @Override
    protected Class<Font> type() {
        return Font.class;
    }   

}
