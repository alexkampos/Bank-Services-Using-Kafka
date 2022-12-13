package org.marlow.consumer.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.marlow.consumer.model.dto.BalanceUnderLimitMessage;
import org.marlow.consumer.model.dto.KafkaMessage;
import org.marlow.consumer.service.EmailService;
import org.marlow.consumer.service.KafkaConsumerService;
import org.marlow.consumer.util.Constants;
import org.springframework.stereotype.Service;

@Service("balanceUnderLimitConsumerServiceImpl")
@Data
@RequiredArgsConstructor
@Log4j2
public class BalanceUnderLimitConsumerServiceImpl implements KafkaConsumerService {

    private final EmailService emailService;

    @Override
    public void handleConsumedMessage(KafkaMessage kafkaMessage) {
        log.info("Method BalanceUnderLimitServiceImpl.handleConsumedMessage() was invoked.");
        BalanceUnderLimitMessage balanceUnderLimitMessage = (BalanceUnderLimitMessage) kafkaMessage;
        emailService.sendEmail(balanceUnderLimitMessage.getUserEmail(), Constants.EMAIL_SUBJECT_FOR_WARNING, Constants.EMAIL_TEXT_FOR_BALANCE_UNDER_LIMIT);
    }

}
