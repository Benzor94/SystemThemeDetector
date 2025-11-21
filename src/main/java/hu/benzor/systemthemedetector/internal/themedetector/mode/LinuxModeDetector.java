package hu.benzor.systemthemedetector.internal.themedetector.mode;

import java.util.Optional;

import hu.benzor.systemthemedetector.theme.Theme.Mode;

public final class LinuxModeDetector extends ModeDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProcessBuilder'");
    }

    @Override
    protected Optional<Mode> getThemeFromProcessOutput(String output) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThemeFromProcessOutput'");
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseProcessOutput'");
    }

}
