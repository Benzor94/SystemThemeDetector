package hu.benzor.systemthemedetector.internal.themedetector.accentcolor;

import java.util.Optional;

import hu.benzor.systemthemedetector.theme.Theme.AccentColor;

public final class LinuxAccentColorDetector extends AccentColorDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProcessBuilder'");
    }

    @Override
    protected Optional<AccentColor> getThemeFromProcessOutput(String output) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThemeFromProcessOutput'");
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseProcessOutput'");
    }

}
