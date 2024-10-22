package com.enigma.tokonyadia_api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionStatus {
    DRAFT("Draft"),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    EXPIRE("Expired"),
    CANCEL("Cancel"),
    DENY("Deny"),
    PROCESSING("Processing"),
    COMPLETED("Completed");

    private final String description;

    public static TransactionStatus findByDesc(String desc) {
        for (TransactionStatus value : values()) {
            if (value.getDescription().equalsIgnoreCase(desc)) {
                return value;
            }
        }
        return null;
    }
}
