package ro.hasna.tutorials.db_comparison.reactive_mongo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "articles")
@Data
public class Article {

    @Id
    private String id;
    private Instant creationDate;
    private Author author;
    private String content;
    private int likes;
}
