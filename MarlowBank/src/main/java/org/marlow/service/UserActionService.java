package org.marlow.service;

import org.marlow.model.dto.UserActionRequest;
import org.marlow.model.dto.UserActionResponse;
import org.marlow.model.enums.UserAction;
import org.springframework.http.ResponseEntity;

public interface UserActionService {

    ResponseEntity<UserActionResponse> handleUserAction(final UserActionRequest producerRequest);

    UserAction getUserAction();

}
