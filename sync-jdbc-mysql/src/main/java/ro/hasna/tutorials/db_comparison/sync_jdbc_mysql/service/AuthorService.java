package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.convertor.AuthorConvertor;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain.Author;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.repository.AuthorRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorResponse findOrCreate(CreateAuthorRequest request) {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(request.email());
        if (optionalAuthor.isPresent()) {
            return AuthorConvertor.buildAuthorResponse(optionalAuthor.get());
        }
        try {
            Author saved = authorRepository.save(AuthorConvertor.buildAuthor(request));
            return AuthorConvertor.buildAuthorResponse(saved);
        } catch (DuplicateKeyException e) {
            // Another thread inserted the author concurrently
            Author existing = authorRepository.findByEmail(request.email())
                    .orElseThrow(() -> new RuntimeException("Author could not be created or found: " + request.email()));
            return AuthorConvertor.buildAuthorResponse(existing);
        }
//        return authorRepository.findByEmail(request.email())
//                .or(() -> Optional.of(authorRepository.save(AuthorConvertor.buildAuthor(request))))
//                .map(AuthorConvertor::buildAuthorResponse)
//                .orElseThrow(() -> new RuntimeException("Author not found and could not be created: " + request.email()));
    }

    public Page<AuthorResponse> findAuthorsByName(String name, PageRequest pageRequest) {
        return authorRepository.findByName(name, pageRequest)
                .map(AuthorConvertor::buildAuthorResponse);
    }
}
