package com.ssportup.controller;

import com.ssportup.model.User;
import com.ssportup.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс контроллера регистрации.
 *
 * @author habatoo
 */
@RestController
@RequestMapping(path = "auth")
public class RegisterControllerImpl implements RegisterController {

    private static final Logger logger = LogManager.getLogger(AuthControllerImpl.class);

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * @method createUser - при http POST запросе по адресу .../auth/user/
     * Перенаправляет запрос на @method authService с сформированным объектом типа {@link User}
     *
     * @param user объект типа {@link User}
     * @return объект типа {@link User}
     */
    @PostMapping("/user")
    @Override
    public User createUser(@RequestBody User user) {
        logger.info("Method .createUser a={}", user);
        User result = authService.createUser(user);
        logger.info("Method .createUser completed a={} , user={}", user, result);
        return result;
    }

}
