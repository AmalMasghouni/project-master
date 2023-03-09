package com.spring.project.Service.Email;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;

public interface EmailService {
    void SendEmail(final AbstractEmailContext email)throws MessagingException;
    void sendEmailWithAttachment(final String toAddress, final String subject, final String message, final String attachment) throws MessagingException, FileNotFoundException;
}
