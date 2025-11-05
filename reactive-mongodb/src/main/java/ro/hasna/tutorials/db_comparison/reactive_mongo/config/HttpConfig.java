package ro.hasna.tutorials.db_comparison.reactive_mongo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import ro.hasna.tutorials.db_comparison.reactive.client.EchoClient;

import java.time.Duration;

@Configuration
public class HttpConfig {

    // see: https://github.com/reactor/reactor-netty/issues/1774
    @Bean
    public WebClient defaultWebClient() {
        ConnectionProvider provider = ConnectionProvider.builder("fixed")
                .maxConnections(500)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider)))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public EchoClient echoClient(@Value("${echo-server.url}") String baseUrl) {
        return new EchoClient(defaultWebClient(), baseUrl);
    }
}
