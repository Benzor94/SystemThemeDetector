package hu.benzor.systemthemedetector.theme;

import lombok.Getter;

public sealed interface Theme permits Theme.Mode, Theme.Color, Theme.Font {

    @Getter
    public enum Mode implements Theme {
        APP_DEFAULT(0),
        DARK(1),
        LIGHT(2);

        private int id;

        private Mode(int id) {
            this.id = id;
        }

        public static Mode fromId(int id) {
            return switch (id) {
                case 1 -> DARK;
                case 2 -> LIGHT;
                default -> APP_DEFAULT;
            };
        }
    }

    public record Color(int red, int green, int blue) implements Theme {

        public Color {
            validateInput(red);
            validateInput(green);
            validateInput(blue);
        }

        private void validateInput(int input) {
            if (input < 0 || input > 255) {
                throw new IllegalArgumentException("Color must be between 0 and 255.");
            }
        }
    }

    public record Font(String name, String size) implements Theme {}

}
