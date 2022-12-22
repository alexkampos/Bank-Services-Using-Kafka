package org.marlow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.marlow.model.User;
import org.marlow.model.dto.*;
import org.marlow.model.enums.UserAction;
import org.marlow.repository.UserRepository;
import org.marlow.service.KafkaProducerService;
import org.marlow.service.UserActionService;
import org.marlow.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Log4j2
public class WithdrawServiceImpl implements UserActionService {

    private final UserRepository userRepository;

    @Qualifier("balanceUnderLimitProducerServiceImpl")
    private final KafkaProducerService kafkaProducerService;

    /**
     * Searches user on database by the email provided and if user's total balance is enough,
     * subtracts the amount provided from it. Furthermore, if user's balance is less than
     * the configured limit, a message is sent to the messaging system,
     * in order for the user to be notified.
     *
     * @param userActionRequest The DTO POJO holding user request's info.
     * @return The JSON object holding response's result.
     */
    @Transactional
    @Override
    public ResponseEntity<UserActionResponse> handleUserAction(UserActionRequest userActionRequest) {
        log.info("Method WithdrawServiceImpl.handleUserAction() was invoked.");

        if (userActionRequest.getAmount().intValue() <= 0) {
            throw new AmountNotPositiveNumber(Constants.AMOUNT_NOT_POSITIVE);
        }
        User user = userRepository
                .findByEmailPessimistic(userActionRequest.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.EMAIL_NOT_FOUND));
        // check if user's balance is less than the withdrawal request's amount
        if (user.getBalance().compareTo(userActionRequest.getAmount()) == -1) {
            throw new OverdraftException(Constants.BALANCE_NOT_ENOUGH);
        }
        user.setBalance(user.getBalance().subtract(userActionRequest.getAmount()));

        boolean isUserBalanceUnderLimit = user.getBalance().compareTo(BigDecimal.valueOf(Constants.BANALANCE_LIMIT)) == -1;
        if (isUserBalanceUnderLimit && user.isShouldNotifyAboutUnderLimitBalance()) {
            kafkaProducerService.send(BalanceWarningKafkaMessage.builder()
                    .userEmail(user.getEmail())
                    .build());
            user.setShouldNotifyAboutUnderLimitBalance(false);

        }

        userRepository.save(user);
        log.info("User's balance was updated. [email: {}, balance: {}]", user.getEmail(), user.getBalance());

        UserActionResponse userActionResponse = UserActionResponse.builder().result(Constants.RESPONSE_OK).build();
        return ResponseEntity.ok(userActionResponse);
    }


    @Override
    public UserAction getUserAction() {
        return UserAction.WITHDRAW;
    }
}
