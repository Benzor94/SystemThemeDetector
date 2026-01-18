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
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private Scene scene;
    private Region accentRegion;
    private SystemThemeDetector themeDetector = new SystemThemeDetector();
    private ThemeManager themeManager;

    @Override
    public void start(Stage stage) {
        Label helloLabel = new Label("Hello World");

        accentRegion = new Region();
        accentRegion.setPrefSize(200, 40);
        /*
        ToggleButton accentButton = new ToggleButton("Set default color");
        accentButton.selectedProperty().addListener(
            (obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    setDefaultAccent();
                    accentButton.setText("Set system color");
                } else {
                    startListeningForAccent();
                    accentButton.setText("Set default color");
                }
            }
        );
         */

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> stage.close());

        VBox root = new VBox(15, helloLabel, accentRegion, /*accentButton,*/ exitButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 400, 250);

        Font font = themeDetector.getFont().orElseThrow();
        AccentColor color = themeDetector.getAccentColor().orElseThrow();

        themeManager = new ThemeManager(scene, color, font, accentRegion);

        Consumer<Appearance> onAppearanceChange = appr -> Platform.runLater(() -> setAppearance(appr));
        Consumer<AccentColor> onAccentColorChange = col -> Platform.runLater(() -> themeManager.applyTheme(col));
        Consumer<Font> onFontChange = f -> Platform.runLater(() -> themeManager.applyTheme(f));

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
            case LIGHT, NO_PREFERENCE -> new PrimerLight().getUserAgentStylesheet();
        };
        Application.setUserAgentStylesheet(styleSheet);
    }

    

    public void setDefaultAccent() {
        themeDetector.stopAllListeners(AccentColor.class);
        scene.getRoot().setStyle(null);
    }

    /*
    public void startListeningForAccent() {
        themeDetector.onAccentColorChange(col -> Platform.runLater(() -> setAccentColor(col)));
    }
         */

    private static String toCssRgb(int r, int g, int b) {
        return "rgb(" + r + "," + g + "," + b + ")";
    }

    private static String toCssRgb(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        return toCssRgb(red, green, blue);
    }

}

