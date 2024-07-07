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

    public static String checkPath(String id) {
        return "/urls/" + id  + "/checks";
    }

    public static String checkPath(Long id) {
        return checkPath(String.valueOf(id));
    }
}
