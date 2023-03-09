package com.spring.project.Service.Token;

import com.spring.project.Models.SecuredToken;
import org.springframework.stereotype.Service;


public interface SecureTokenService {
    SecuredToken createSecureTokens();
    void saveSecureToken(final SecuredToken token);
    SecuredToken findByToken(final String token);
    void removeToken(final SecuredToken token);
    void removeTokenByToken(final String token);
}
