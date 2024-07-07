package hexlet.code.controller;

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
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with idd = " + id + " not found"));
        var urlChecks = UrlRepository.getChecksByUrlId(id);
        var page = new UrlPage(url, urlChecks);
        ctx.render("url/url.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException, URISyntaxException, IOException {
        var urlString = ctx.formParam("url");
        var uri = new URI(urlString);
        var url = uri.toURL();
        var authority = uri.getAuthority();
        var protocol = url.getProtocol();
        var urlSave = new Url(protocol + "://" + authority, new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(urlSave);
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var checks = UrlRepository.getChecks();
        var page = new UrlsPage(urls, checks);
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void check(Context ctx) throws SQLException, IOException {
        var check = new UrlCheck();
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with idd = " + id + " not found"));
        HttpResponse<String> response = Unirest.get(url.getName())
                .asString();
        Document doc = Jsoup.parse(response.getBody());
        var title = doc.title();
        int statusCode = response.getStatus();
        var h1 = doc.selectFirst("h1");
        if (h1 == null) {
            check.setH1("");
        } else {
            check.setH1(h1.toString());
        }
        Element meta = doc.select("meta[name=description]").first();
        var description = meta.attr("content");
        check.setStatusCode(statusCode);

        check.setTitle(title);
        check.setDescription(description);
        check.setUrlId(id);
        var createdAt = new Timestamp(System.currentTimeMillis());
        check.setCreatedAt(createdAt);
        UrlRepository.saveCheck(check);

        url.setResponseCode(statusCode);
        url.setCheckedAt(createdAt);
        UrlRepository.updateUrl(url);



        var urlChecks = UrlRepository.getChecksByUrlId(id);
        var page = new UrlPage(url, urlChecks);
        ctx.render("url/url.jte", model("page", page));
    }


}
