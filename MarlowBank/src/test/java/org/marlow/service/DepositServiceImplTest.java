package org.marlow.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marlow.model.User;
import org.marlow.model.dto.AmountNotPositiveNumber;
import org.marlow.model.dto.UserActionRequest;
import org.marlow.model.dto.UserActionResponse;
import org.marlow.model.enums.UserAction;
import org.marlow.repository.UserRepository;
import org.marlow.service.impl.DepositServiceImpl;
import org.marlow.util.Constants;
import org.marlow.util.TestConstants;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DepositServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    @Test
    @WithMockUser
    void testHandleUserAction() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.DEPOSIT)
                .amount(BigDecimal.valueOf(100))
                .build();

        User testUser = User.builder()
                .balance(TestConstants.MOCK_USER_BALANCE)
                .build();

        Mockito.when(userRepository.findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        ResponseEntity<UserActionResponse> response = depositService.handleUserAction(userActionRequest);

        Mockito.verify(userRepository).findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL);
        Mockito.verify(userRepository).save(testUser);
        String responseResult = Objects.requireNonNull(response.getBody()).getResult();
        Assertions.assertEquals(responseResult, Constants.RESPONSE_OK);
        Assertions.assertEquals(testUser.getBalance().intValue(), 1600);
    }

    @Test
    void testHandleUserActionWhenAmountNotPositive() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.DEPOSIT)
                .amount(BigDecimal.valueOf(0))
                .build();

        Exception exception = assertThrows(AmountNotPositiveNumber.class, () -> {
            depositService.handleUserAction(userActionRequest);
        });

        Assertions.assertEquals(exception.getMessage(), Constants.AMOUNT_NOT_POSITIVE);
    }

    @Test
    void testHandleUserActionWhenUserNotFound() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userEmail(TestConstants.MOCK_USER_EMAIL)
                .userAction(UserAction.DEPOSIT)
                .amount(BigDecimal.valueOf(100))
                .build();

        Mockito.when(userRepository.findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            depositService.handleUserAction(userActionRequest);
        });

        Mockito.verify(userRepository).findByEmailPessimistic(TestConstants.MOCK_USER_EMAIL);
        Assertions.assertEquals(exception.getMessage(), Constants.EMAIL_NOT_FOUND);
    }

}
