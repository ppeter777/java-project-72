package hexlet.code.dto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlsPage extends BasePage {
    private List<Url> urls;
//    private List<UrlCheck> checks;
    private Optional<Map<Long, UrlCheck>> lastChecks;

    public static Optional<UrlCheck> getLastCheck(long id) {
        try {
            return UrlRepository.getLastCheck(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
