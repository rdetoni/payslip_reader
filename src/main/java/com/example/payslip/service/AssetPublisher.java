package com.example.payslip.service;

import com.example.payslip.config.RabbitConfig;
import com.example.payslip.model.messages.AssetMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AssetPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAsset(AssetMessage assetMessage) {
        rabbitTemplate.convertAndSend(RabbitConfig.BROKER_NOTE_INFO_QUEUE, assetMessage);
    }
}
