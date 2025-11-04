package ro.hasna.tutorials.db_comparison.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstants {

    public static final String ARTICLES_API_PATH = "/api/v1/articles";
    public static final String AUTHOR_API_PATH = "/api/v1/authors";
    public static final String ECHO_SERVER_CONTENT_TYPE_HEADER_NAME = "x-set-response-content-type";
    public static final String ECHO_SERVER_DELAY_HEADER_NAME = "x-set-response-delay-ms";
    public static final String ECHO_SERVER_DELAY_VALUE = "100";
}
