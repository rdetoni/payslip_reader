package com.example.payslip.model.enums;

import lombok.Getter;

@Getter
public enum AssetType {
    BR_STOCK("Brazilian Stock"),
    US_STOCK("US Stock"),
    FII("Real State Investment Trust");

    private final String description;

    AssetType(String description) {
        this.description = description;
    }

}
