package org.marlow.consumer.util;

public final class Constants {

    // KAFKA
    public static final String BALANCE_WARNING_TOPIC = "balance-warning";
    public static final String BALANCE_WARNING_GROUP_ID = "balance-warnings";

    // EMAIL
    public static final String EMAIL_FROM = "noreply@marlowbank.com";
    public static final String EMAIL_SUBJECT_FOR_WARNING = "Marlow Bank Warning Email";
    public static final String EMAIL_TEXT_FOR_BALANCE_UNDER_LIMIT = "Your account balance is under the limit!";
}
