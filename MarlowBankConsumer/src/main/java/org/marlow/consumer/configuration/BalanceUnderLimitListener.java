package org.marlow.consumer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.marlow.consumer.model.dto.BalanceUnderLimitMessage;
import org.marlow.consumer.service.KafkaConsumerService;
import org.marlow.consumer.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BalanceUnderLimitListener {

    @Qualifier("balanceUnderLimitConsumerServiceImpl")
    private final KafkaConsumerService service;

    /*
     * Below method can be more elegant, regarding the deserialization of the message.
     * */
    @SneakyThrows
    @KafkaListener(
            topics = Constants.BALANCE_WARNING_TOPIC,
            groupId = Constants.BALANCE_WARNING_GROUP_ID
    )
    void listener(@Payload String message) {
        String email = new ObjectMapper().readTree(message).path("userEmail").asText();
        service.handleConsumedMessage(new BalanceUnderLimitMessage(email));
    }

}
