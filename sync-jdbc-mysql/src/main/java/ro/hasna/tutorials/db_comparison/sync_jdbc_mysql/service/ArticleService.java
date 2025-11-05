package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.AuthorResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.CreateAuthorRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;
import ro.hasna.tutorials.db_comparison.sync.client.EchoClient;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.convertor.ArticleConvertor;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.domain.Article;
import ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.repository.ArticleRepository;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AuthorService authorService;
    private final EchoClient echoClient;

    @Transactional
    public ArticleResponse createArticle(CreateArticleRequest request) {
        try {
            EchoResponse echoResponse = echoClient.createEcho(EchoRequest.buildExample());
            AuthorResponse authorResponse = authorService.findOrCreate(new CreateAuthorRequest(request));
            Article article = ArticleConvertor.buildArticle(request, authorResponse, echoResponse);
            return ArticleConvertor.buildArticleResponse(articleRepository.save(article));
        } catch (DataIntegrityViolationException e) {

            throw new RuntimeException(e);
        }
    }

    public Page<ArticleResponse> findArticles(String authorName, PageRequest pageRequest) {
        if (authorName == null) {
            return articleRepository.findAll(pageRequest)
                    .map(ArticleConvertor::buildArticleResponse);
        }

        return articleRepository.findAllByAuthorName(authorName, pageRequest)
                .map(ArticleConvertor::buildArticleResponse);
    }

    public ArticleResponse findArticleById(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleConvertor::buildArticleResponse)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
