package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashierResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private String userId;
}
