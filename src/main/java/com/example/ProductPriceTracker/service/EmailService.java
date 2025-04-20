package com.example.ProductPriceTracker.service;

import com.example.ProductPriceTracker.dto.AlertRequest;
import com.example.ProductPriceTracker.dto.ProductPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;


    public void sendPriceDropEmail(String toEmail, String subject, String body) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(emailFrom);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            mailSender.send(simpleMailMessage);
            log.info("✅ Email sent successfully to {}", toEmail);
        }
        catch (Exception exception){
            exception.getMessage();
            log.error("Error occurred while sending mail to {}",emailFrom);
        }

    }

    //jasminejose17@gmail.com
    public void notifyUser(ProductPrice product, AlertRequest alert, AlertService alertService) {
        log.info("notifying user...");
        String productName = product.getProductUrl().substring(product.getProductUrl().lastIndexOf("/") + 1);
        String body = String.format(
                "Hi there,\n\n" +
                        "Good news! The product you're tracking has dropped in price.\n\n" +
                        "🛍 Product: %s\n" +
                        "Current Price: ₹%.2f\n" +
                        "Your Alert Price: ₹%.2f\n\n" +
                        "You can check it out here:\n%s\n\n" +
                        "Hurry before the deal is gone!\n\n" +
                        "— Price Drop Alert Team",
                productName, product.getCurrentPrice(), alert.getDesiredPrice(), product.getProductUrl()
        );

        sendPriceDropEmail(alert.getEmail(),"🔔 Hurry!! Price Drop Notification – Don't Miss This Deal!",body);
    }
}

