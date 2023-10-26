package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.enums.EmailType;
import com.hugorithm.hopfencraft.exception.email.EmailSendingFailedException;
import com.hugorithm.hopfencraft.model.*;
import com.hugorithm.hopfencraft.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Async
    protected void sendEmail(String to, String subject, String email, ApplicationUser user, EmailType emailType) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("no-reply@hopfencraft.com");

            mailSender.send(mimeMessage);

            emailRepository.save(new Email(emailType, LocalDateTime.now(), user));

        } catch (MessagingException | MailSendException ex) {
            throw new EmailSendingFailedException("Email sending failed", ex);
        }
    }

    private String buildPaypalPaymentSuccessEmail(String username, Order order) {
        StringBuilder orderItemsDetails = new StringBuilder();

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();

            orderItemsDetails.append("<strong>Brand:</strong> ")
                    .append(product.getBrand())
                    .append("<br>\n")
                    .append("<strong>Product:</strong> ")
                    .append(product.getName())
                    .append("<br>\n")
                    .append(orderItem.getQuantity())
                    .append(" x €")
                    .append(product.getPrice())
                    .append(" = €")
                    .append(orderItem.getTotal())
                    .append("<br><br>\n\n");
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Payment Success at HopfenCraft</title>\n" +
                "    <style>\n" +
                "        /* Reset email styles */\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f7f7f7;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        /* Container */\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        /* Header */\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 20px 0;\n" +
                "        }\n" +
                "\n" +
                "        .header h1 {\n" +
                "            color: #333333;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "\n" +
                "        /* Content */\n" +
                "        .content {\n" +
                "            display: flex;\n" +
                "            flex-wrap: wrap;\n" +
                "            justify-content: center;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 4px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        .message {\n" +
                "            width: 100%;\n" +
                "            font-size: 16px;\n" +
                "            color: #333333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .product-details {\n" +
                "            width: 100%;\n" +
                "            font-size: 16px;\n" +
                "            color: #333333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        /* Creative Image */\n" +
                "        .creative-image {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .creative-image img {\n" +
                "            max-width: 100%;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "\n" +
                "        /* Footer */\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding-top: 30px;\n" +
                "            color: #888888;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Payment Success at HopfenCraft</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p class=\"message\">Hi <strong>" + username + "</strong>,</p>\n" +
                "            <p class=\"message\">Your payment was successful for the following product:</p>\n" +
                "            <div class=\"product-details\">\n" +
                orderItemsDetails +
                "            </div>\n" +
                "            <p class=\"message\"><strong>Total:</strong> €" + order.getTotal() + "</p>" +
                "            <p class=\"message\">Thank you for your purchase. Enjoy your beers! Cheers! 🍻</p>\n" +
                "            <div class=\"creative-image\">\n" +
                "                <!--img src=\"file.png\" alt=\"HopfenCraft Illustration\"-->\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you have any questions or need further assistance, feel free to <a href=\"mailto:support@hopfencraft.com\">contact us</a>.</p>\n" +
                "            <p>&copy; " + LocalDateTime.now().getYear() + " HopfenCraft</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }


    private String buildWelcomeEmail(String username) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Welcome to HopfenCraft</title>\n" +
                "    <style>\n" +
                "        /* Reset email styles */\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f7f7f7;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        /* Container */\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        /* Header */\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 20px 0;\n" +
                "        }\n" +
                "\n" +
                "        .header h1 {\n" +
                "            color: #333333;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "\n" +
                "        /* Content */\n" +
                "        .content {\n" +
                "            display: flex;\n" +
                "            flex-wrap: wrap;\n" +
                "            justify-content: center;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 4px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        .message {\n" +
                "            width: 100%;\n" +
                "            font-size: 16px;\n" +
                "            color: #333333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        /* Creative Image */\n" +
                "        .creative-image {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .creative-image img {\n" +
                "            max-width: 100%;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "\n" +
                "        /* Footer */\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding-top: 30px;\n" +
                "            color: #888888;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Welcome to HopfenCraft</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p class=\"message\">Hi <strong>" + username + "</strong>,</p>\n" +
                "            <p class=\"message\">Thank you for joining HopfenCraft! Explore our website and discover a world of amazing products and services. If you have any questions or need assistance, don't hesitate to contact our support team.</p>\n" +
                "            <div class=\"creative-image\">\n" +
                "                <!--img src=\"file.png\" alt=\"HopfenCraft Illustration\"-->\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you have any questions or need assistance, feel free to <a href=\"mailto:support@hopfencraft.com\">contact us</a>.</p>\n" +
                "            <p>&copy; " + LocalDateTime.now().getYear() + " HopfenCraft</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String buildPasswordResetEmail(String username, String link) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Password Reset</title>\n" +
                "    <style>\n" +
                "        /* Reset email styles */\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f7f7f7;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        /* Container */\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        /* Header */\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding: 20px 0;\n" +
                "        }\n" +
                "\n" +
                "        .header h1 {\n" +
                "            color: #333333;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "\n" +
                "        /* Content */\n" +
                "        .content {\n" +
                "            display: flex;\n" +
                "            flex-wrap: wrap;\n" +
                "            justify-content: center;" +
                "            background-color: #ffffff;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 4px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        .message {\n" +
                "            width: 100%;\n" +
                "            font-size: 16px;\n" +
                "            color: #333333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        /* Button */\n" +
                "        .cta-button {\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #007BFF;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "            transition: background-color 0.3s ease;\n" +
                "        }\n" +
                "\n" +
                "        .cta-button:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "\n" +
                "        /* Footer */\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding-top: 30px;\n" +
                "            color: #888888;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Password Reset</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p class=\"message\">Hi <strong>" + username + "</strong>,</p>\n" +
                "            <p class=\"message\">You recently requested to reset your password. Click the button below to reset it:</p>\n" +
                "            <a class=\"cta-button\" href=\"" + link + "\">Reset Password</a>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you didn't request this, you can ignore this email.</p>\n" +
                "            <p>&copy; " + LocalDateTime.now().getYear() + " HopfenCraft</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public void sendWelcomeEmail(ApplicationUser user) {
        try {
            String subject = "Welcome to HopfenCraft - Your Registration Was Successful!";
            String username = getUserUsernameBasedOnAuthProvider(user);
            String message = buildWelcomeEmail(username);
            sendEmail(user.getEmail(), subject, message, user, EmailType.REGISTRATION);
        } catch (EmailSendingFailedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public void sendPasswordResetEmail(ApplicationUser user, String link) {
        try {
            String subject = "Password Reset Request - HopfenCraft";
            String username = getUserUsernameBasedOnAuthProvider(user);
            String message = buildPasswordResetEmail(username, link);
            sendEmail(user.getEmail(), subject, message, user, EmailType.PASSWORD_RESET);
        } catch (EmailSendingFailedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public void sendPaypalPaymentSuccessEmail(ApplicationUser user, Order order) {
        try {
            String subject = "Order Payment Confirmation - Thank You for Shopping at HopfenCraft!";
            String username = getUserUsernameBasedOnAuthProvider(user);
            String message = buildPaypalPaymentSuccessEmail(username, order);
            sendEmail(user.getEmail(), subject, message, user, EmailType.ORDER);
        } catch (EmailSendingFailedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private String getUserUsernameBasedOnAuthProvider(ApplicationUser user) {
        return user.getAuthProvider().equals(AuthProvider.LOCAL) ? user.getUsername() : user.getFirstName();
    }
}