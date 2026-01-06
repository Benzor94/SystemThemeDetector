module hu.benzor.systemthemedetector.demoapp {
    requires hu.benzor.systemthemedetector;
    requires javafx.controls;
    requires atlantafx.base;
    requires org.slf4j.simple;

    opens hu.benzor.systemthemedetector.demoapp to javafx.graphics;
}