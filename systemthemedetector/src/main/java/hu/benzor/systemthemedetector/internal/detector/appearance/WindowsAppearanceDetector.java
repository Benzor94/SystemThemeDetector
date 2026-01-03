package hu.benzor.systemthemedetector.internal.detector.appearance;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class WindowsAppearanceDetector extends AppearanceDetector {

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commandOutputMapper'");
    }

    @Override
    protected Optional<Appearance> outputLineToThemeMap(String line) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'outputLineToThemeMap'");
    }

}
