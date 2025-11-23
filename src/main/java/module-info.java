module hu.benzor.systemthemedetector {
    requires static lombok;
    requires org.slf4j;

    exports hu.benzor.systemthemedetector;
    exports hu.benzor.systemthemedetector.api.environment;
    exports hu.benzor.systemthemedetector.api.listeners;
    exports hu.benzor.systemthemedetector.api.theme;
}