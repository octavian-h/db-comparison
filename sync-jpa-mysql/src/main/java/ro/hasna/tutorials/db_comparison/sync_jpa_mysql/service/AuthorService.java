package ro.hasna.tutorials.db_comparison.sync_jpa_mysql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.convertor.AuthorConvertor;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.repository.AuthorRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    public AuthorResponse findOrCreate(CreateAuthorRequest request) {
        return authorRepository.findByEmail(request.email())
                .or(() -> Optional.of(authorRepository.save(AuthorConvertor.buildAuthor(request))))
                .map(AuthorConvertor::buildAuthorResponse)
                .orElseThrow(() -> new RuntimeException("Author not found and could not be created: " + request.email()));
    }

    public Page<AuthorResponse> findAuthorsByName(String name, PageRequest pageRequest) {
        return authorRepository.findByName(name, pageRequest)
                .map(AuthorConvertor::buildAuthorResponse);
    }
}
