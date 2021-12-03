package com.ssportup.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Класс конфигурации пользователя.
 *
 * @author habatoo
 */
@Table(	name = "users")
public class User {
    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String telegramChatId;
    private LocalDateTime userCreationDate;
    private LocalDateTime lastVisitedDate;
    private Set<String> roles;

    private User(Builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userEmail = builder.userEmail;
        this.userPassword = builder.userPassword;
        this.telegramChatId = builder.telegramChatId;
        this.userCreationDate = builder.userCreationDate;
        this.lastVisitedDate = builder.lastVisitedDate;
        this.roles = builder.roles;
    }

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public LocalDateTime getUserCreationDate() {
        return userCreationDate;
    }

    public void setUserCreationDate(LocalDateTime userCreationDate) {
        this.userCreationDate = userCreationDate;
    }

    public LocalDateTime getLastVisitedDate() {
        return lastVisitedDate;
    }

    public void setLastVisitedDate(LocalDateTime lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public static class Builder {
        private String userId;
        private String userName;
        private String userPassword;
        private String userEmail;
        private String telegramChatId;
        private LocalDateTime userCreationDate;
        private LocalDateTime lastVisitedDate;
        private Set<String> roles;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }

        public Builder setUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder setTelegramChatId(String telegramChatId) {
            this.telegramChatId = telegramChatId;
            return this;
        }

        public Builder setUserCreationDate(LocalDateTime userCreationDate) {
            this.userCreationDate = userCreationDate;
            return this;
        }

        public Builder setLastVisitedDate(LocalDateTime lastVisitedDate) {
            this.lastVisitedDate = lastVisitedDate;
            return this;
        }

        public Builder setRoles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userEmail, user.userEmail) &&
                Objects.equals(userPassword, user.userPassword) &&
                Objects.equals(telegramChatId, user.telegramChatId) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                userId,
                userName,
                userPassword,
                userEmail,
                telegramChatId,
                roles);
    }
}
