package hu.benzor.systemthemedetector.demoapp;

import java.util.Optional;
import java.util.function.Consumer;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import hu.benzor.systemthemedetector.SystemThemeDetector;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private Scene scene;
    private Region accentRegion;
    private SystemThemeDetector themeDetector = new SystemThemeDetector();

    @Override
    public void start(Stage stage) {
        Label helloLabel = new Label("Hello World");

        accentRegion = new Region();
        accentRegion.setPrefSize(200, 40);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> stage.close());

        VBox root = new VBox(15, helloLabel, accentRegion, exitButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 400, 250);

        Optional<Appearance> appearance = themeDetector.getAppearance();
        Optional<AccentColor> accentColor = themeDetector.getAccentColor();
        Optional<Font> font = themeDetector.getFont();

        appearance.ifPresent(this::setAppearance);
        accentColor.ifPresent(this::setAccentColor);
        font.ifPresent(this::setFont);

        Consumer<Appearance> onAppearanceChange = appr -> Platform.runLater(() -> setAppearance(appr));
        Consumer<AccentColor> onAccentColorChange = col -> Platform.runLater(() -> setAccentColor(col));
        Consumer<Font> onFontChange = f -> Platform.runLater(() -> setFont(f));

        themeDetector.onAppearanceChange(onAppearanceChange);
        themeDetector.onAccentColorChange(onAccentColorChange);
        themeDetector.onFontChange(onFontChange);
        
        /*
        String css = """
                -fx-accent: rgb(58, 148, 47);
                -color-accent-emphasis: rgb(58, 148, 47);
                """;
        scene.getRoot().setStyle(css);
         */

        stage.setTitle("System Theme Detector Demo");
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

    public void setAppearance(Appearance appearance) {
        var styleSheet = switch (appearance) {
            case DARK -> new PrimerDark().getUserAgentStylesheet();
            case LIGHT, NO_PREFERENCE -> new PrimerLight().getUserAgentStylesheet();
        };
        Application.setUserAgentStylesheet(styleSheet);
    }

    public void setAccentColor(AccentColor accentColor) {
        String cssColor = toCssRgb(accentColor.red(), accentColor.green(), accentColor.blue());
        accentRegion.setStyle(
            "-fx-background-color: " + cssColor + ";" +
            "-fx-background-radius: 6;"
        );
        AccentVariants variants = deriveBase(Color.rgb(accentColor.red(), accentColor.green(), accentColor.blue()));
        scene.getRoot().setStyle(
            //"-fx-accent: " + cssColor + ";" //+
            "-color-accent-fg: " + toCssRgb(variants.fg()) + ";" +
            "-color-accent-emphasis: " + toCssRgb(variants.emphasis()) + ";" +
            "-color-accent-muted: " + toCssRgb(variants.muted()) + ";" +
            "-color-accent-subtle: " + toCssRgb(variants.sublte()) + ";"
        );
    }

    public void setFont(Font font) {
        String fontFamily = font.name();
        try {
            double fontSize = font.size();
            scene.getRoot().setStyle(
                scene.getRoot().getStyle()
                    + "-fx-font-family: '" + fontFamily + "';"
                    + "-fx-font-size: " + fontSize + "pt;"
            );
        } catch (NumberFormatException e) {

        }        
    }

    private static String toCssRgb(int r, int g, int b) {
        return "rgb(" + r + "," + g + "," + b + ")";
    }

    private static String toCssRgb(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        return toCssRgb(red, green, blue);
    }

    private static AccentVariants deriveBase(Color base) {
        Color fg = base.deriveColor(0, 0.9, 1.25, 1);
        Color emphasis = base.deriveColor(0, 1.0, 0.85, 1);

        Color muted = base.deriveColor(0, 0.95, 1.0, 0.4);
        Color subtle = base.deriveColor(0, 0.95, 1.0, 0.15);

        return new AccentVariants(fg, emphasis, muted, subtle);
    }

}

record AccentVariants(Color fg, Color emphasis, Color muted, Color sublte) {}