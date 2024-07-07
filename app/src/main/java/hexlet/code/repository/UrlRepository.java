package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.sql.Statement;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, createdAt) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, url.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("createdAt");
                var checkedAt = resultSet.getTimestamp("checkedAt");
                var responseCode = resultSet.getInt("responseCode");
                var url = new Url(name, createdAt);
                url.setCheckedAt(checkedAt);
                url.setResponseCode(responseCode);
                url.setId(id);
                result.add(url);
            }
            return result;
        }
    }

    public static List<UrlCheck> getChecksByUrlId(long urlId) throws SQLException {
        String sql = "SELECT * FROM urlChecks WHERE urlId = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var statusCode = resultSet.getInt("statusCode");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("createdAt");
                var check = new UrlCheck();
                check.setStatusCode(statusCode);
                check.setTitle(title);
                check.setH1(h1);
                check.setDescription(description);
                check.setCreatedAt(createdAt);
                check.setId(id);
                result.add(check);
            }
            Collections.reverse(result);
            return result;
        }
    }

    public static List<UrlCheck> getChecks() throws SQLException {
        String sql = "SELECT * FROM urlChecks";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var statusCode = resultSet.getInt("statusCode");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("urlId");
                var createdAt = resultSet.getTimestamp("createdAt");
                var check = new UrlCheck();
                check.setStatusCode(statusCode);
                check.setTitle(title);
                check.setH1(h1);
                check.setDescription(description);
                check.setCreatedAt(createdAt);
                check.setId(id);
                check.setUrlId(urlId);
                result.add(check);
            }
            Collections.reverse(result);
            return result;
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var timestamp = resultSet.getTimestamp("createdAt");
                var url = new Url(name, timestamp);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static void saveCheck(UrlCheck check) throws SQLException {
        String sql = "INSERT INTO urlChecks (urlId, statusCode, title, h1, description, createdAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setLong(2, check.getStatusCode());
            preparedStatement.setString(3, check.getTitle());
            preparedStatement.setString(4, check.getH1());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.setTimestamp(6, check.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static void updateUrl(Url url) throws SQLException {
        String sql = "UPDATE urls SET checkedAt = ?, responseCode = ? WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, url.getCheckedAt());
            preparedStatement.setInt(2, url.getResponseCode());
            preparedStatement.setLong(3, url.getId());
            var i = preparedStatement.executeUpdate();
            System.out.println(i);
        }
    }
}

