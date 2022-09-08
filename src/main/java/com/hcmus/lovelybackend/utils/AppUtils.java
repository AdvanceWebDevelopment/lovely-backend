package com.hcmus.lovelybackend.utils;

import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.exception.runtime.SendOTPEmailException;
import com.hcmus.lovelybackend.exception.runtime.UserNotFoundException;
import com.hcmus.lovelybackend.repository.UserRepository;
import com.hcmus.lovelybackend.service.impl.RefreshTokenService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class AppUtils {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

//    private String myEmailAddress;

    // TODO: Google disabled "Less secure app access" feature so we can't use the email anymore
//    @Autowired
//    private JavaMailSender mailSender;

//    @Value("${spring.mail.username}")
//    public void setMyEmailAddress(String myEmailAddress) {
//        this.myEmailAddress = myEmailAddress;
//    }

    public String extractTokenFromRequestToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public TokenUser handleTokenCommon(String token){
        String username = jwtUtil.getUsernameFromExpireToken(extractTokenFromRequestToken(token));
        String newToken = refreshTokenService.generateNewTokenIfExpire();

        UserDAO userDAO = userRepository.findByEmail(username);
        if(userDAO == null){
            throw new UserNotFoundException();
        }
        return new TokenUser(newToken, userDAO);
    }

    public void sendVerificationEmail(UserDAO userDAO, String typeEmail) {
        // NOSONAR
//        String toAddress = userDAO.getEmail();
//        String fromAddress = myEmailAddress;
//        String senderName = "HDD Auction";
//        String subject = "Please verify your registration";
//        String content = "Dear [[name]],<br>"
//                + "<h3>This is OTP to verify your [[type]]: <p style=\"font-weight: bold;\">[[OTP]]</p><h3><br><br>"
//                + "Thank you,<br>"
//                + "HDD Auction.";
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        try {
//            helper.setFrom(fromAddress, senderName);
//            helper.setTo(toAddress);
//            helper.setSubject(subject);
//
//            content = content.replace("[[name]]", userDAO.getName());
//
//            content = content.replace("[[OTP]]", userDAO.getVerificationCode());
//
//            content = content.replace("[[type]]", typeEmail);
//
//            helper.setText(content, true);
//
//            mailSender.send(message);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new SendOTPEmailException();
//        }
    }

    public void sendNotifictionEmail(UserDAO userDAO, String contentEmail, Product product) {
        // NOSONAR
//        String toAddress = userDAO.getEmail();
//        String fromAddress = myEmailAddress;
//        String senderName = "HDD Auction";
//        String subject = "Please verify your registration";
//        String content = "Dear [[name]],<br>"
//                + "[[content]]<br>"
//                + "Information:<br>"
//                + "- Product: " + product.getName() +"<br>"
//                + "- Price: " + product.getCurrentPrice() + "<br>"
//                + "Thank you,<br>"
//                + "HDD Auction.";
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        try {
//            helper.setFrom(fromAddress, senderName);
//            helper.setTo(toAddress);
//            helper.setSubject(subject);
//
//            content = content.replace("[[name]]", userDAO.getName());
//
//            content = content.replace("[[content]]", contentEmail);
//
//            helper.setText(content, true);
//
//            mailSender.send(message);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new SendOTPEmailException();
//        }
    }
}
