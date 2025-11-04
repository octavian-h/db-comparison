package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.client.EchoClient;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain.Author;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.repository.AuthorRepository;

import java.util.Optional;

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

    @Transactional
    public AuthorResponse findOrCreate(CreateAuthorRequest request) {
        return authorRepository.findByEmail(request.email())
                .or(() -> Optional.of(createAuthor(request)))
                .map(AuthorService::buildAuthorResponse)
                .orElseThrow(() -> new RuntimeException("Author not found and could not be created: " + request.email()));
    }

    private Author createAuthor(CreateAuthorRequest request) {
        EchoResponse echoResponse = echoClient.createEcho(EchoRequest.buildExample());
        Author author = new Author();
        author.setName(request.name());
        author.setEmail(echoResponse.likes() + "-" + request.email());
        return authorRepository.save(author);
    }

    public Page<AuthorResponse> findAuthorsByName(String name, PageRequest pageRequest) {
        return authorRepository.findByName(name, pageRequest)
                .map(AuthorService::buildAuthorResponse);
    }
}
