package ro.hasna.tutorials.db_comparison.reactive_mysql.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, String> {

    Mono<Author> findByEmail(String authorEmail);

    Flux<Author> findByName(String name, Pageable pageable);
}
