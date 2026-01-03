package hu.benzor.systemthemedetector.internal.detector.appearance;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class FallbackAppearanceDetector extends AppearanceDetector {

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        return new FilteredCommandOutputLineMapper(new ProcessBuilder());
    }

    @Override
    protected Optional<Appearance> outputLineToThemeMap(String line) {
        return Optional.empty();
    }

}
