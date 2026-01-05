module hu.benzor.systemthemedetector.demoapp {
    requires hu.benzor.systemthemedetector;
    requires javafx.controls;
    requires atlantafx.base;

    opens hu.benzor.systemthemedetector.demoapp to javafx.graphics;
}