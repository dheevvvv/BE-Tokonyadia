package com.enigma.tokonyadia_api.dto.request;

import com.enigma.tokonyadia_api.constant.TransactionStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTransactionStatusRequest {
    private TransactionStatus status;
}
