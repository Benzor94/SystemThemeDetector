module hu.benzor.systemthemedetector {
    requires static lombok;
    requires org.slf4j;

    exports hu.benzor.systemthemedetector;
    exports hu.benzor.systemthemedetector.environment.api;
    exports hu.benzor.systemthemedetector.listeners.api;
    exports hu.benzor.systemthemedetector.theme;
}