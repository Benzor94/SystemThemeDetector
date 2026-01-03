package hu.benzor.systemthemedetector.internal.command;

import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FilteredCommandOutputLineMapperTest {

    @Mock
    ProcessBuilder processBuilder;

    @Mock
    Process process;

    @Mock
    BufferedReader reader;

    @Test
    void testReadingUnfilteredLine() {
        String[] lines = {"Hello world", "herp derp"};
        setMockCommandOutput(lines);
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(processBuilder);
        Optional<String> result = outputLineMapper.mapLine(s -> Optional.of(s));
        
        Assertions.assertEquals(Optional.of("Hello world"), result);

    }

    @Test
    void testReadingFilteredLine() {
        String[] lines = {"Hello world", "herp derp"};
        setMockCommandOutput(lines);
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(processBuilder, "herp");
        Optional<String> result = outputLineMapper.mapLine(s -> Optional.of(s));
        
        Assertions.assertEquals(Optional.of("herp derp"), result);
    }

    @Test
    void testReadingEmptyOutput() {
        setMockCommandOutput(new String[0]);
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(processBuilder);
        Optional<String> result = outputLineMapper.mapLine(s -> Optional.of(s));
        
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    void testReadingFilteredOutputWithNoMatch() {
        String[] lines = {"Hello world", "herp derp"};
        setMockCommandOutput(lines);
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(processBuilder, "hurr");
        Optional<String> result = outputLineMapper.mapLine(s -> Optional.of(s));
        
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    void testLineMapping() {
        String[] lines = {"Hello world", "My name is John", "itty bitty kitty committee"};
        setMockCommandOutput(lines);
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(processBuilder, "John");
        Optional<Integer> result = outputLineMapper.mapLine(s -> Optional.of(s.split(" ").length));
        
        Assertions.assertEquals(4, result.get());
    }

    @Test
    void testIOExceptionThrown() throws IOException {
        when(processBuilder.start()).thenThrow(IOException.class);
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(processBuilder);
        Optional<String> result = outputLineMapper.mapLine(s -> Optional.of(s));
        
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    void testWithEmptyProcessBuilder() {
        FilteredCommandOutputLineMapper outputLineMapper = new FilteredCommandOutputLineMapper(new ProcessBuilder());
        Optional<String> result = outputLineMapper.mapLine(s -> Optional.of(s));

        Assertions.assertEquals(Optional.empty(), result);
    }

    private void setMockCommandOutput(String[] lines) {
        try {
            when(processBuilder.start()).thenReturn(process);
            when(process.inputReader()).thenReturn(reader);
            when(reader.lines()).thenReturn(Arrays.stream(lines));
        } catch (IOException e) {
            throw new AssertionError(e);
        }        
    }
}
