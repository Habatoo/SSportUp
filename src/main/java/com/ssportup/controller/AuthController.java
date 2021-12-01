package com.ssportup.controller;

import com.ssportup.model.User;

import java.util.List;

public interface AuthController {

    /**
     * Получить информацию о пользователе по userId
     */
    User getUserByUserId(String userId);

    /**
     * Получить информацию о пользователе по login
     */
    User getUserByLogin(String login);

    /**
     * Получить информацию о пользователе по chat id
     */
    User getUserByTelegramChat(String chatId);

    /**
     * Удалить информацию о пользователе
     */
    void deleteUser(String userId);

    /**
     * Создать нового пользователя
     */

    User createUser(User user);

    /**
     * Обновить информацию о существующем пользователе
     */
    User updateUser(User user);

    /**
     * Получить всех пользователей
     */
    List<User> getAllUsers();

    /**
     * Получить всех пользователей по данным ролям
     */
    List<User> getAllUsersByRole(String roles);

    /**
     * Привязать telegram аккаунт к пользователю.
     *
     * @param userId - user id
     * @param chatId - chatId
     */
    User attachTelegramChatAccount(String userId, String chatId);
}
