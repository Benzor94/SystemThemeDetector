package hu.benzor.systemthemedetector.internal.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.internal.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class LinuxFontDetector extends FontDetector {

    private final DesktopEnvironment desktop;

    @Override
    protected ProcessBuilder getCommandProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "get",
            getDconfInterfaceSchema(),
            "font-name"
       );
       pb.redirectErrorStream(true);
       return pb;
    }

    @Override
    protected ProcessBuilder getMonitorProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "monitor",
            getDconfInterfaceSchema(),
            "font-name"
        );
        pb.redirectErrorStream(true);
        return pb;
    }

    @Override
    protected Optional<Font> getFontFromCommandOutput(String output) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    protected Optional<Font> getFontFromMonitorOutput(String output) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    private String getDconfInterfaceSchema() {
        return switch (desktop) {
            case GNOME, KDE, XFCE, UNKNOWN -> "org.gnome.desktop.interface";
            case CINNAMON -> "org.cinnamon.desktop.interface";
            case MATE -> "org.mate.interface";
        };
    }

}
