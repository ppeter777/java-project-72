package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.HttpStatus;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.AssertionsForClassTypes;
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

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    @BeforeAll
    public static void setUpMockServer() throws Exception {
        testServer = new MockWebServer();
        var body = getFileContent(getFixturePath("testPage.html"));
        MockResponse mockResponse = new MockResponse().setResponseCode(HttpStatus.OK.getCode()).setBody(body);
        testServer.enqueue(mockResponse);
        testServer.start();
        testUrl = removeLastChar(testServer.url("/").toString());
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
    public void addMultipleUrls() {
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

    @Test
    public void testCreateIncorrectUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=qwerty";
            var response = client.post("/urls", requestBody);
            AssertionsForClassTypes.assertThat(response.body().string()).doesNotContain("qwerty");
        });
    }

    @Test
    public void testUrlCheck() {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + testUrl);
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(!response.body().string().contains("Test is successful"));
            var response2 = client.get("/urls/1/checks");
            assertThat(response2.body().string()).contains("Test is successful");
        });
    }

    @Test
    public void testCreateDuplicateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + testUrl;
            var response = client.post("/urls", requestBody);
            AssertionsForClassTypes.assertThat(response.code()).isEqualTo(200);
            AssertionsForClassTypes.assertThat(response.body().string()).contains("Сайты");
            AssertionsForClassTypes.assertThat(client.post("/urls", requestBody).body().string())
                    .contains("Сайты");
        });
    }
}
