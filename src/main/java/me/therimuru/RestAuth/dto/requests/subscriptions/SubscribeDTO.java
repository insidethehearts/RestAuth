package me.therimuru.RestAuth.dto.requests.subscriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.therimuru.RestAuth.dto.DataTransferObject;

@Getter
@Setter
public class SubscribeDTO implements DataTransferObject {

    @NotNull
    @Min(0)
    @JsonProperty("target_id")
    private Long targetId;

    @Override
    public String debugString() {
        return "SubscribeDTO[%s]".formatted(targetId);
    }
}
