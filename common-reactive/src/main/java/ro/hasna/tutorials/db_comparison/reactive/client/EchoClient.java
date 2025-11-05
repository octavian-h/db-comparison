package ro.hasna.tutorials.db_comparison.reactive.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.util.AppConstants;

import java.net.URI;

@Slf4j
public class EchoClient {

    private final WebClient webClient;
    private final String baseUrl;

    public EchoClient(WebClient webClient, String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    public Mono<EchoResponse> createEcho(EchoRequest echoRequest) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment("echo")
                .queryParam("response_body_only", true)
                .build()
                .toUri();
        return webClient.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(AppConstants.ECHO_SERVER_CONTENT_TYPE_HEADER_NAME, MediaType.APPLICATION_JSON_VALUE)
                .header(AppConstants.ECHO_SERVER_DELAY_HEADER_NAME, AppConstants.ECHO_SERVER_DELAY_VALUE)
                .bodyValue(echoRequest)
                .retrieve()
                .bodyToMono(EchoResponse.class)
                .doFirst(() -> log.debug("Calling echo server with request={}", echoRequest))
                .doOnSuccess(echoResponse -> log.debug("Echo server successfully responds with {}", echoResponse))
                .doOnError(e -> log.error("Echo server failed to create an echo, request={}", echoRequest, e));
    }
}
