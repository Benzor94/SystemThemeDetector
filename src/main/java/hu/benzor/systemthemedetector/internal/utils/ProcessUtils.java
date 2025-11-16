package hu.benzor.systemthemedetector.internal.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessUtils {

    public static Optional<String> getOutputLineFromProcess(ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                String line = reader.readLine();
                return Optional.ofNullable(line);
            }

        } catch (IOException e) {

            return Optional.empty();

        }
    }

}
