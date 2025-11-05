package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ro.hasna.tutorials.db_comparison.exception.ArticleNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleArticleNotFoundException(ArticleNotFoundException e) {
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, "Article with the given id was not found")
                .property("articleId", e.getArticleId())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())
                .build();
    }
}
