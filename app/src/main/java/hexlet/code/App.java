package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.stream.Collectors;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.resolve.ResourceCodeResolver;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) throws SQLException, IOException, URISyntaxException {
        var app = getApp()
                .start(getPort());
        HttpResponse response = Unirest.get("http://localhost:7070/urls")
                .asJson();
    }

    public static Javalin getApp() throws SQLException, IOException, URISyntaxException {
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
        app.get(NamedRoutes.mainPagePath(), UrlController::indexStart);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.checkPath("{id}"), UrlController::check);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.checkPath("{id}"), UrlController::check);
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
