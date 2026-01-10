package hu.benzor.systemthemedetector.internal.environment;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.internal.environment.propertyreader.PropertyReader;

@ExtendWith(MockitoExtension.class)
public class EnvironmentDetectorTest {

    @Mock
    PropertyReader systemPropertyReader;

    @Mock
    PropertyReader environmentVariableReader;

    EnvironmentDetector environmentDetector;

    @BeforeEach
    void setUp() {
        environmentDetector = new EnvironmentDetector(systemPropertyReader, environmentVariableReader);
    }

    @ParameterizedTest
    @CsvSource(
        {
            "Linux, LINUX",
            "Windows 11, WINDOWS",
            "MacOS, MACOS",
            "asdasd, UNKNOWN",
            "null, UNKNOWN"
        }
    )
    void testGetPlatform(String input, Platform expected) {
        when(systemPropertyReader.getValue(eq("os.name")))
            .thenReturn("null".equals(input) ? Optional.empty() : Optional.of(input));
        
        Assertions.assertEquals(expected, environmentDetector.getPlatform());
    }

    @ParameterizedTest
    @CsvSource(
        {
            "Gnome, GNOME",
            "KDE, KDE",
            "XFCE, XFCE",
            "X-Cinnamon, CINNAMON",
            "MATE, MATE",
            "asdasd, UNKNOWN",
            "null, UNKNOWN"
        }
    )
    void testGetDesktop(String input, DesktopEnvironment expected) {
        when(environmentVariableReader.getValue(eq("XDG_CURRENT_DESKTOP")))
            .thenReturn("null".equals(input) ? Optional.empty() : Optional.of(input));
        Assertions.assertEquals(expected, environmentDetector.getDesktop());
    }
    
}
