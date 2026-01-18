package hu.benzor.systemthemedetector.demoapp;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class ThemeManager {

    private Scene scene;
    private AccentColor color;
    private Font font;
    private String stylesheetUrl;
    private Node rectangle;

    public ThemeManager(Scene scene, AccentColor color, Font font, Node rectangle) {
        this.scene = scene;
        this.color = color;
        this.font = font;
        this.rectangle = rectangle;
    }

    public synchronized void applyTheme(AccentColor color) {
        this.color = color;
        rectangle.setStyle(
            "-fx-background-color: " + toCssRgb(color.red(), color.green(), color.blue()) + ";" +
            "-fx-background-radius: 6;"
        );
        applyThemeInternal();
    }

    public synchronized void applyTheme(Font font) {
        this.font = font;
        applyThemeInternal();
    }

    private void applyThemeInternal() {
        if (stylesheetUrl != null) {
            scene.getStylesheets().remove(stylesheetUrl);
        }
        stylesheetUrl = cssToDataUrl(buildCss());
        scene.getStylesheets().add(stylesheetUrl);
    }

    private String buildCss() {
        AccentVariants derived = deriveBase(color);
        return """
        .root {
            -color-accent-fg: %s;
            -color-accent-emphasis: %s;
            -color-accent-muted: %s;
            -color-accent-subtle: %s;
            -fx-font-family: '%s';
        }
        """.formatted(
            toCssRgb(derived.fg()),
            toCssRgb(derived.emphasis()),
            toCssRgb(derived.muted()),
            toCssRgb(derived.subtle()),
            font.name()
        );
    }

    private static Color accentColorToColor(AccentColor color) {
        return Color.rgb(color.red(), color.green(), color.blue());
    }

    private static String toCssRgb(int r, int g, int b) {
        return "rgb(" + r + "," + g + "," + b + ")";
    }

    private static String toCssRgb(Color color) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return toCssRgb(red, green, blue);
    }

    private static AccentVariants deriveBase(Color base) {
        Color fg = base.deriveColor(0, 0.9, 1.25, 1);
        Color emphasis = base.deriveColor(0, 1.0, 0.85, 1);

        Color muted = base.deriveColor(0, 0.95, 1.0, 0.4);
        Color subtle = base.deriveColor(0, 0.95, 1.0, 0.15);

        return new AccentVariants(fg, emphasis, muted, subtle);
    }

    private static AccentVariants deriveBase(AccentColor base) {
        return deriveBase(accentColorToColor(base));
    }

    private static String cssToDataUrl(String css) {
    return "data:text/css;charset=utf-8," +
           URLEncoder.encode(css, StandardCharsets.UTF_8).replace("+", "%20");
}

    record AccentVariants(Color fg, Color emphasis, Color muted, Color subtle) {}

}
