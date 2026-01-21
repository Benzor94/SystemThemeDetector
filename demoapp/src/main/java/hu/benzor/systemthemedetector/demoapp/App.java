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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
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
        accentRegion.setStyle(
            "-fx-background-color: -color-accent-emphasis;" +
            "-fx-background-radius: 6;"
        );
        
        ToggleButton accentButton = new ToggleButton("Press me!");
        accentButton.selectedProperty().addListener(
            (obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    //setDefaultAccent();
                    accentButton.setText("Release me!");
                } else {
                    // startListeningForAccent();
                    accentButton.setText("Press me!");
                }
            }
        );
        

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, exitButton, accentButton);
        buttons.setPadding(new Insets(20));
        buttons.setAlignment(Pos.CENTER);

        ProgressBar bar = new ProgressBar(0.75);
        bar.setPrefWidth(200);


        VBox root = new VBox(15, helloLabel, accentRegion, bar, buttons);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 400, 250);

        Font font = themeDetector.getFont().orElseThrow();
        AccentColor color = themeDetector.getAccentColor().orElseThrow();

        themeManager = new ThemeManager(scene, color, font);

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
            case LIGHT, NO_PREFERENCE -> new PrimerLight().getUserAgentStylesheet();
        };
        Application.setUserAgentStylesheet(styleSheet);
    }

    public void setAccentColor(AccentColor color) {
        themeManager.applyTheme(color);
    }

    public void setFont(Font font) {
        themeManager.applyTheme(font);
    }
    

    public void setDefaultAccent() {
        themeDetector.stopAllListeners(AccentColor.class);
        scene.getRoot().setStyle(null);
    }


}

