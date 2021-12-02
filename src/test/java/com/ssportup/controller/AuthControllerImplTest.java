package com.ssportup.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ssportup.model.User;
import com.ssportup.repo.AuthRepositoryImpl;
import liquibase.pro.packaged.S;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс AuthControllerImplTest проводит тестирование публичных методов
 * контроллера {@link AuthControllerImpl}
 *
 * @author habatoo
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class AuthControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthControllerImpl authController;

    @Autowired
    AuthRepositoryImpl authRepository;

    private User user;
    private String requestJson;
    private Set<String> roles;

    /**
     * Инициализация экземпляров тестируемого класса {@link User}.
     */
    @BeforeEach
    void setUp() throws JsonProcessingException {
        roles = new HashSet<String>();
        roles.add("user");

        user = new User.Builder()
                .setUserId("ooooo-ooooo")
                .setFirstName("name")
                .setLastName("last")
                .setSecondName("second")
                .setLogin("login")
                .setPassword("12345")
                .setTelegramChatId("t-name")
                .setRoles(roles)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson = ow.writeValueAsString(user);
    }

    /**
     * Очистка экземпляров тестируемого класса {@link User}.
     */
    @AfterEach
    void tearDown() {
        user = null;
    }

    /**
     * Метод тестирует инициализацию контекста.
     * Сценарий проверяет успешность создания
     * {@link AuthRepositoryImpl}
     * {@link AuthControllerImpl}
     */
    @Test
    public void loadControllers_Test() {
        assertThat(authRepository).isNotNull();
        assertThat(authController).isNotNull();
    }

    @Test
    void setAuthService() {
    }

    @Test
    void info() {
    }

    @Test
    void getUserByUserId() {
    }

    @Test
    void getUserByLogin() {
    }

    @Test
    void getUserByTelegramChat() {
    }

    /**
     * Метод тестирует удаление объекта {@link User}.
     * при запросе типа DELETE по адресу "/auth/user/{id}"
     * где id - индекс id удаляемого объекта.
     * Сценарий проверяет возможность удаление пользователя с id = "ooooo-ooooo"
     * пользователем с ролью (user).
     */
    @Test
    void deleteUser() throws Exception {
        createUser();

        this.mockMvc.perform(delete("/auth/user/" + user.getUserId()))
                .andExpect(status().isOk());
    }

    /**
     * Метод тестирует создание объекта {@link User}.
     * при запросе типа POST по адресу "/auth/user/"
     * где id - индекс id удаляемого объекта.
     * Сценарий проверяет возможность создания пользователя с id = "ooooo-ooooo"
     * с ролью (user).
     */
    @Test
    void createUser() throws Exception {
        this.mockMvc.perform(post("/auth/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getAllUsersByRole() {
    }

    @Test
    void attachTelegramChatAccount() {
    }
}