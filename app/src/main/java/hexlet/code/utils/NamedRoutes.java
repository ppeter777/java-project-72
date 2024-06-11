package hexlet.code.utils;

public class NamedRoutes {
    public static String urlsPath() {
        return "/urls";
    }

    public static String mainPagePath() {
        return "/";
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return "/url/" + id;
    }
}
