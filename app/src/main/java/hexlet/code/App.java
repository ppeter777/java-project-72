package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        return app;
    }
}
