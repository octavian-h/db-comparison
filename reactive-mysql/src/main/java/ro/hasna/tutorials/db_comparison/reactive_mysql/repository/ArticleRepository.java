package ro.hasna.tutorials.db_comparison.reactive_mysql.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Article;

public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {
    Flux<Article> findAllBy(Pageable pageable);

    Flux<Article> findAllByAuthorName(String authorName, Pageable pageable);
}
