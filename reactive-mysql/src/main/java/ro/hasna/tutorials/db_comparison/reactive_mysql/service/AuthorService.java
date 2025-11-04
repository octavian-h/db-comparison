package ro.hasna.tutorials.db_comparison.reactive_mysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.reactive_mysql.client.EchoClient;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;
import ro.hasna.tutorials.db_comparison.reactive_mysql.repository.AuthorRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final EchoClient echoClient;

    private static AuthorResponse buildAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(String.valueOf(author.getId()))
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    private static Author buildAuthor(CreateAuthorRequest request, EchoResponse echoResponse) {
        Author author = new Author();
        author.setName(request.name());
        author.setEmail(echoResponse.likes() + "-" + request.email());
        return author;
    }

    public Mono<AuthorResponse> findOrCreate(CreateAuthorRequest request) {
        return authorRepository.findByEmail(request.email())
                .or(createAuthor(request))
                .map(AuthorService::buildAuthorResponse);
    }

    private Mono<Author> createAuthor(CreateAuthorRequest request) {
        return echoClient.createEcho(EchoRequest.buildExample())
                .map(echoResponse -> buildAuthor(request, echoResponse))
                .flatMap(authorRepository::save);
    }

    public Flux<AuthorResponse> findAuthorsByName(String name, PageRequest pageRequest) {
        return authorRepository.findByName(name, pageRequest)
                .map(AuthorService::buildAuthorResponse);
    }
}
