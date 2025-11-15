package hu.benzor.systemthemedetector.linux;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.dto.Font;
import hu.benzor.systemthemedetector.interfaces.FontDetector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinuxFontDetector implements FontDetector {

    private static LinuxFontDetector instance;

    public static LinuxFontDetector getInstance() {
        if (instance == null) {
            instance = new LinuxFontDetector();
        }
        return instance;
    }

    @Override
    public Optional<Font> getSystemFont() {
        /*
         * We try to get the system font from the org.gnome.desktop.interface schema.
         * Most desktop environments will set the font-name key here when the font is changed from the GUI settings tool
         * for Gnome / GTK compatibility.
         */
        Optional<String> font = LinuxUtils.getLineFromCommand(LinuxUtils.GSETTINGS_GET_FONT_COMMAND);
        return font.map(String::trim).flatMap(LinuxFontDetector::getFontFromString);
    }

    private static Optional<Font> getFontFromString(String fontString) {

        if (fontString.contains(",")) {
            /*
             * It seems that if the font is set from KDE, then the name might be separated from the number by a comma, like
             * "Noto Sans, 10", otherwise it is of the format "Noto Sans 10".
             */
            fontString = fontString.replace(",", "");
        }
        String finalFontString = new String(fontString);
        OptionalInt indexOfFirstNumber = IntStream.range(0, fontString.length()).filter(
            i -> Character.isDigit(finalFontString.charAt(i))
        ).findFirst();
        if (!indexOfFirstNumber.isPresent()) {
            return Optional.empty();
        }
        int index = indexOfFirstNumber.getAsInt();
        String fontName = finalFontString.substring(0, index).trim();
        String fontSize = finalFontString.substring(index, finalFontString.length());
        return Optional.of(new Font(fontName, fontSize));
    }

}
