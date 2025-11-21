package hu.benzor.systemthemedetector.internal.themedetector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.theme.Theme.Font;

public final class MacOsFontDetector extends FontDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProcessBuilder'");
    }

    @Override
    protected Optional<Font> getThemeFromProcessOutput(String output) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThemeFromProcessOutput'");
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseProcessOutput'");
    }

}
