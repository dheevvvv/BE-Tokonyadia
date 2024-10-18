package com.enigma.tokonyadia_api.dto.request;

import com.enigma.tokonyadia_api.entity.ProductImage;
import com.enigma.tokonyadia_api.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private Long price;
    private Integer stock;
    private String storeId;
}
