package org.marlow.service;

import org.marlow.model.enums.UserAction;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible to determine the correct UserActionService for user's request, based on Factory Pattern
 */
@Service
public class UserActionFactory {

    private Map<UserAction, UserActionService> userActionServiceMap;

    public UserActionFactory(final List<UserActionService> userActionServiceList) {
        userActionServiceMap = new EnumMap<>(UserAction.class);
        userActionServiceList.forEach(userActionService -> {
            userActionServiceMap.put(userActionService.getUserAction(), userActionService);
        });
    }

    public UserActionService getUserActionService(final UserAction userAction) {
        return this.userActionServiceMap.get(userAction);
    }

}
