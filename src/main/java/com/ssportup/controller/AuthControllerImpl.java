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
 * Класс конфигурации контроллера.
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

    @GetMapping("info")
    public String info() {
        logger.info("method .info invoked");
        return "AuthController " + new Date();
    }

    /**
     * Gets Parameter (userID) by REST
     * Redirects request to authService to find user in database with specified userID
     * or throws BusinessLogicException
     *
     * @param userId - user id
     * @return User
     */
    @Override
    @GetMapping("/user/byUserId")
    public User getUserByUserId(@RequestParam("userId") String userId) {
        return authService.getUserByUserId(userId);
    }

    /**
     * Gets Parameter (login) by REST
     * Redirects request to authService to find user in database with specified login
     * or throws BusinessLogicException
     *
     * @param login - user login
     * @return User
     */
    @Override
    @GetMapping("/user/byLogin")
    public User getUserByLogin(@RequestParam("login") String login) {
        logger.info("Method AuthController.getUserByLogin login={}", login);
        User user = authService.getUserByLogin(login);
        logger.info("Method AuthController.getUserByLogin completed login={} user={}" , login, user);
        return user;
    }

    @Override
    @GetMapping("/user/byTelegramChat")
    public User getUserByTelegramChat(@RequestParam("chatId") String chatId) {
        logger.info("Method AuthController.getUserByTelegramChat chatId={}", chatId);
        User user = authService.getUserByTelegramChat(chatId);
        logger.info("Method AuthController.getUserByTelegramChat completed chatId={} user={}" , chatId, user);
        return user;
    }

    @Override
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable("userId") String userid) {
        authService.deleteUser(userid);
    }

    /**
     * Method creates user in database and returns created user.
     *
     * @return User
     */
    @PostMapping("/user")
    @Override
    public User createUser(@RequestBody User user) {
        logger.info("Method .createUser a={}", user);
        User result = authService.createUser(user);
        logger.info("Method .createUser completed a={} , user={}", user, result);
        return result;
    }


    /**
     * Method updates user in database and returns updated user.
     *
     * @return User
     */
    @PutMapping("/user")
    @Override
    public User updateUser(@RequestBody User user) {
        logger.info("Method .updateUser a={}", user);
        User result = authService.updateUser(user);
        logger.info("Method .updateUser completed a={} , user={}", user, result);
        return result;
    }

    /**
     * Method gets all user from repository and return list of users
     *
     * @return List<User>
     */
    @Override
    @GetMapping("/user/all")
    public List<User> getAllUsers() {
        logger.info("method .getAllUsers invoked");
        return authService.getAllUsers();
    }

    /**
     * Method take a collection of roles in parameters and return list of user with this roles
     *
     * @param roles
     * @return List<User>
     */
    @Override
    @GetMapping("/user/all/byRoles")
    public List<User> getAllUsersByRole(@RequestParam("roles") String roles) {
        Set<String> rolesSet = Arrays.stream(roles.split(",")).collect(Collectors.toSet());
        return authService.getAllUsersByRole(rolesSet);
    }

    /**
     * Привязать telegram аккаунт к пользователю.
     *
     * @param userId - user id
     * @param chatId - chatId
     */
    @Override
    @PutMapping("/user/{userId}/telegram/attach")
    public User attachTelegramChatAccount(@PathVariable("userId") String userId,
                                          @RequestParam("chatId") String chatId) {
        return authService.attachTelegramChatAccount(userId, chatId);
    }
}
