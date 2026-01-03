package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class WindowsFontDetector extends FontDetector {

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commandOutputMapper'");
    }

    @Override
    protected Optional<Font> outputLineToThemeMap(String line) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'outputLineToThemeMap'");
    }

}
