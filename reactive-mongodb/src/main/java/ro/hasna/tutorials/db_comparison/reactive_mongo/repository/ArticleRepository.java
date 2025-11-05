package ro.hasna.tutorials.db_comparison.reactive_mongo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ro.hasna.tutorials.db_comparison.reactive_mongo.domain.Article;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {

    Flux<Article> findAllBy(Pageable pageable);

    Flux<Article> findAllByAuthorName(String authorName, Pageable pageable);
}
