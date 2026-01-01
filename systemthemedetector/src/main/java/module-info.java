module hu.benzor.systemthemedetector {
    requires static lombok;
    requires org.slf4j;

    exports hu.benzor.systemthemedetector;
    exports hu.benzor.systemthemedetector.api.environment;
    exports hu.benzor.systemthemedetector.api.listener;
    exports hu.benzor.systemthemedetector.api.theme;
}
