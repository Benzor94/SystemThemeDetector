package hu.benzor.systemthemedetector.interfaces;

import java.util.Optional;

import hu.benzor.systemthemedetector.dto.Font;
import hu.benzor.systemthemedetector.linux.LinuxFontDetector;

public sealed interface FontDetector permits LinuxFontDetector {

    Optional<Font> getSystemFont();

}
