module hu.benzor.systemthemedetactor {
    requires static lombok;
    requires org.slf4j;
    requires ch.qos.logback.classic;

    exports hu.benzor.systemthemedetector;
    exports hu.benzor.systemthemedetector.theme;
}