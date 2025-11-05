package ro.hasna.tutorials.db_comparison.reactive_mysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.reactive_mysql.convertor.AuthorConvertor;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;
import ro.hasna.tutorials.db_comparison.reactive_mysql.repository.AuthorRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Mono<AuthorResponse> findOrCreateAuthor(CreateAuthorRequest request) {
        return authorRepository.findByEmail(request.email())
                .switchIfEmpty(createAuthor(request))
                .map(AuthorConvertor::buildAuthorResponse);
    }

    private Mono<Author> createAuthor(CreateAuthorRequest request) {
        return authorRepository.save(AuthorConvertor.buildAuthor(request));
    }

    public Flux<AuthorResponse> findAuthorsByName(String name, PageRequest pageRequest) {
        return authorRepository.findByName(name, pageRequest)
                .map(AuthorConvertor::buildAuthorResponse);
    }
}
