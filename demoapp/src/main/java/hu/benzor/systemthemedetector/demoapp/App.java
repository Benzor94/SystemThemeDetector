package hu.benzor.systemthemedetector.demoapp;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private Scene scene;
    private Region accentRegion;
    private SystemThemeDetector themeDetector = new SystemThemeDetector();
    private ThemeManager themeManager;

    private Consumer<Appearance> onAppearanceChange = appr -> Platform.runLater(() -> setAppearance(appr));
    private Consumer<AccentColor> onAccentColorChange = col -> Platform.runLater(() -> setAccentColor(col));
    private Consumer<Font> onFontChange = f -> Platform.runLater(() -> setFont(f));

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
                    accentButton.setText("Release me!");
                } else {
                    accentButton.setText("Press me!");
                }
            }
        );

        CheckBox checkBox = new CheckBox("Use system theme");
        checkBox.setSelected(true);
        checkBox.selectedProperty().addListener(
            (obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    startListeningForThemeChanges();
                } else {
                    setDefaultTheme();
                }
            }
        );        

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> stage.close());

        HBox controls = new HBox(30, checkBox, exitButton);
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);

        ProgressBar bar = new ProgressBar(0.75);
        bar.setPrefWidth(200);


        VBox root = new VBox(15, helloLabel, accentRegion, bar, accentButton, controls);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 400, 350);

        themeManager = new ThemeManager(scene);

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

    public void setDefaultTheme() {
        themeDetector.stopAllListeners(AccentColor.class);
        themeDetector.stopAllListeners(Font.class);
        themeManager.clearTheme();
    }

    public void startListeningForThemeChanges() {
        themeDetector.onAccentColorChange(onAccentColorChange);
        themeDetector.onFontChange(onFontChange);
    }

}

