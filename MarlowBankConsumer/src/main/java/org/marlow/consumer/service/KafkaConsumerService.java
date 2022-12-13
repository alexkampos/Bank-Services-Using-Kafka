package org.marlow.consumer.service;

import org.marlow.consumer.model.dto.KafkaMessage;

public interface KafkaConsumerService {

    void handleConsumedMessage(KafkaMessage kafkaMessage);

}
