package ro.hasna.tutorials.db_comparison.reactive_mysql.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;

@Data
@Table(name = "articles")
public class Article implements Serializable {

    @Id
    private Long id;

    @Column(value = "creation_date")
    private Instant creationDate;

    @Column(value = "author_id")
    private Long authorId;

    @Transient
    private Author author;

    private String content;

    private Integer likes;
}
