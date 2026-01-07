package hu.benzor.systemthemedetector.api.theme;

import java.util.Optional;

import lombok.Getter;

public sealed interface Theme permits Theme.Appearance, Theme.AccentColor, Theme.Font {

    public enum Appearance implements Theme {
        NO_PREFERENCE(0),
        DARK(1),
        LIGHT(2);

        @Getter
        private int id;

        private Appearance(int id) {
            this.id = id;
        }

        public static Optional<Appearance> fromId(int id) {
            return switch (id) {
                case 0 -> Optional.of(NO_PREFERENCE);
                case 1 -> Optional.of(DARK);
                case 2 -> Optional.of(LIGHT);
                default -> Optional.empty();
            };
        }
    }

    public record AccentColor(int red, int green, int blue) implements Theme {

        public AccentColor {
            verifyColorNumber(red);
            verifyColorNumber(blue);
            verifyColorNumber(green);
        }

        public static AccentColor fromArray(int[] rgbNumbers) {
            if (rgbNumbers.length != 3) {
                throw new IllegalArgumentException("Color array must have length 3.");
            }
            return new AccentColor(rgbNumbers[0], rgbNumbers[1], rgbNumbers[2]);
        }

        private static void verifyColorNumber(int input) {
            if (input < 0 || input > 255) {
                throw new IllegalArgumentException("Color number must be between 0 and 255 (both inclusive).");
            }
        }
    }

    public record Font(String name, String size) implements Theme {}
}
