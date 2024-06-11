package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString

public class Url {
    private long id;
    private String name;
    private Timestamp created_at;
    private Timestamp checked_at;
    private Integer responseCode;

    public Url(String name, Timestamp timestamp) {
        this.name = name;
        this.created_at = timestamp;
    }

}
