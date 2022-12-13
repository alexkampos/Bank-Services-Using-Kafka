package org.marlow.consumer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marlow.consumer.model.dto.BalanceUnderLimitMessage;
import org.marlow.consumer.service.impl.BalanceUnderLimitConsumerServiceImpl;
import org.marlow.consumer.util.Constants;
import org.marlow.consumer.util.TestConstants;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BalanceUnderLimitServiceImplTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BalanceUnderLimitConsumerServiceImpl balanceUnderLimitService;

    @Test
    void testHandleConsumedMessage() {
        BalanceUnderLimitMessage message = new BalanceUnderLimitMessage(TestConstants.MOCK_USER_EMAIL);

        Mockito.doNothing().when(emailService).sendEmail(
                message.getUserEmail(),
                Constants.EMAIL_SUBJECT_FOR_WARNING,
                Constants.EMAIL_TEXT_FOR_BALANCE_UNDER_LIMIT
        );

        balanceUnderLimitService.handleConsumedMessage(message);

        Mockito.verify(emailService).sendEmail(
                message.getUserEmail(),
                Constants.EMAIL_SUBJECT_FOR_WARNING,
                Constants.EMAIL_TEXT_FOR_BALANCE_UNDER_LIMIT
        );
    }

}
