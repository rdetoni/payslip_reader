package com.example.payslip.model.enums;

public enum OperationType {
    BUY("C"),
    SELL("V");

    private final String code;

    OperationType(String code) {
        this.code = code;
    }

    public static OperationType fromCode(String code) {
        for (OperationType op : values()) {
            if (op.code.equalsIgnoreCase(code)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
