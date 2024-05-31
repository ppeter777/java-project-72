package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.resolve.ResourceCodeResolver;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp()
                .get("/", ctx -> ctx.result("Hello World " + getDBUrl()))
                .start(getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDBUrl());
        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }


        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });
        app.get("/test", ctx -> ctx.render("index.jte"));
        return app;
    }

    public static String getDBUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
}
