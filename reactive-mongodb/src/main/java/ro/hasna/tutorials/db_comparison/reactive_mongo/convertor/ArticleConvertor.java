package ro.hasna.tutorials.db_comparison.reactive_mongo.convertor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.reactive_mongo.domain.Article;
import ro.hasna.tutorials.db_comparison.reactive_mongo.domain.Author;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleConvertor {

    public static ArticleResponse buildArticleResponse(Article article) {
        Author author = article.getAuthor();
        return ArticleResponse.builder()
                .id(article.getId())
                .authorName(author.getName())
                .authorEmail(author.getEmail())
                .content(article.getContent())
                .creationDate(article.getCreationDate())
                .likes(article.getLikes())
                .build();
    }

    public static Author buildAuthor(CreateArticleRequest request) {
        Author author = new Author();
        author.setName(request.authorName());
        author.setEmail(request.authorEmail());
        return author;
    }

    public static Article buildArticle(CreateArticleRequest request, EchoResponse echoResponse) {
        Article article = new Article();
        article.setCreationDate(Instant.now());
        article.setAuthor(buildAuthor(request));
        article.setContent(request.content());
        article.setLikes(echoResponse.likes());
        return article;
    }
}
