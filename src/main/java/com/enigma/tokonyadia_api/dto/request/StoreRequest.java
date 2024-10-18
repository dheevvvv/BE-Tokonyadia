package com.enigma.tokonyadia_api.dto.request;

import com.enigma.tokonyadia_api.entity.AdminStore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {
    private String id;
    private String noSiup;
    private String name;
    private String phoneNumber;
    private String address;
    private String adminStoreId;
}
