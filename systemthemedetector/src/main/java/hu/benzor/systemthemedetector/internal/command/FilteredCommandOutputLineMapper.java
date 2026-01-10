package hu.benzor.systemthemedetector.internal.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilteredCommandOutputLineMapper {

    private final ProcessBuilder processBuilder;
    private final String filter;
    private final boolean passBlankStringOnReadFailure;

    public FilteredCommandOutputLineMapper(ProcessBuilder processBuilder) {
        this(processBuilder, null, false);
    }

    public FilteredCommandOutputLineMapper(ProcessBuilder processBuilder, String filter) {
        this(processBuilder, filter, false);
    }

    public FilteredCommandOutputLineMapper(ProcessBuilder processBuilder, boolean passBlankStringOnReadFailure) {
        this(processBuilder, null, passBlankStringOnReadFailure);
    }

    public <T> Optional<T> mapLine(Function<String, Optional<T>> lineMapper) {
        Optional<String> line = Optional.empty();
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                line = reader.lines().filter(s -> filter == null ? true : s.contains(filter)).findFirst();
            } finally {
                if (!process.waitFor(1, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
            }
        } catch (IOException | IndexOutOfBoundsException e){
            
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        return passBlankStringOnReadFailure ? lineMapper.apply("") : line.flatMap(lineMapper);
    }
}
