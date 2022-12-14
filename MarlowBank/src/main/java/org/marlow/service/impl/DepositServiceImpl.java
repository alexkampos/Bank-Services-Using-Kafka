package org.marlow.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.marlow.model.User;
import org.marlow.model.dto.AmountNotPositiveNumber;
import org.marlow.model.dto.UserActionRequest;
import org.marlow.model.dto.UserActionResponse;
import org.marlow.model.enums.UserAction;
import org.marlow.repository.UserRepository;
import org.marlow.service.UserActionService;
import org.marlow.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Log4j2
public class DepositServiceImpl implements UserActionService {

    private final UserRepository userRepository;

    /**
     * Searches user on database by the email provided
     * and adds the amount provided to user's total balance.
     *
     * @param userActionRequest The DTO POJO holding user request's info.
     * @return The JSON object holding response's result.
     */
    @Transactional
    @Override
    public ResponseEntity<UserActionResponse> handleUserAction(UserActionRequest userActionRequest) {
        log.info("Method DepositServiceImpl.handleUserAction() was invoked.");

        if (userActionRequest.getAmount().intValue() <= 0) {
            throw new AmountNotPositiveNumber(Constants.AMOUNT_NOT_POSITIVE);
        }
        User user = userRepository
                .findByEmailPessimistic(userActionRequest.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.EMAIL_NOT_FOUND));
        user.setBalance(user.getBalance().add(userActionRequest.getAmount()));
        if (user.getBalance().intValue() >= Constants.BANALANCE_LIMIT) {
            user.setShouldNotifyAboutUnderLimitBalance(true);
        }
        userRepository.save(user);
        log.info("User's balance was updated. [email: {}, balance: {}]", user.getEmail(), user.getBalance());
        UserActionResponse userActionResponse = UserActionResponse.builder().result(Constants.RESPONSE_OK).build();
        return ResponseEntity.ok(userActionResponse);
    }

    @Override
    public UserAction getUserAction() {
        return UserAction.DEPOSIT;
    }
}
