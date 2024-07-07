package hexlet.code.dto;

import java.util.List;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlsPage {
    private List<Url> urls;
    private List<UrlCheck> checks;
}
