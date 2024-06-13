package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@Getter
@Setter
@ToString

public class Url {
    private long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp checkedAt;
    private Integer responseCode;

    public Url(String name, Timestamp timestamp) {
        this.name = name;
        this.createdAt = timestamp;
    }
}
