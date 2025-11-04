package ro.hasna.tutorials.db_comparison.sync_jpa_mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByAuthorName(String authorName, Pageable pageable);
}
