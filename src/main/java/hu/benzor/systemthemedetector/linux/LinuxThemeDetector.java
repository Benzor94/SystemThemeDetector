package hu.benzor.systemthemedetector.linux;

import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Theme;
import hu.benzor.systemthemedetector.interfaces.ThemeDetector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinuxThemeDetector implements ThemeDetector {

    private static LinuxThemeDetector instance;

    public static LinuxThemeDetector getInstance() {
        if (instance == null) {
            instance = new LinuxThemeDetector();
        }
        return instance;
    }

    @Override
    public Theme getSystemTheme() {
        Optional<String> themeString = LinuxUtils.getLineFromCommand(LinuxUtils.GDBUS_GET_THEME_COMMAND);
        /*
         * The output looks like "(<<uint32 1>>,)" with
         * 0 = no preference
         * 1 = dark
         * 2 = light
         */
        return themeString.map(LinuxThemeDetector::getThemeFromString).orElse(Theme.UNKNOWN);
    }

    @Override
    public void registerCallback(Consumer<Theme> callback) {
        // Stub
        
    }

    private static Theme getThemeFromString(String themeString) {
        if (themeString.length() < 11) {
            return Theme.UNKNOWN;
        }
        String innerThemeString = themeString.substring(3, 11); // the (3, 11) substring is the uint32 1
        String[] parts = innerThemeString.split(" ");
        if (parts.length != 2) {
            return Theme.UNKNOWN;
        }
        try {
            int themeNumber = Integer.parseInt(parts[1]);
            return switch (themeNumber) {
                case 1 -> Theme.DARK;
                case 2 -> Theme.LIGHT;
                default -> Theme.UNKNOWN;
            };
        } catch (NumberFormatException e) {
            return Theme.UNKNOWN;
        }
    }

}
