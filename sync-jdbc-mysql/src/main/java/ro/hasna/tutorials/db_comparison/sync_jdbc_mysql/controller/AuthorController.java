package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.service.AuthorService;
import ro.hasna.tutorials.db_comparison.util.AppConstants;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppConstants.AUTHOR_API_PATH)
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponse postAuthor(@Valid @RequestBody CreateAuthorRequest request) {
        return authorService.findOrCreate(request);
    }

    @GetMapping
    public PagedModel<AuthorResponse> getAuthors(@RequestParam(name = "page", defaultValue = "0", required = false) int pageNumber,
                                                 @RequestParam(name = "size", defaultValue = "10", required = false) int pageSize,
                                                 @RequestParam(name = "name") String name) {
        return new PagedModel<>(authorService.findAuthorsByName(name, PageRequest.of(pageNumber, pageSize)));
    }
}
