package ro.hasna.tutorials.db_comparison.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record EchoResponse(@JsonProperty int likes) {
}
