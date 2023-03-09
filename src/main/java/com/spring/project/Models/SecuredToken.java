package com.spring.project.Models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "secureTokens")
public class SecuredToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private String token;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp timestamp;
    @Column(updatable = false)
    @Basic(optional = false)
    private LocalDateTime expiredAt;
    @ManyToOne
    @JoinColumn(name = "costum_id",referencedColumnName = "id")
    private UserEntity user;
    @Transient
    private boolean isExpired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isExpired() {
        return getExpiredAt().isBefore(LocalDateTime.now());
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

}
