package org.marlow.consumer.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.marlow.consumer.service.EmailService;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

//    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        log.info("Method EmailServiceImpl.sendEmail() was invoked. Sending email to: {}.", to);

        /*
         * Below code can be used in case we want to email notification to user.
         * Is commented out so that the app can start easier (without the need of email config).
         * */
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(Constants.EMAIL_FROM);
//        message.setTo("giorgospantis1998@gmail.com");
//        message.setSubject(subject);
//        message.setText(text);
//        javaMailSender.send(message);

        log.info("Notification send to user email: {}", to);
    }

}
