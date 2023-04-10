package com.github.wenqiglantz.service.customer.data.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "errorCode", "errorKey", "errorMessage"})
public class ErrorResponse {

    @JsonProperty("error-code")
    private String errorCode;

    @JsonProperty("error-key")
    private String errorKey;

    @JsonProperty("error-message")
    private String errorMessage;
}
