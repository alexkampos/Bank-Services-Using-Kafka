package org.marlow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.marlow.model.dto.BalanceWarningKafkaMessage;
import org.marlow.service.KafkaProducerService;
import org.marlow.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service("balanceUnderLimitProducerServiceImpl")
@Log4j2
public class BalanceUnderLimitProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, BalanceWarningKafkaMessage> kafkaTemplate;

    @Async
    public void send(BalanceWarningKafkaMessage message) {
        log.info("BalanceUnderLimitProducerServiceImpl.send() was invoked. Message is: {}", message);
        kafkaTemplate.send(Constants.BALANCE_WARNING_TOPIC, UUID.randomUUID().toString(), message);
    }

}

