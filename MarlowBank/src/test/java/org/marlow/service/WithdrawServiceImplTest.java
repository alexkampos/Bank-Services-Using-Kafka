package org.marlow.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marlow.model.User;
import org.marlow.model.dto.*;
import org.marlow.model.enums.UserAction;
import org.marlow.repository.UserRepository;
import org.marlow.service.impl.WithdrawServiceImpl;
import org.marlow.util.Constants;
import org.marlow.util.TestConstants;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WithdrawServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private WithdrawServiceImpl withdrawService;

    @Test
    void testHandleUserActionWithUserNotification() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.WITHDRAW)
                .amount(BigDecimal.valueOf(1401))
                .build();

        User testUser = User.builder()
                .balance(TestConstants.MOCK_USER_BALANCE)
                .shouldNotifyAboutUnderLimitBalance(true)
                .build();

        BalanceWarningKafkaMessage message = BalanceWarningKafkaMessage.builder()
                .userEmail(testUser.getEmail())
                .build();

        Mockito.when(userRepository.findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.doNothing().when(kafkaProducerService).send(message);

        ResponseEntity<UserActionResponse> response = withdrawService.handleUserAction(userActionRequest);

        Mockito.verify(userRepository).findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL);
        Mockito.verify(userRepository).save(testUser);
        Mockito.verify(kafkaProducerService).send(message);

        String responseResult = Objects.requireNonNull(response.getBody()).getResult();
        Assertions.assertEquals(responseResult, Constants.RESPONSE_OK);
        Assertions.assertEquals(testUser.getBalance().intValue(), 99);
        Assertions.assertFalse(testUser.isShouldNotifyAboutUnderLimitBalance());
    }

    @Test
    void testHandleUserActionWithoutUserNotification() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.WITHDRAW)
                .amount(BigDecimal.valueOf(100))
                .build();

        User testUser = User.builder()
                .balance(TestConstants.MOCK_USER_BALANCE)
                .shouldNotifyAboutUnderLimitBalance(true)
                .build();

        Mockito.when(userRepository.findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        ResponseEntity<UserActionResponse> response = withdrawService.handleUserAction(userActionRequest);

        Mockito.verify(userRepository).findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL);
        Mockito.verify(userRepository).save(testUser);

        String responseResult = Objects.requireNonNull(response.getBody()).getResult();
        Assertions.assertEquals(responseResult, Constants.RESPONSE_OK);
        Assertions.assertEquals(testUser.getBalance().intValue(), 1400);
        Assertions.assertTrue(testUser.isShouldNotifyAboutUnderLimitBalance());
    }

    @Test
    void testHandleUserActionWhenAmountNotPositive() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.WITHDRAW)
                .amount(BigDecimal.valueOf(0))
                .build();

        Exception exception = assertThrows(AmountNotPositiveNumber.class, () -> {
            withdrawService.handleUserAction(userActionRequest);
        });

        Assertions.assertEquals(exception.getMessage(), Constants.AMOUNT_NOT_POSITIVE);
    }

    @Test
    void testHandleUserActionWhenUserNotFound() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.WITHDRAW)
                .amount(BigDecimal.valueOf(100))
                .build();

        Mockito.when(userRepository.findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            withdrawService.handleUserAction(userActionRequest);
        });

        Mockito.verify(userRepository).findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL);
        Assertions.assertEquals(exception.getMessage(), Constants.EMAIL_NOT_FOUND);
    }

    @Test
    void testHandleUserActionWhenOverdraft() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.WITHDRAW)
                .amount(BigDecimal.valueOf(1501))
                .build();

        User testUser = User.builder()
                .balance(TestConstants.MOCK_USER_BALANCE)
                .build();

        Mockito.when(userRepository.findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL)).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(OverdraftException.class, () -> {
            withdrawService.handleUserAction(userActionRequest);
        });

        Mockito.verify(userRepository).findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL);
        Assertions.assertEquals(exception.getMessage(), Constants.BALANCE_NOT_ENOUGH);
    }
}
