package org.marlow.controller;

import lombok.RequiredArgsConstructor;
import org.marlow.model.dto.UserActionRequest;
import org.marlow.service.UserActionFactory;
import org.marlow.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserActionFactory userActionFactory;

    @PostMapping(Constants.USER_PERFORM_ACTION_URL)
    public ResponseEntity performUserAction(@RequestBody final UserActionRequest request) {
         var userActionService = userActionFactory.getUserActionService(request.getUserAction());
         return userActionService.handleUserAction(request);
    }

}
