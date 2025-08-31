package com.example.payslip.model.messages;

import com.example.payslip.model.enums.AssetType;
import com.example.payslip.model.enums.OperationType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AssetMessage {
    private String ticker;
    private Double fees;
    private String name;
    private BigDecimal paidAmount;
    private Integer quantity;
    private AssetType type;
    private OperationType operationType;
}
