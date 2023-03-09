package com.spring.project.Service.Token;

import com.spring.project.Models.SecuredToken;
import com.spring.project.Repository.SecureTokenRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Service
public class DefaultSecureTokenService implements SecureTokenService {
    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    @Value("${jdj.secure.token.validity}")
    private int tokenValidityInSeconds;
    @Autowired
    SecureTokenRepository secureTokenRepository;
    @Override
    public SecuredToken createSecureTokens() {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        SecuredToken secureToken = new SecuredToken();
        secureToken.setToken(tokenValue);
        secureToken.setExpiredAt(LocalDateTime.now().plusSeconds(getTokenValidityInSeconds()));

        this.saveSecureToken(secureToken);
        return secureToken;
    }



    @Override
    public void saveSecureToken(SecuredToken token) {
        secureTokenRepository.save(token);

    }

    @Override
    public SecuredToken findByToken(String token) {
        return secureTokenRepository.findByToken(token);
    }

    @Override
    public void removeToken(SecuredToken token) {
        secureTokenRepository.delete(token);
    }

    @Override
    public void removeTokenByToken(String token) {
        secureTokenRepository.removeByToken(token);
    }
    public int getTokenValidityInSeconds(){
        return tokenValidityInSeconds;
    }
}
