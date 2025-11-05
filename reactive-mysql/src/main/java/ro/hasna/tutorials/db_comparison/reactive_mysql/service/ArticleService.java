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
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;
import ro.hasna.tutorials.db_comparison.reactive.client.EchoClient;
import ro.hasna.tutorials.db_comparison.reactive_mysql.convertor.ArticleConvertor;
import ro.hasna.tutorials.db_comparison.reactive_mysql.convertor.AuthorConvertor;
import ro.hasna.tutorials.db_comparison.reactive_mysql.repository.ArticleQueryRepository;
import ro.hasna.tutorials.db_comparison.reactive_mysql.repository.ArticleRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleQueryRepository articleQueryRepository;
    private final AuthorService authorService;
    private final EchoClient echoClient;

    public Mono<ArticleResponse> createArticle(CreateArticleRequest request) {
        return echoClient.createEcho(EchoRequest.buildExample())
                .zipWith(authorService.findOrCreateAuthor(new CreateAuthorRequest(request))
                        .map(AuthorConvertor::buildAuthor))
                .map(tuple -> ArticleConvertor.buildArticle(request, tuple.getT2(), tuple.getT1()))
                .flatMap(articleRepository::save)
                .map(ArticleConvertor::buildArticleResponse);
    }

    public Flux<ArticleResponse> findArticles(String authorName, PageRequest pageRequest) {
        if (authorName == null) {
            return articleQueryRepository.findAll(pageRequest)
                    .map(ArticleConvertor::buildArticleResponse);
        }
        return articleQueryRepository.findAllByAuthorName(authorName, pageRequest)
                .map(ArticleConvertor::buildArticleResponse);
    }

    public Mono<ArticleResponse> findArticleById(long articleId) {
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

    public Mono<Void> deleteArticle(long articleId) {
        return articleRepository.deleteById(articleId);
    }
}
