package com.enigma.tokonyadia_api.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransItemDetailRequest {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "price")
    private Long price;

    @JsonProperty(value = "quantity")
    private Integer quantity;

}
