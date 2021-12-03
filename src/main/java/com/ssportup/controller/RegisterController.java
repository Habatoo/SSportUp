package com.ssportup.controller;

import com.ssportup.model.User;

/**
 * Интерфейс контрактов методов необходимых к реализации для
 * работы с БД при регистрации.
 */
public interface RegisterController {

    /**
     * Создать нового пользователя
     */
    User createUser(User user);

}
