package org.marlow.util;

public final class Constants {

    // ENDPOINTS
    public static final String USER_PERFORM_ACTION_URL = "/user";

    // SECURITY ENDPOINTS
    public static final String USER_PERFORM_ACTION_URL_SEC = "/user";

    // ERRORS MESSAGES
    public static final String ERROR_MESSAGE_NOT_AVAILABLE = "message not available";
    public static final String EMAIL_NOT_FOUND = "email not found";
    public static final String BALANCE_NOT_ENOUGH = "amount requested is bigger than balance";
    public static final String AMOUNT_NOT_POSITIVE = "amount received is not positive";

    // MESSAGES
    public static final String RESPONSE_OK = "SUCCESS";
    public static final String RESPONSE_NOK = "FAIL";

    // KAFKA
    public static final String USER_ACTION_TOPIC = "user-actions";
    public static final Integer USER_ACTION_PARTITION_COUNT = 3;
    public static final Integer USER_ACTION_REPLICAS_COUNT = 2;

    // GENERIC
    public static final Integer BANALANCE_LIMIT = 100;

}
