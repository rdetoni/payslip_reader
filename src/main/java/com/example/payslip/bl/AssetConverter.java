package com.example.payslip.bl;

import com.example.payslip.model.entities.RicoBrokerNote;
import com.example.payslip.model.enums.AssetType;
import com.example.payslip.model.messages.AssetMessage;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class AssetConverter {
    public AssetMessage convertNoteToMessage(RicoBrokerNote note) {
        return AssetMessage.builder()
                .fees(note.getFees().doubleValue())
                .type(note.getName().contains("FII") ? AssetType.FII : AssetType.BR_STOCK)
                .name(note.getName())
                .paidAmount(note.getPrice().multiply(BigDecimal.valueOf(note.getQuantity())))
                .ticker(note.getTicker())
                .operationType(note.getOperationType())
                .quantity(note.getQuantity())
                .build();
    }
}
