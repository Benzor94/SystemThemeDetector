package hu.benzor.systemthemedetector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class LinuxFontDetector extends FontDetector {

    private final DesktopEnvironment desktop;

    @Override
    protected ProcessBuilder getCommandProcessBuilder() {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    protected ProcessBuilder getMonitorProcessBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

}
