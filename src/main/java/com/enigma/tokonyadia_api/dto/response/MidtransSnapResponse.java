package com.enigma.tokonyadia_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransSnapResponse {
    @JsonProperty(value = "token")
    private String token;
    @JsonProperty(value = "redirect_url")
    private String redirectUrl;
}