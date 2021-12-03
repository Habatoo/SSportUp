package com.ssportup.controller;

import com.ssportup.model.User;

import java.util.List;

/**
 * Интерфейс контрактов методов необходимых к реализации для
 * работы с БД при работе с пользователем.
 */
public interface UserController {

    /**
     * Получить информацию о пользователе по userId
     */
    User getUserByUserId(String userId);

    /**
     * Получить информацию о пользователе по userName
     */
    User getUserByUserName(String userName);

    /**
     * Получить информацию о пользователе по chat id
     */
    User getUserByTelegramChat(String chatId);

    /**
     * Удалить информацию о пользователе
     */
    void deleteUser(String userId);

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
