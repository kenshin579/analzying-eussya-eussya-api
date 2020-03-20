package com.sproutt.eussyaeussyaapi.application;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Test
    public void send_mail() {
        String to = "kjkun7631@naver.com";

        mailService.sendAuthEmail(to);
    }

    @Test
    public void send_wrong_email_address() {
        String to = "wrongAddress";

        assertThrows(MailSendException.class, () -> mailService.sendAuthEmail(to));
    }
}