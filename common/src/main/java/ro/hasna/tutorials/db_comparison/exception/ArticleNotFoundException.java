package ro.hasna.tutorials.db_comparison.exception;

import lombok.Getter;

@Getter
public class ArticleNotFoundException extends RuntimeException {

    private final String articleId;

    public ArticleNotFoundException(String articleId) {
        this.articleId = articleId;
    }

    public ArticleNotFoundException(long articleId) {
        this.articleId = "" + articleId;
    }
}
