package com.spring.project.Models;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity


public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String token;
    private Boolean AccountisVerified;
    @OneToMany(mappedBy = "user")
    private Set<SecuredToken> tokens;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public boolean isAccountVerified() {
        return true;
    }
    public void setAccountVerified(boolean  AccountisVerified) {
        this.AccountisVerified=AccountisVerified;


    }
    public void setTokens(Set<SecuredToken> tokens) {
        this.tokens = tokens;
    }

    public void addToken(final SecuredToken token){
        tokens.add(token);
    }

}