package hu.benzor.systemthemedetector;

import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.listener.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemThemeDetector {


    public static Theme.Mode getCurrentMode() {
        // Stub
        return null;
    }

    public static Optional<Theme.Color> getCurrentColor() {
        // Stub
        return Optional.empty();
    }

    public static Optional<Theme.Font> getCurrentFont() {
        // Stub
        return Optional.empty();
    }

    public static ListenerHandle<Theme.Mode> onModeChange(Consumer<Theme.Mode> callback) {
        // Stub
        return null;
    }

    public static ListenerHandle<Theme.Color> onColorChange(Consumer<Theme.Color> callback) {
        // Stub
        return null;
    }

    public static ListenerHandle<Theme.Font> onFontChange(Consumer<Theme.Font> callback) {
        // Stub
        return null;
    }

}
