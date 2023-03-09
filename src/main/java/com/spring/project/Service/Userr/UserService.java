package com.spring.project.Service.Userr;

import com.spring.project.Models.UserData;
import com.spring.project.Models.UserEntity;
import com.spring.project.Service.Token.InvalidTokenException;
import com.spring.project.Service.Userr.UserAlreadyExists;

public interface UserService {
    void register(final UserData user) throws UserAlreadyExists;
    boolean checkIfUserExist(final String email);
    void sendRegistrationConfirmationEmail(final UserEntity user);
    boolean verifyUser(final String token) throws InvalidTokenException;
    UserEntity getUserById(final String id) throws UnknownIdentifierException;
  //  MfaTokenData mfaSetup(final String email) throws UnkownIdentifierException, QrGenerationException;
}
