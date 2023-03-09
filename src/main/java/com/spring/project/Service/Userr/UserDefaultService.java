package com.spring.project.Service.Userr;

import com.spring.project.Models.SecuredToken;
import com.spring.project.Models.UserData;
import com.spring.project.Models.UserEntity;
import com.spring.project.Repository.SecureTokenRepository;
import com.spring.project.Repository.UserRepository;

import com.spring.project.Service.Email.AccountVerificationEmailContext;
import com.spring.project.Service.Email.EmailService;
import com.spring.project.Service.Token.DefaultSecureTokenService;
import com.spring.project.Service.Token.InvalidTokenException;
import com.spring.project.Service.Token.SecureTokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;


@Service("userService")
public class UserDefaultService implements UserService {

    @Value("${site.base.url.http}")
    private String baseURL;

@Autowired
private UserRepository userRepository;
@Autowired
private  PasswordEncoder passwordEncoder;
@Resource
private SecureTokenService secureTokenService;
@Resource
DefaultSecureTokenService defaultSecureTokenService;
@Resource
    SecureTokenRepository secureTokenRepository;
@Resource
EmailService emailService;
    @Override
    public void register(UserData user) throws UserAlreadyExists {
        if(checkIfUserExist(user.getEmail())){
            throw new UserAlreadyExists("User already exists for this email");
        } UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        encodePassword(userEntity, user);
        userRepository.save(userEntity);
        sendRegistrationConfirmationEmail(userEntity);


    }

    @Override
    public boolean checkIfUserExist(String email) {

        return userRepository.findByEmail(email) !=null ? true : false;
    }

    @Override
    public void sendRegistrationConfirmationEmail(UserEntity user) {
     SecuredToken secureToken= secureTokenService.createSecureTokens();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
          emailService.SendEmail(emailContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean verifyUser(String token) throws InvalidTokenException {
        SecuredToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        UserEntity user = userRepository.getOne(secureToken.getUser().getId());
        if(Objects.isNull(user)){
            return false;
        }
        user.setAccountVerified(true);

        userRepository.save(user); // let's same user details

        // we don't need invalid password now
        secureTokenService.removeToken(secureToken);
        return true;
    }

    @Override
    public UserEntity getUserById(String id) throws UnknownIdentifierException {
        return null;
    }

    private void encodePassword( UserEntity userEntity, UserData user){
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
