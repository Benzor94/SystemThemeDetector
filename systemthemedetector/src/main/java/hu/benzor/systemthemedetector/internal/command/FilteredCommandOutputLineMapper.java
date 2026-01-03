package hu.benzor.systemthemedetector.internal.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilteredCommandOutputLineMapper {

    private final ProcessBuilder processBuilder;
    private String filter;

    public FilteredCommandOutputLineMapper filter(String filter) {
        this.filter = filter;
        return this;
    }

    public <T> Optional<T> mapLine(Function<String, Optional<T>> lineMapper) {
        Optional<String> line = Optional.empty();
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                line = reader.lines().filter(s -> filter == null ? true : s.contains(filter)).findFirst();
            } finally {
                process.destroy();
            }
        } catch (IOException e){
            return Optional.empty();
        }
        return line.flatMap(lineMapper);
    }
}
