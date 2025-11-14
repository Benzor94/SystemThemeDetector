package hu.benzor.systemthemedetector.interfaces;

import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Color;

public interface AccentColorDetector {

    Color getAccentColor();

    void registerCallback(Consumer<Color> callback);

}
