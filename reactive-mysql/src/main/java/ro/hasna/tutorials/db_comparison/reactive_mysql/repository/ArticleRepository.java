package ro.hasna.tutorials.db_comparison.reactive_mysql.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Article;

public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {
}
