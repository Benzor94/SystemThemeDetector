package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class MacOsAccentColorDetector extends AccentColorDetector {

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commandOutputMapper'");
    }

    @Override
    protected Optional<AccentColor> outputLineToThemeMap(String line) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'outputLineToThemeMap'");
    }

}
