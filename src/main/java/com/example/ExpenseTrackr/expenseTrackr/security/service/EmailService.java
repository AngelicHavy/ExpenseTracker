package com.example.ExpenseTrackr.expenseTrackr.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

@Service
public class EmailService {
    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    public void sendVerificationEmail(String to, String code) throws Exception {
        Email from = new Email("nadya.yarmak03@gmail.com");
        String subject = "Verification Code";
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", "Your verification code is: " + code);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw new Exception("Error sending email", ex);
        }
    }
}
