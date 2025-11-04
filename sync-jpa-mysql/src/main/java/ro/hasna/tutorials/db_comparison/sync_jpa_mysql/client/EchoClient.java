package ro.hasna.tutorials.db_comparison.sync_jpa_mysql.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.hasna.tutorials.db_comparison.dto.EchoRequest;
import ro.hasna.tutorials.db_comparison.dto.EchoResponse;
import ro.hasna.tutorials.db_comparison.util.AppConstants;

import java.net.URI;

@Slf4j
@Component
public class EchoClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public EchoClient(RestTemplate restTemplate, @Value("${echo-server.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public EchoResponse createEcho(EchoRequest echoRequest) {
        log.debug("Calling echo server with request={}", echoRequest);
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment("echo")
                .queryParam("response_body_only", true)
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AppConstants.ECHO_SERVER_CONTENT_TYPE_HEADER_NAME, MediaType.APPLICATION_JSON_VALUE);
        headers.set(AppConstants.ECHO_SERVER_DELAY_HEADER_NAME, AppConstants.ECHO_SERVER_DELAY_VALUE);
        try {
            EchoResponse echoResponse = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(echoRequest, headers), EchoResponse.class)
                    .getBody();
            log.debug("Echo server successfully responds with {}", echoResponse);
            return echoResponse;
        } catch (RestClientException e) {
            log.error("Echo server failed to create an echo, request={}", echoRequest, e);
            throw e;
        }
    }
}
