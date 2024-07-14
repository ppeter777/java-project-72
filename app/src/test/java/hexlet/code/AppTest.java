package hexlet.code;





import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.javalin.rendering.template.JavalinJte;

import hexlet.code.controller.UrlController;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppTest {

    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException, URISyntaxException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Бесплатно проверяйте сайты на SEO пригодность");
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
            var response = client.get("/url/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void urlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/url/999999");
            assertThat(response.code()).isEqualTo(404);
        });

    }

    @Test
    public void testRequest() throws Exception {

        MockWebServer mockServer = new MockWebServer();
        MockResponse mockResponse = new MockResponse()
                .setBody("bla bla bla");
        mockServer.enqueue(mockResponse);
        mockServer.start();
        String baseUrl = mockServer.url("/").toString();
        RecordedRequest request = mockServer.takeRequest();
        app.get(baseUrl, UrlController::index);

//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get(baseUrl);
//            assertThat(response.body().toString().contains("bla bla bla"));
//        });
//        var response = app.get(baseUrl.toString(), UrlController::show);
//        assertThat(response.code()).isEqualTo(200);
    }
}
