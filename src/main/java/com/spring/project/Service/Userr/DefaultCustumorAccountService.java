package com.spring.project.Service.Userr;

import com.spring.project.Models.ForgotPasswordEmailContext;
import com.spring.project.Models.SecuredToken;
import com.spring.project.Models.UserEntity;
import com.spring.project.Repository.SecureTokenRepository;
import com.spring.project.Repository.UserRepository;
import com.spring.project.Service.Email.EmailService;
import com.spring.project.Service.Token.InvalidTokenException;
import com.spring.project.Service.Token.SecureTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Objects;

@Service
public class DefaultCustumorAccountService implements CustumorAccountService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SecureTokenRepository secureTokenRepository;
    @Resource
    EmailService emailService;
    @Resource
    private UserService userService;
    @Resource
    private SecureTokenService secureTokenService;
    private String baseURL;

    @Override
    public void forgottenPassword(String username) throws UnknownIdentifierException {
        UserEntity user= userService.getUserById(username);
        sendResetPasswordEmail(user);
    }

    @Override
    public void updatePassword(String password, String token) throws InvalidTokenException, UnknownIdentifierException {
        SecuredToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        UserEntity user = userRepository.getOne(secureToken.getUser().getId());
        if(Objects.isNull(user)){
            throw new UnknownIdentifierException("unable to find user for the token");
        }
        secureTokenService.removeToken(secureToken);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean loginDisabled(String username) {
        return false;
    }


    protected void sendResetPasswordEmail(UserEntity user) {
        SecuredToken secureToken= secureTokenService.createSecureTokens();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.SendEmail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
