package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@Table(name = "authors")
public class Author implements Serializable {

    @Id
    private Long id;

    private String name;

    private String email;
}
