package hexlet.code;

import io.javalin.Javalin;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) {
        var app = getApp()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(getPort());
    }

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        return app;
    }
}
