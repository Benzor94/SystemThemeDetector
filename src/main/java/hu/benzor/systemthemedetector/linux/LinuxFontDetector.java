package hu.benzor.systemthemedetector.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Font;
import hu.benzor.systemthemedetector.environment.DE;
import hu.benzor.systemthemedetector.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.interfaces.FontDetector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinuxFontDetector implements FontDetector {

    private static LinuxFontDetector instance;

    public static LinuxFontDetector getInstance() {
        if (instance == null) {
            var fontDetector = new LinuxFontDetector();
            instance = fontDetector;
        }
        return instance;
    }

    @Override
    public Optional<Font> getSystemFont() {
        Optional<String> font = getFontFromGsettings().or(
            () -> {
                DE de = EnvironmentDetector.getDesktopEnvironment();
                return switch (de) {
                    case KDE -> getFontFallbackKDE();
                    case XFCE -> getFontFallbackXFCE();
                    default -> Optional.empty();
                };
            }
        );
        
        return font.map(Font::new);
    }

    @Override
    public void registerCallback(Consumer<Font> callback) {
        // TODO Auto-generated method stub
        
    }

    private Optional<String> getFontFromGsettings() {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "get",
            "org.gnome.desktop.interface",
            "font-name"
        );
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line == null) {
                    return Optional.empty();
                }
                line.trim();
                if (line.startsWith("'") && line.endsWith("'")) {
                    line = line.substring(1, line.length() - 1);
                }
                String[] splitLine = line.split(",");
                if (splitLine.length >= 1) {
                    return Optional.of(splitLine[0].trim());
                }
                
            }
            process.waitFor();
        } catch (IOException | InterruptedException | NullPointerException e) {

        }
        return Optional.empty();
    }

    private Optional<String> getFontFallbackKDE() {
        // Stub
        return Optional.empty();
    }

    private Optional<String> getFontFallbackXFCE() {
        // Stub
        return Optional.empty();
    }

}
