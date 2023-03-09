package com.spring.project.Service.Userr;

import com.spring.project.Service.Token.InvalidTokenException;
import com.spring.project.Service.Userr.UnknownIdentifierException;

public interface CustumorAccountService {
    void forgottenPassword(String username) throws UnknownIdentifierException;
    void updatePassword(final String password, final String token) throws InvalidTokenException, UnknownIdentifierException;
    boolean loginDisabled(final String username);
}
