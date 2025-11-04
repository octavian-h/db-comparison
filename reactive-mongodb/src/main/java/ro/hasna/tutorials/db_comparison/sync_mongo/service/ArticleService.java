package ro.hasna.tutorials.db_comparison.sync_mongo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;
import ro.hasna.tutorials.db_comparison.sync_mongo.client.EchoClient;
import ro.hasna.tutorials.db_comparison.sync_mongo.domain.Article;
import ro.hasna.tutorials.db_comparison.sync_mongo.domain.Author;
import ro.hasna.tutorials.db_comparison.sync_mongo.repository.ArticleRepository;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EchoClient echoClient;

    private static ArticleResponse buildArticleResponse(Article article) {
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

    private static Author buildAuthor(CreateArticleRequest request) {
        Author author = new Author();
        author.setName(request.authorName());
        author.setEmail(request.authorEmail());
        return author;
    }

    private static Article buildArticle(CreateArticleRequest request, EchoResponse echoResponse) {
        Article article = new Article();
        article.setCreationDate(Instant.now());
        article.setAuthor(buildAuthor(request));
        article.setContent(request.content());
        article.setLikes(echoResponse.likes());
        return article;
    }

    public Flux<ArticleResponse> findArticles(String authorName, PageRequest pageRequest) {
        if (authorName == null) {
            return articleRepository.findAllBy(pageRequest)
                    .map(ArticleService::buildArticleResponse);
        }
        return articleRepository.findAllByAuthorName(authorName, pageRequest)
                .map(ArticleService::buildArticleResponse);
    }

    public Mono<ArticleResponse> createArticle(CreateArticleRequest createArticleRequest) {
        return echoClient.createEcho(EchoRequest.buildExample())
                .map(echoResponse -> buildArticle(createArticleRequest, echoResponse))
                .flatMap(articleRepository::save)
                .map(ArticleService::buildArticleResponse);
    }

    public Mono<ArticleResponse> findArticleById(String articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleService::buildArticleResponse)
                .switchIfEmpty(Mono.error(new ArticleNotFoundException(articleId)))
                .onErrorMap(throwable -> {
                    if (throwable instanceof EmptyResultDataAccessException) {
                        return new ArticleNotFoundException(articleId);
                    }
                    return throwable;
                });
    }

    public Mono<Void> deleteArticle(String articleId) {
        return articleRepository.deleteById(articleId);
    }
}
