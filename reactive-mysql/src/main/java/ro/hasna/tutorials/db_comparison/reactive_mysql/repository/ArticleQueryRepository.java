package ro.hasna.tutorials.db_comparison.reactive_mysql.repository;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Article;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;

import java.time.Instant;

@RequiredArgsConstructor
@Repository
public class ArticleQueryRepository {
    private final DatabaseClient databaseClient;

    private static Article buildArticle(Row row) {
        Article article = new Article();
        article.setId(row.get("article_id", Long.class));
        article.setCreationDate(row.get("creation_date", Instant.class));
        article.setAuthorId(row.get("author_id", Long.class));
        article.setContent(row.get("content", String.class));
        article.setLikes(row.get("likes", Integer.class));

        Author author = new Author();
        author.setId(row.get("author_id", Long.class));
        author.setName(row.get("name", String.class));
        author.setEmail(row.get("email", String.class));

        article.setAuthor(author);
        return article;
    }

    public Flux<Article> findAll(Pageable pageable) {
        String sql = """
                SELECT a.id AS article_id, a.creation_date, a.author_id, a.content, a.likes, au.id AS author_id, au.name, au.email 
                FROM articles a 
                JOIN authors au ON a.author_id = au.id 
                LIMIT :limit OFFSET :offset
                """;

        return databaseClient.sql(sql)
                .bind("limit", pageable.getPageSize())
                .bind("offset", pageable.getOffset())
                .map((row, metadata) -> buildArticle(row))
                .all();
    }

    public Flux<Article> findAllByAuthorName(String authorName, Pageable pageable) {
        String sql = """
                SELECT a.id AS article_id, a.creation_date, a.author_id, a.content, a.likes, au.id AS author_id, au.name, au.email 
                FROM articles a 
                JOIN authors au ON a.author_id = au.id 
                WHERE au.name = :authorName 
                LIMIT :limit OFFSET :offset
                """;

        return databaseClient.sql(sql)
                .bind("authorName", authorName)
                .bind("limit", pageable.getPageSize())
                .bind("offset", pageable.getOffset())
                .map((row, metadata) -> buildArticle(row))
                .all();
    }
}
