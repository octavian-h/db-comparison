package ro.hasna.tutorials.db_comparison.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateAuthorRequest(
        @JsonProperty @Schema(example = "William Shakespeare") @NotBlank @Size(max = 255) String name,
        @JsonProperty @Schema(example = "william@shakespeare.com") @Email @Size(max = 255) String email) {

    public CreateAuthorRequest(CreateArticleRequest request) {
        this(request.authorName(), request.authorEmail());
    }
}
