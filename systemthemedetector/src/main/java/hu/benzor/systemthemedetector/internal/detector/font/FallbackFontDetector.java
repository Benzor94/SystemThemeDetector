package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class FallbackFontDetector extends FontDetector {

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        return new FilteredCommandOutputLineMapper(new ProcessBuilder());
    }

    @Override
    protected Optional<Font> outputLineToThemeMap(String line) {
        return Optional.empty();
    }

}
