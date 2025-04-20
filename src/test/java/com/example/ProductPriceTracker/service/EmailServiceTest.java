package com.example.ProductPriceTracker.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private String emailFrom = "test@example.com";

    @Before
    public void setUp() throws Exception {
        // Set private field 'emailFrom' using reflection
        java.lang.reflect.Field emailFromField = EmailService.class.getDeclaredField("emailFrom");
        emailFromField.setAccessible(true);
        emailFromField.set(emailService, emailFrom);
    }

    @Test
    public void testSendPriceDropEmail() {
        String to = "user@example.com";
        String subject = "Price Drop!";
        String body = "This is a test email.";

        // Act
        emailService.sendPriceDropEmail(to, subject, body);

        // Verify that mailSender.send() was called with the correct message
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assert sentMessage.getTo()[0].equals(to);
        assert sentMessage.getSubject().equals(subject);
        assert sentMessage.getText().equals(body);
        assert sentMessage.getFrom().equals(emailFrom);
    }
}
