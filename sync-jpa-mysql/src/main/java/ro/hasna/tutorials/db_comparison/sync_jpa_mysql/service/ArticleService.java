package ro.hasna.tutorials.db_comparison.sync_jpa_mysql.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.client.EchoClient;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.domain.Article;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.domain.Author;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.repository.ArticleRepository;
import ro.hasna.tutorials.db_comparison.sync_jpa_mysql.repository.AuthorRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AuthorRepository authorRepository;
    private final EchoClient echoClient;

    private static ArticleResponse buildArticleResponse(Article article) {
        Author author = article.getAuthor();
        return ArticleResponse.builder()
                .id(Optional.ofNullable(article.getId())
                        .map(Objects::toString)
                        .orElse(null))
                .authorName(Optional.ofNullable(author).map(Author::getName).orElse(null))
                .authorEmail(Optional.ofNullable(author).map(Author::getEmail).orElse(null))
                .content(article.getContent())
                .creationDate(article.getCreationDate())
                .likes(Optional.ofNullable(article.getLikes())
                        .orElse(0))
                .build();
    }

    public Page<ArticleResponse> findArticles(String authorName, PageRequest pageRequest) {
        if (authorName == null) {
            return articleRepository.findAll(pageRequest)
                    .map(ArticleService::buildArticleResponse);
        }
        return articleRepository.findAllByAuthorName(authorName, pageRequest)
                .map(ArticleService::buildArticleResponse);
    }

    public ArticleResponse createArticle(CreateArticleRequest request) {
        EchoResponse echoResponse = echoClient.createEcho(EchoRequest.buildExample());
        Author author = authorRepository.findByEmail(request.authorEmail())
                .orElseGet(() -> createAuthor(request));

        Article article = new Article();
        article.setCreationDate(Instant.now());
        article.setAuthor(author);
        article.setContent(request.content());
        article.setLikes(echoResponse.likes());
        return buildArticleResponse(articleRepository.save(article));
    }

    public ArticleResponse findArticleById(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleService::buildArticleResponse)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    private Author createAuthor(CreateArticleRequest request) {
        Author author = new Author();
        author.setName(request.authorName());
        author.setEmail(request.authorEmail());
        return authorRepository.save(author);
    }
}
