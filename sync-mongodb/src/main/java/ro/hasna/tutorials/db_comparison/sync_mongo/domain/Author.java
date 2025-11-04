package ro.hasna.tutorials.db_comparison.sync_mongo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Author {

    @Id
    private String id;
    private String name;
    private String email;
}
