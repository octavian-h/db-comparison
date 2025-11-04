package ro.hasna.tutorials.db_comparison.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ArticleResponse(
        @JsonProperty String id,
        @JsonProperty @NotNull Instant creationDate,
        @JsonProperty @Schema(example = "William Shakespeare") @NotBlank @Size(max = 255) String authorName,
        @JsonProperty @Schema(example = "william@shakespeare.com") @Email @Size(max = 255) String authorEmail,
        @JsonProperty @Schema(example = "To be or not to be...") @NotBlank @Size(max = 1024) String content,
        @JsonProperty @Schema(example = "5412") int likes) {
}
