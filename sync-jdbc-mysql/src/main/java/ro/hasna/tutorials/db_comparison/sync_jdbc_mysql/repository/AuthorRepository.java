package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    private static Author buildAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setName(rs.getString("name"));
        author.setEmail(rs.getString("email"));
        return author;
    }

    public Optional<Author> findByEmail(String authorEmail) {
        return jdbcTemplate.query("SELECT id, name, email " +
                        "FROM authors " +
                        "WHERE email = ?",
                (rs, rowNum) -> buildAuthor(rs),
                authorEmail
        ).stream().findAny();
    }

    public Page<Author> findByName(String name, PageRequest pageRequest) {
        var authors = jdbcTemplate.query("SELECT id, name, email " +
                        "FROM authors " +
                        "WHERE name = ? " +
                        "LIMIT ? OFFSET ?",
                (rs, rowNum) -> buildAuthor(rs),
                name,
                pageRequest.getPageSize(),
                pageRequest.getOffset()
        );
        var total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authors WHERE name = ?",
                Long.class,
                name
        );
        return new PageImpl<>(authors, pageRequest, total != null ? total : 0);
    }

    public Author save(Author author) {
        jdbcTemplate.update("INSERT INTO authors (name, email) VALUES (?, ?)",
                author.getName(),
                author.getEmail()
        );
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        author.setId(id);
        return author;
    }
}
