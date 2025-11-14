package hu.benzor.systemthemedetector;

import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Color;
import hu.benzor.systemthemedetector.dto.Font;
import hu.benzor.systemthemedetector.dto.Theme;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemThemeDetector {


    public static Theme getCurrentTheme() {
        // Stub
        return Theme.UNKNOWN;
    }

    public static Color getCurrentColor() {
        // Stub
        return new Color(0, 0, 0);
    }

    public static Font getCurrentFont() {
        // Stub
        return new Font("Ubuntu");
    }

    public static void onThemeChange(Consumer<Theme> callback) {
        // Stub
    }

    public static void onColorChange(Consumer<Color> callback) {
        // Stub
    }

    public static void onFontChange(Consumer<Font> callback) {
        // Stub
    }

}
