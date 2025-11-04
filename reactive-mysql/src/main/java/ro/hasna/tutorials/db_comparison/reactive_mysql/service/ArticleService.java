package ro.hasna.tutorials.db_comparison.reactive_mysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ro.hasna.tutorials.db_comparison.reactive_mysql.client.EchoClient;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Article;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;
import ro.hasna.tutorials.db_comparison.reactive_mysql.repository.ArticleRepository;
import ro.hasna.tutorials.db_comparison.reactive_mysql.repository.AuthorRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AuthorRepository authorRepository;
    private final EchoClient echoClient;

    private static ArticleResponse buildArticleResponse(Article article) {
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

    private static Author buildAuthor(CreateArticleRequest request) {
        Author author = new Author();
        author.setName(request.authorName());
        author.setEmail(request.authorEmail());
        return author;
    }

    private static Article buildArticle(CreateArticleRequest request, Author author, EchoResponse echoResponse) {
        Article article = new Article();
        article.setCreationDate(Instant.now());
        article.setAuthorId(author.getId());
        article.setAuthor(author);
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

    public Mono<ArticleResponse> createArticle(CreateArticleRequest request) {
        return echoClient.createEcho(EchoRequest.buildExample())
                .zipWith(findOrCreateAuthor(request))
                .map(tuple -> buildArticle(request, tuple.getT2(), tuple.getT1()))
                .flatMap(articleRepository::save)
                .map(ArticleService::buildArticleResponse);
    }

    public Mono<ArticleResponse> findArticleById(long articleId) {
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

    public Mono<Void> deleteArticle(long articleId) {
        return articleRepository.deleteById(articleId);
    }

    private Mono<Author> findOrCreateAuthor(CreateArticleRequest request) {
        return authorRepository.findByEmail(request.authorEmail())
                .switchIfEmpty(authorRepository.save(buildAuthor(request)));
    }
}
