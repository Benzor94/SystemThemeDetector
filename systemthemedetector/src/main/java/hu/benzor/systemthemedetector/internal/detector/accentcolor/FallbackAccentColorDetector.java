package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class FallbackAccentColorDetector extends AccentColorDetector {

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        return new FilteredCommandOutputLineMapper(new ProcessBuilder());
    }

    @Override
    protected Optional<AccentColor> outputLineToThemeMap(String line) {
        return Optional.empty();
    }

}
