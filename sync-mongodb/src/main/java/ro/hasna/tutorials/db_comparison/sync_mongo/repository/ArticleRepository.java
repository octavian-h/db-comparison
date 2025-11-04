package ro.hasna.tutorials.db_comparison.sync_mongo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ro.hasna.tutorials.db_comparison.sync_mongo.domain.Article;

public interface ArticleRepository extends MongoRepository<Article, String> {

    Page<Article> findAllBy(Pageable pageable);

    Page<Article> findAllByAuthorName(String authorName, Pageable pageable);
}
