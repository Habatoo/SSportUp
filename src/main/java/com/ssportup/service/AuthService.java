package com.ssportup.service;

import com.ssportup.exception.BusinessLogicException;
import com.ssportup.model.User;

import java.util.Collection;
import java.util.List;

public interface AuthService {

    /**
     * Получить информацию о пользователе по userId
     *
     * @throws {@link BusinessLogicException} with code USER_NOT_FOUND if user doesn't exist
     * @throws {@link BusinessLogicException} with code ILLEGAL_ARGUMENT if userId is empty or null
     */
    User getUserByUserId(String userId);

    /**
     * Получить информацию о пользователе по login
     *
     * @throws {@link BusinessLogicException} with code USER_NOT_FOUND if user doesn't exist
     * @throws com.ssportup.exception.BusinessLogicException with code ILLEGAL_ARGUMENT if login is empty or null
     */
    User getUserByLogin(String login);

    /**
     * Получить информацию о пользователе по chat id
     *
     * @throws {@link BusinessLogicException} with code USER_NOT_FOUND if user doesn't exist
     * @throws {@link BusinessLogicException} with code ILLEGAL_ARGUMENT if chat id is empty or null
     */
    User getUserByTelegramChat(String chatId);

    /**
     * Удалить информацию о пользователе
     *
     * @throws {@link BusinessLogicException} with code USER_NOT_FOUND if user doesn't exist
     * @throws {@link BusinessLogicException} with code ILLEGAL_ARGUMENT if userId is empty or null
     */
    void deleteUser(String userId);

    /**
     * Создать нового пользователя
     *
     * @throws {@link BusinessLogicException} with code USER_ALREADY_EXISTS if user with id exists
     * @throws {@link BusinessLogicException} with code DUPLICATE_LOGIN if user with login exists
     * @throws {@link BusinessLogicException} with code ILLEGAL_ARGUMENT if request contains wrong data
     */
    User createUser(User user);

    /**
     * Обновить информацию о существующем пользователе
     *
     * @throws {@link BusinessLogicException} with code USER_NOT_FOUND if user doesn't exist
     * @throws {@link BusinessLogicException} with code DUPLICATE_LOGIN if user with login exists
     * @throws {@link BusinessLogicException} with code ILLEGAL_ARGUMENT if request contains wrong data
     */
    User updateUser(User user);

    /**
     * Получить всех пользователей
     */
    List<User> getAllUsers();

    /**
     * Получить всех пользователей по данным ролям
     *
     * @throws {@link BusinessLogicException} with code ILLEGAL_ARGUMENT if roles is null
     */
    List<User> getAllUsersByRole(Collection<String> roles);

    /**
     * Привязать telegram аккаунт к пользователю.
     *
     * @param userId - user id
     * @param chatId - chat id
     */
    User attachTelegramChatAccount(String userId, String chatId);
}
