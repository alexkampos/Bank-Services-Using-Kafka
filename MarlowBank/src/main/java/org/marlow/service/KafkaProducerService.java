package org.marlow.service;

import org.marlow.model.dto.UserActionKafkaMessage;

public interface KafkaProducerService {

    void send(UserActionKafkaMessage message);

}
