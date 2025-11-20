package hu.benzor.systemthemedetector.theme;

import lombok.Getter;

public sealed interface Theme permits Theme.Mode, Theme.AccentColor, Theme.Font {

    default Theme getAbsent() {
        return switch(this) {
            case Font f -> new Font.Absent();
            case AccentColor c -> new AccentColor.Absent();
            case Mode m -> Mode.APP_DEFAULT;
        };
    }

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

    public sealed interface AccentColor extends Theme permits AccentColor.Present, AccentColor.Absent {

        public record Present(int red, int green, int blue) implements AccentColor {

            public Present {
                verifyInput(red);
                verifyInput(green);
                verifyInput(blue);
            }

            public static Present fromArray(int[] rgbNumbers) {
                if (rgbNumbers.length != 3) {
                    throw new IllegalArgumentException("Length of the input array must be 3.");
                }
                return new Present(rgbNumbers[0], rgbNumbers[1], rgbNumbers[2]);
            }

            private static void verifyInput(int input) {
                if (input < 0 || input > 255) {
                    throw new IllegalArgumentException("Color value must be between 0 and 255.");
                }
            }
        }

        public record Absent() implements AccentColor {}

    }

    public sealed interface Font extends Theme permits Font.Present, Font.Absent {

        public record Present(String name, String size) implements Font {}

        public record Absent() implements Font {}

    }

}
