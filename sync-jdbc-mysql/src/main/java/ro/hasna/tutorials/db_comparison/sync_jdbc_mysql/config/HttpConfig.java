package ro.hasna.tutorials.db_comparison.sync_jdbc_mysql.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ro.hasna.tutorials.db_comparison.sync.client.EchoClient;

import java.time.Duration;

@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate defaultRestTemplate() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(2));
        clientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(5));
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public EchoClient echoClient(@Value("${echo-server.url}") String baseUrl) {
        return new EchoClient(defaultRestTemplate(), baseUrl);
    }
}
