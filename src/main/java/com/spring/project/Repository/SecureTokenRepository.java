package com.spring.project.Repository;

import com.spring.project.Models.SecuredToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecuredToken,Long> {
    SecuredToken findByToken(final String token);
    Long removeByToken(String token );

}
