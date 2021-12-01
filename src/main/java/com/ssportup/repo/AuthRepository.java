package com.ssportup.repo;

import com.ssportup.model.User;

import java.util.List;

public interface AuthRepository {

    /**
     * Получить информацию о пользователе
     */
    User findUserByUserId(String userId);

    /**
     * Получить информацию о пользователе
     */
    User findUserByLogin(String login);

    /**
     * Получить информацию о пользователе по chat id
     */
    User findUserByTelegramChat(String chatId);

    /**
     * Удалить информацию о пользователе
     */
    void deleteUserId(String userId);

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
     * Приявязать аккаунт телеграма к пользователю.
     * @param userId - user id
     * @param chatId - chat id
     */
    User attachTelegramChatAccount(String userId, String chatId);
}
