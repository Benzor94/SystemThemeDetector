package hu.benzor.systemthemedetector.api.theme;

import java.util.Optional;

import lombok.Getter;

public sealed interface Theme permits Theme.Mode, Theme.AccentColor, Theme.Font {

    @Getter
    public enum Mode implements Theme {
        DARK(1),
        LIGHT(2);

        private int id;

        private Mode(int id) {
            this.id = id;
        }

        public static Optional<Mode> fromId(int id) {
            return switch (id) {
                case 1 -> Optional.of(DARK);
                case 2 -> Optional.of(LIGHT);
                default -> Optional.empty();
            };
        }
    }

    public record AccentColor(int red, int green, int blue) implements Theme {

        public AccentColor {
            verifyColorNumber(red);
            verifyColorNumber(green);
            verifyColorNumber(blue);
        }

        public static AccentColor fromArray(int[] rgbNumbers) {
            if (rgbNumbers.length != 3) {
                throw new IllegalArgumentException("Color array must have length 3.");
            }
            return new AccentColor(rgbNumbers[0], rgbNumbers[1], rgbNumbers[2]);
        }

        private static void verifyColorNumber(int input) {
            if (input < 0 || input > 255) {
                throw new IllegalArgumentException("Color number must be between 0 and 255.");
            }
        }
    }

    public record Font(String name, String size) implements Theme {}

}
