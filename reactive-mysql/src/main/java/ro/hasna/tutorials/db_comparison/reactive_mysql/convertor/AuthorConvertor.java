package ro.hasna.tutorials.db_comparison.reactive_mysql.convertor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.reactive_mysql.domain.Author;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorConvertor {
    public static AuthorResponse buildAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(String.valueOf(author.getId()))
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    public static Author buildAuthor(CreateAuthorRequest request) {
        Author author = new Author();
        author.setName(request.name());
        author.setEmail(request.email());
        return author;
    }

    public static Author buildAuthor(AuthorResponse authorResponse) {
        Author author = new Author();
        author.setId(Long.parseLong(authorResponse.id()));
        author.setName(authorResponse.name());
        author.setEmail(authorResponse.email());
        return author;
    }


}
