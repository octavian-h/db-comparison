package ro.hasna.tutorials.db_comparison.reactive_mysql.convertor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Article;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleConvertor {

    public static ArticleResponse buildArticleResponse(Article article) {
        Author author = article.getAuthor();
        return ArticleResponse.builder()
                .id(Optional.ofNullable(article.getId())
                        .map(Objects::toString)
                        .orElse(null))
                .authorName(Optional.ofNullable(author).map(Author::getName).orElse(null))
                .authorEmail(Optional.ofNullable(author).map(Author::getEmail).orElse(null))
                .content(article.getContent())
                .creationDate(article.getCreationDate())
                .likes(Optional.ofNullable(article.getLikes())
                        .orElse(0))
                .build();
    }

    public static Article buildArticle(CreateArticleRequest request, Author author, EchoResponse echoResponse) {
        Article article = new Article();
        article.setCreationDate(Instant.now());
        article.setAuthorId(author.getId());
        article.setAuthor(author);
        article.setContent(request.content());
        article.setLikes(echoResponse.likes());
        return article;
    }
}
