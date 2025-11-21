package hu.benzor.systemthemedetector.internal.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessUtils {

    public static Optional<String> getOutputLineFromProcess(ProcessBuilder processBuilder, String filter) {
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                if (filter == null) {
                    return Optional.ofNullable(reader.readLine());
                }
                return reader.lines().filter(s -> s.contains(filter)).findFirst();
            }

        } catch (IOException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getOutputLineFromProcess(ProcessBuilder processBuilder) {
        return getOutputLineFromProcess(processBuilder, null);
    }

}
