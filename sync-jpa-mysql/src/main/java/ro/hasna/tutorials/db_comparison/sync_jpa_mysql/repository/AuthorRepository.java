package ro.hasna.tutorials.db_comparison.sync_jpa_mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByEmail(String authorEmail);

    Page<Author> findByName(String name, Pageable pageable);
}
