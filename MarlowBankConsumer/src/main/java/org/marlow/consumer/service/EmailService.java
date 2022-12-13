package org.marlow.consumer.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
