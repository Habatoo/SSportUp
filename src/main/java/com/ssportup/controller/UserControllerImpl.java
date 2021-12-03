package com.ssportup.controller;

import com.ssportup.model.User;
import com.ssportup.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс контроллера работы с пользователем.
 *
 * @author habatoo
 */
@RestController
@RequestMapping(path = "auth")
public class UserControllerImpl implements UserController {

    private static final Logger logger = LogManager.getLogger(AuthControllerImpl.class);

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * @method info - при http GET запросе по адресу .../auth/info
     * возвращает строку.
     * @return объект типа {@link String} в виде "UserController " и время и дату.
     */
    @GetMapping("info")
    public String info() {
        logger.info("method .info invoked");
        return "UserController " + new Date();
    }

    /**
     * @method getUserByUserId - при http GET запросе по адресу .../auth/user/byUserId
     * возвращает объект пользователя.
     * Перенаправляет запрос на @method authService для поиска пользователя по параметру userID
     * или throws BusinessLogicException.
     *
     * @param userId - user id пользователя
     * @return объект типа {@link User}
     */
    @Override
    @GetMapping("/user/byUserId")
    public User getUserByUserId(@RequestParam("userId") String userId) {
        return authService.getUserByUserId(userId);
    }

    /**
     * @method getUserByUserId - при http GET запросе по адресу .../auth/user/byLogin
     * возвращает объект пользователя.
     * Перенаправляет запрос на @method authService для поиска пользователя по параметру userName
     * или throws BusinessLogicException.
     *
     * @param userName user login
     * @return объект типа {@link User}
     */
    @Override
    @GetMapping("/user/byUserName")
    public User getUserByUserName(@RequestParam("userName") String userName) {
        logger.info("Method AuthController.getUserByUserName userName={}", userName);
        User user = authService.getUserByLogin(userName);
        logger.info("Method AuthController.getUserByUserName completed userName={} user={}" , userName, user);
        return user;
    }

    /**
     * @method getUserByTelegramChat - при http GET запросе по адресу .../auth/user/byTelegramChat
     * возвращает объект пользователя.
     * Перенаправляет запрос на @method authService для поиска пользователя по параметру chatId
     * или throws BusinessLogicException.
     *
     * @param chatId user chatId id пользователя в телеграмм
     * @return объект типа {@link User}
     */
    @Override
    @GetMapping("/user/byTelegramChat")
    public User getUserByTelegramChat(@RequestParam("chatId") String chatId) {
        logger.info("Method AuthController.getUserByTelegramChat chatId={}", chatId);
        User user = authService.getUserByTelegramChat(chatId);
        logger.info("Method AuthController.getUserByTelegramChat completed chatId={} user={}" , chatId, user);
        return user;
    }

    /**
     * @method deleteUser - при http DELETE запросе по адресу .../auth/user/{userId}
     * Перенаправляет запрос на @method authService для поиска пользователя по параметру userid
     * и удалению или throws BusinessLogicException.
     *
     * @param userid user chatId id пользователя в телеграмм
     * @return объект типа {@link User}
     */
    @Override
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable("userId") String userid) {
        authService.deleteUser(userid);
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
     * @return возвращает объект типа {@link List<User>}
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
