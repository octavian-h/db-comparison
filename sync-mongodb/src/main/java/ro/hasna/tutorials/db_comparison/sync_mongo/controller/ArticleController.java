package ro.hasna.tutorials.db_comparison.sync_mongo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.sync_mongo.service.ArticleService;
import ro.hasna.tutorials.db_comparison.util.AppConstants;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppConstants.ARTICLES_API_PATH)
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse postArticle(@Valid @RequestBody CreateArticleRequest request) {
        return articleService.createArticle(request);
    }

    @GetMapping
    public PagedModel<ArticleResponse> getArticles(@RequestParam(name = "page", defaultValue = "0", required = false) int pageNumber,
                                                   @RequestParam(name = "size", defaultValue = "10", required = false) int pageSize,
                                                   @RequestParam(name = "author", required = false) String authorName) {
        return new PagedModel<>(articleService.findArticles(authorName, PageRequest.of(pageNumber, pageSize)));
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticleById(@PathVariable(name = "id") String articleId) {
        return articleService.findArticleById(articleId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticleById(@PathVariable(name = "id") String articleId) {
        articleService.deleteArticle(articleId);
    }
}
