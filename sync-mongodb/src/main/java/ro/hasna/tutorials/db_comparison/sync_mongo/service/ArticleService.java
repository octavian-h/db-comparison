package ro.hasna.tutorials.db_comparison.sync_mongo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.hasna.tutorials.db_comparison.dto.ArticleResponse;
import ro.hasna.tutorials.db_comparison.dto.CreateArticleRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;
import ro.hasna.tutorials.db_comparison.sync.client.EchoClient;
import ro.hasna.tutorials.db_comparison.sync_mongo.convertor.ArticleConvertor;
import ro.hasna.tutorials.db_comparison.sync_mongo.domain.Article;
import ro.hasna.tutorials.db_comparison.sync_mongo.repository.ArticleRepository;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EchoClient echoClient;

    public ArticleResponse createArticle(CreateArticleRequest request) {
        EchoResponse echoResponse = echoClient.createEcho(EchoRequest.buildExample());
        Article article = ArticleConvertor.buildArticle(request, echoResponse);
        return ArticleConvertor.buildArticleResponse(articleRepository.save(article));
    }

    public Page<ArticleResponse> findArticles(String authorName, PageRequest pageRequest) {
        if (authorName == null) {
            return articleRepository.findAllBy(pageRequest)
                    .map(ArticleConvertor::buildArticleResponse);
        }
        return articleRepository.findAllByAuthorName(authorName, pageRequest)
                .map(ArticleConvertor::buildArticleResponse);
    }

    public ArticleResponse findArticleById(String articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleConvertor::buildArticleResponse)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
    }

    public void deleteArticle(String articleId) {
        articleRepository.deleteById(articleId);
    }
}
