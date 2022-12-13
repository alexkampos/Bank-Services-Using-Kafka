package org.marlow.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marlow.model.dto.UserActionRequest;
import org.marlow.model.dto.UserActionResponse;
import org.marlow.model.enums.UserAction;
import org.marlow.service.UserActionFactory;
import org.marlow.service.UserActionService;
import org.marlow.util.Constants;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserActionFactory userActionFactory;

    @Mock
    private UserActionService userActionService;

    @InjectMocks
    private UserController controller;


    @Test
    void testPerformUserAction() {
        UserActionRequest userActionRequest = UserActionRequest.builder()
                .userAction(UserAction.DEPOSIT)
                .build();

        ResponseEntity<UserActionResponse> responseEntity = ResponseEntity.ok(UserActionResponse.builder().result(Constants.RESPONSE_OK).build());

        Mockito.when(userActionFactory.getUserActionService(userActionRequest.getUserAction())).thenReturn(userActionService);
        Mockito.when(userActionService.handleUserAction(userActionRequest)).thenReturn(responseEntity);

        controller.performUserAction(userActionRequest);

        Mockito.verify(userActionFactory).getUserActionService(userActionRequest.getUserAction());
        Mockito.verify(userActionService).handleUserAction(userActionRequest);
    }

}
