package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain.Article;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Article> findAllBy(Pageable pageable) {
        return jdbcTemplate.query("SELECT articles.id, articles.creation_date, articles.author_id, articles.content, authors.name, authors.email " +
                        "FROM articles " +
                        "INNER JOIN authors ON articles.author_id = authors.id " +
                        "LIMIT ? " +
                        "OFFSET ?",
                (rs, rowNum) -> buildArticle(rs),
                pageable.getPageSize(), pageable.getPageNumber()
        );
    }

    public List<Article> findAllByAuthorName(String authorName, Pageable pageable) {

        return jdbcTemplate.query("SELECT articles.id, articles.creation_date, articles.author_id, articles.content, articles.likes, authors.name, authors.email " +
                        "FROM articles " +
                        "INNER JOIN authors ON articles.author_id = authors.id " +
                        "WHERE authors.name = ? " +
                        "LIMIT ? " +
                        "OFFSET ?",
                (rs, rowNum) -> buildArticle(rs),
                authorName, pageable.getPageSize(), pageable.getPageNumber()
        );
    }

    public Optional<Article> findById(long articleId) {
        return jdbcTemplate.query("SELECT articles.id, articles.creation_date, articles.author_id, articles.content, articles.likes, authors.name, authors.email " +
                                "FROM articles " +
                                "INNER JOIN authors ON articles.author_id = authors.id " +
                                "WHERE articles.id = ?",
                        (rs, rowNum) -> buildArticle(rs),
                        articleId
                )
                .stream()
                .findAny();
    }

    public Article save(Article article) {
        jdbcTemplate.update("INSERT INTO articles (creation_date, author_id, content, likes) VALUES (?, ?, ?, ?)",
                java.sql.Timestamp.from(article.getCreationDate()),
                article.getAuthorId(),
                article.getContent(),
                article.getLikes()
        );
        Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        article.setId(generatedId);
        return article;
    }

    public void deleteById(long articleId) {
        jdbcTemplate.update("DELETE FROM articles WHERE id = ?", articleId);
    }

    private Author buildAuthor(Long authorId, ResultSet row) throws SQLException {
        Author author = new Author();
        author.setId(authorId);
        author.setName(row.getString("name"));
        author.setEmail(row.getString("email"));
        return author;
    }

    private Article buildArticle(ResultSet row) throws SQLException {
        Article article = new Article();
        article.setId(row.getLong("id"));
        article.setCreationDate(row.getTimestamp("creation_date")
                .toInstant());
        Long authorId = row.getLong("author_id");
        article.setAuthorId(authorId);
        article.setAuthor(buildAuthor(authorId, row));
        article.setContent(row.getString("content"));
        return article;
    }
}
