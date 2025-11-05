package ro.hasna.tutorials.db_comparison.reactive_mongo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;
import ro.hasna.tutorials.db_comparison.reactive.client.EchoClient;
import ro.hasna.tutorials.db_comparison.reactive_mongo.convertor.ArticleConvertor;
import ro.hasna.tutorials.db_comparison.reactive_mongo.repository.ArticleRepository;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EchoClient echoClient;

    public Mono<ArticleResponse> createArticle(CreateArticleRequest createArticleRequest) {
        return echoClient.createEcho(EchoRequest.buildExample())
                .map(echoResponse -> ArticleConvertor.buildArticle(createArticleRequest, echoResponse))
                .flatMap(articleRepository::save)
                .map(ArticleConvertor::buildArticleResponse);
    }

    public Flux<ArticleResponse> findArticles(String authorName, PageRequest pageRequest) {
        if (authorName == null) {
            return articleRepository.findAllBy(pageRequest)
                    .map(ArticleConvertor::buildArticleResponse);
        }
        return articleRepository.findAllByAuthorName(authorName, pageRequest)
                .map(ArticleConvertor::buildArticleResponse);
    }

    public Mono<ArticleResponse> findArticleById(String articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleConvertor::buildArticleResponse)
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
