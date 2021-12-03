package com.ssportup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ssportup.model.User;
import com.ssportup.service.AuthService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс контроллера регистрации и аутентификации.
 *
 * @author habatoo
 */
@RestController
@RequestMapping(path = "auth")
public class AuthControllerImpl implements AuthController {

    private static final Logger logger = LogManager.getLogger(AuthControllerImpl.class);

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

}
