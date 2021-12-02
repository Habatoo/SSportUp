package com.ssportup.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
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
    private String firstName;
    private String lastName;
    private String secondName;
    private LocalDate birthDate;
    private String login;
    private String password;
    private String telegramChatId;
    private Set<String> roles;

    private User(Builder builder) {
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.secondName = builder.secondName;
        this.birthDate = builder.birthDate;
        this.login = builder.login;
        this.password = builder.password;
        this.telegramChatId = builder.telegramChatId;
        this.roles = builder.roles;
    }

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public static class Builder {
        private String userId;
        private String firstName;
        private String lastName;
        private String secondName;
        private LocalDate birthDate;
        private String login;
        private String password;
        private String telegramChatId;
        private Set<String> roles;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setTelegramChatId(String telegramChatId) {
            this.telegramChatId = telegramChatId;
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
        return userId.equals(user.userId) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(secondName, user.secondName) && Objects.equals(birthDate, user.birthDate) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(telegramChatId, user.telegramChatId) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, secondName, birthDate, login, password, telegramChatId, roles);
    }
}
