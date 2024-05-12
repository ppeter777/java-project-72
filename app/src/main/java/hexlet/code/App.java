package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) throws SQLException {
//        var conn = DriverManager.getConnection("jdbc:h2:mem:project");
//
//        var sql = "CREATE TABLE url (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), created_at VARCHAR(255))";
//        var statement = conn.createStatement();
//        statement.execute(sql);
//        statement.close();
//
//        var sql2 = "INSERT INTO url (name, created_at) VALUES ('tommy', '02-05-2022')";
//        var statement2 = conn.createStatement();
//        statement2.executeUpdate(sql2);
//        statement2.close();
//
//
//
//        var sql3 = "SELECT * FROM url";
//        var statement3 = conn.createStatement();
//        // Здесь вы видите указатель на набор данных в памяти СУБД
//        var resultSet = statement3.executeQuery(sql3);


        var app = getApp()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(getPort());
    }

    public static Javalin getApp() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
        System.out.println(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        return app;
    }
}
