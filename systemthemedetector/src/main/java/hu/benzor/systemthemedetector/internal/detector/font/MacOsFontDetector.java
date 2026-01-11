package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.command.CommandOutputLineMapper;

public final class MacOsFontDetector extends FontDetector {

    @Override
    protected CommandOutputLineMapper commandOutputMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commandOutputMapper'");
    }

    @Override
    protected Optional<Font> outputLineToThemeMap(String line) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'outputLineToThemeMap'");
    }

}
