package com.warriorfoot.api.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
  export MAIL_USERNAME="marcelobreis12345@gmail.com"
  export MAIL_PASSWORD="boby lvyg wzrp ymch"
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInviteEmail(String toEmail, String toName, String inviterName, String inviteToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Convite para WARRIORFOOT");

            String inviteLink = "http://localhost:5173/accept-invite?token=" + inviteToken;

            String htmlContent = buildEmailTemplate(toName, inviterName, inviteLink);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invite email", e);
        }
    }

    private String buildEmailTemplate(String inviteeName, String inviterName, String inviteLink) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                    }
                    .container {
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        padding: 40px;
                        border-radius: 10px;
                        color: white;
                    }
                    .content {
                        background: white;
                        color: #333;
                        padding: 30px;
                        border-radius: 8px;
                        margin-top: 20px;
                    }
                    .button {
                        display: inline-block;
                        padding: 12px 30px;
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                        margin: 20px 0;
                        font-weight: bold;
                    }
                    .footer {
                        margin-top: 20px;
                        font-size: 12px;
                        color: #ddd;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>⚽ WARRIORFOOT</h1>
                    <div class="content">
                        <h2>Olá, %s!</h2>
                        <p><strong>%s</strong> está te convidando para juntar-se a ele no WARRIORFOOT.</p>
                        <p>Clique no botão abaixo para aceitar o convite e começar sua jornada:</p>
                        <a href="%s" class="button">Aceitar Convite</a>
                        <p style="color: #666; font-size: 14px;">
                            Ou copie e cole este link no seu navegador:<br>
                            <code>%s</code>
                        </p>
                        <p style="color: #999; font-size: 12px; margin-top: 30px;">
                            Este convite expira em 7 dias.
                        </p>
                    </div>
                    <div class="footer">
                        <p>Se você não esperava este convite, pode ignorar este e-mail.</p>
                    </div>
                </div>
            </body>
            </html>
            """, inviteeName, inviterName, inviteLink, inviteLink);
    }
}
