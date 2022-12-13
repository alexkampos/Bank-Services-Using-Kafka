package org.marlow.service;

import org.marlow.model.dto.BalanceWarningKafkaMessage;

public interface KafkaProducerService {

    void send(BalanceWarningKafkaMessage message);

}
