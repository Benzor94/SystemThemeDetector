package hu.benzor.systemthemedetector.internal.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FilteredCommandOutputLineMapper {

    private final List<String> command;
    private String filter;

    public FilteredCommandOutputLineMapper(String... command) {
        this.command = Arrays.asList(command);
    }

    public FilteredCommandOutputLineMapper filter(String filter) {
        this.filter = filter;
        return this;
    }

    public <T> Optional<T> mapLine(Function<String, Optional<T>> lineMapper) {
        ProcessBuilder pb = new ProcessBuilder(command);
        Optional<String> line = Optional.empty();
        try {
            Process process = pb.start();
            try (BufferedReader reader = process.inputReader()) {
                line = reader.lines().filter(s -> filter == null ? true : s.contains(filter)).findFirst();
            }
        } catch (IOException e){
            return Optional.empty();
        }
        return line.flatMap(lineMapper);
    }
}
