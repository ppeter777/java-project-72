package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppTest {

    private Javalin app;
    private static MockWebServer testServer;
    private static String testUrl;

    private static String getFileContent(Path testFilePath) throws Exception {
        return Files.readString(testFilePath);
    }

    private static Path getFixturePath(String filename) throws IOException {
        return Paths.get("src/test/resources/fixtures/" + filename).toAbsolutePath().normalize();
    }

    @BeforeAll
    public static void setUpMockServer() throws Exception {
        testServer = new MockWebServer();
        var body = getFileContent(getFixturePath("testPage.html"));
        MockResponse mockResponse = new MockResponse().setResponseCode(HttpStatus.OK.getCode()).setBody(body);
        testServer.enqueue(mockResponse);
        testServer.start();
        testUrl = testServer.url("/test").toString();
    }

    @AfterAll
    public static void shutDown() throws IOException {
        testServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException, URISyntaxException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://mysite.ru:8000/path/", new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.body().string()).contains("mysite.ru");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrls() {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=http://mail.ru");
            client.post("/urls", "url=http://google.com");
            client.post("/urls", "url=http://cnn.com");
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("mail.ru", "google.com", "cnn.com");
        });
    }

    @Test
    void urlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/url/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test void testUrlCheck() throws Exception {
        var url = new Url(testUrl, Timestamp.valueOf(LocalDateTime.now()));
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var savedUrl = UrlRepository.find((long) 1)
                    .orElseThrow(() -> new NotFoundResponse("Url with id = 1 not found"));
            var postUrl = NamedRoutes.checkPath(savedUrl.getId());
            var response = client.post(postUrl);
            assertThat(response.code()).isEqualTo(200);

            var checks = UrlRepository.getChecksByUrlId((long) 1);
            var title = checks.getFirst().getTitle();
            var h1 = checks.getFirst().getH1();

            assertThat(title).isEqualTo("Test");
            assertThat(h1).isEqualTo("Test is successful");
        });
    }
}
