package ro.hasna.tutorials.db_comparison.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Random;

@Builder
public record EchoRequest(@JsonProperty int likes) {

    public static EchoRequest buildExample() {
        return EchoRequest.builder()
                .likes(new Random().nextInt(10000))
                .build();
    }
}
