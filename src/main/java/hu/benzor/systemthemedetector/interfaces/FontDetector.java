package hu.benzor.systemthemedetector.interfaces;

import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Font;

public interface FontDetector {

    Optional<Font> getSystemFont();

    void registerCallback(Consumer<Font> callback);

}
