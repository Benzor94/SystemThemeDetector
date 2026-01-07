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
            case LIGHT -> new PrimerLight().getUserAgentStylesheet();
        };
        Application.setUserAgentStylesheet(styleSheet);
    }

    public void setAccentColor(AccentColor accentColor) {
        String cssColor = toCssRgb(accentColor.red(), accentColor.green(), accentColor.blue());

        scene.getRoot().setStyle(
            "-fx-accent: " + cssColor + ";"
        );
        accentRegion.setStyle(
            "-fx-background-color: " + cssColor + ";" +
            "-fx-background-radius: 6;"
        );
    }

    public void setFont(Font font) {
        String fontFamily = font.name();
        try {
            double fontSize = Double.parseDouble(font.size());
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
}