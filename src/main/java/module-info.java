module hu.benzor.systemthemedetector {
    requires static lombok;
    requires org.slf4j;
    requires ch.qos.logback.classic;

    exports hu.benzor.systemthemedetector;
    exports hu.benzor.systemthemedetector.monitoring.api;
    exports hu.benzor.systemthemedetector.theme;
}