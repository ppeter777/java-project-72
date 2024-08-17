package hexlet.code.controller;

import hexlet.code.dto.MainPage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Objects;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with idd = " + id + " not found"));
        var urlChecks = UrlRepository.getChecksByUrlId(id);
        var page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("url/url.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException, URISyntaxException, IOException {
        var urlString = ctx.formParam("url");
        String urlName;
        try {
            var uri = new URI(urlString);
            var url = uri.toURL();
            var authority = uri.getAuthority();
            var protocol = url.getProtocol();
            urlName = protocol + "://" + authority;
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.mainPagePath());
            return;
        }
        var urlSave = new Url(urlName, new Timestamp(System.currentTimeMillis()));
        if (UrlRepository.findByName(urlName).isEmpty()) {
            UrlRepository.save(urlSave);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
        } else {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "info");
        }
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var checks = UrlRepository.getChecks();
        var page = new UrlsPage(urls, checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void indexStart(Context ctx) throws SQLException {
        var page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void check(Context ctx) throws SQLException, IOException {
        var check = new UrlCheck();
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with idd = " + id + " not found"));
        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());
            var title = doc.title();
            int statusCode = response.getStatus();
            var h1 = doc.selectFirst("h1") == null ? "" : Objects.requireNonNull(doc.selectFirst("h1")).text();
            String description = doc.select("meta[name=description]").first() == null ? ""
                    : doc.select("meta[name=description]").first().attr("content");
            check.setH1(h1);
            check.setStatusCode(statusCode);
            check.setTitle(title);
            check.setDescription(description);
            check.setUrlId(id);
            var createdAt = new Timestamp(System.currentTimeMillis());
            check.setCreatedAt(createdAt);
            UrlRepository.saveCheck(check);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");
        } catch (Exception exception) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flashType", "danger");
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
