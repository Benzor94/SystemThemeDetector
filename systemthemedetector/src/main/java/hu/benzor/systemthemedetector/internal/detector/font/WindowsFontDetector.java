package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;
import java.util.function.Function;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class WindowsFontDetector extends FontDetector {

    private static final String FONT_NAME = "Segoe UI";
    private static final double PT_SIZE = 9.0;

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        return new FilteredCommandOutputLineMapper(null) {
            @Override
            public <T> Optional<T> mapLine(Function<String, Optional<T>> lineMapper) {
                return lineMapper.apply(null);
            }
        };
    }

    @Override
    protected Optional<Font> outputLineToThemeMap(String line) {
        return Optional.of(new Font(FONT_NAME, PT_SIZE));
    }

}
