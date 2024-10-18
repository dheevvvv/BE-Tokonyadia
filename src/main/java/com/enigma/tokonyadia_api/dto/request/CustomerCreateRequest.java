package com.enigma.tokonyadia_api.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCreateRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
