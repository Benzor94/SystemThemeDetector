package hu.benzor.systemthemedetector.internal.environment;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.internal.environment.propertyreader.PropertyReader;

@ExtendWith(MockitoExtension.class)
public class EnvironmentDetectorTest {

    @Mock
    PropertyReader systemPropertyReader;

    @Mock
    PropertyReader environmentPropertyReader;

    @InjectMocks
    EnvironmentDetector environmentDetector;
    
    @Test
    void testGetPlatformLinux() {

        when(systemPropertyReader.getValue("os.name")).thenReturn(Optional.of("Linux"));

        Platform result = environmentDetector.getPlatform();

        Assertions.assertEquals(Platform.LINUX, result);

    }

    @Test
    void testGetPlatformWindows() {

        when(systemPropertyReader.getValue("os.name")).thenReturn(Optional.of("Windows 11"));

        Platform result = environmentDetector.getPlatform();

        Assertions.assertEquals(Platform.WINDOWS, result);
    }

    @Test
    void testGetPlatformMac() {

        when(systemPropertyReader.getValue("os.name")).thenReturn(Optional.of("MacOs"));

        Platform result = environmentDetector.getPlatform();

        Assertions.assertEquals(Platform.MACOS, result);
    }

    @Test
    void testGetPlatformEmpty() {

        when(systemPropertyReader.getValue("os.name")).thenReturn(Optional.empty());

        Platform result = environmentDetector.getPlatform();

        Assertions.assertEquals(Platform.UNKNOWN, result);
    }

    @Test
    void testGetPlatformGibberish() {

        when(systemPropertyReader.getValue("os.name")).thenReturn(Optional.of("asdaasd"));

        Platform result = environmentDetector.getPlatform();

        Assertions.assertEquals(Platform.UNKNOWN, result);
    }
}
