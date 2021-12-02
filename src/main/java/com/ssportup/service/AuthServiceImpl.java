package com.ssportup.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssportup.repo.AuthRepository;
import com.ssportup.model.User;
import com.ssportup.exception.BusinessLogicException;
import com.ssportup.exception.ErrorCodes;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс конфигурации сервиса.
 *
 * @author habatoo
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);
    private AuthRepository authRepository;

    @Autowired
    public void setAuthRepository(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Returns registered user from database with specified user_id
     * or throws BusinessLogicException
     *
     * @param userId - user id
     * @return User
     */
    @Override
    public User getUserByUserId(String userId) {
        logger.info("method .getUserByUserId userId={}", userId);

        try {
            if (Objects.isNull(userId)) {
                throw new BusinessLogicException("UserId is null", ErrorCodes.ILLEGAL_ARGUMENT.toString());
            }
            User user = authRepository.findUserByUserId(userId);
            if (Objects.isNull(user)) {
                throw new BusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND.toString());
            }
            logger.info("Method .getUserByUserId completed, user was received={}", userId);
            return user;
        } catch (Exception ex) {
            String message = "Failed to .getUserByUserId error={}" + ex.toString();
            logger.error(message, ex);
            throw new BusinessLogicException(message, ex, getErrorCode(ex, ErrorCodes.GET_USER_BY_USER_ID_ERROR));
        }
    }

    /**
     * Returns registered user from database with specified login
     * or throws BusinessLogicException
     *
     * @param login - user login
     * @return User
     */
    @Override
    public User getUserByLogin(String login) {
        logger.info("Method AuthService.getUserByLogin login={}", login);
        User user = authRepository.findUserByLogin(login);
        if (user == null) {
            logger.error("User not found");
            throw new BusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND.toString());
        }
        logger.info("Method AuthService.getUserByTelegramChat completed login={} user={}", login, user);
        return user;
    }

    @Override
    public User getUserByTelegramChat(String chatId) {
        logger.info("Method AuthService.getUserByTelegramChat chatId={}", chatId);
        User user = authRepository.findUserByTelegramChat(chatId);
        if (user == null) {
            logger.error("User not found");
            throw new BusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND.toString());
        }
        logger.info("Method AuthService.getUserByTelegramChat completed chatId={} user={}", chatId, user);
        return user;
    }

    /**
     * Method delete user by id that gets in params
     *
     * @param userId
     */
    @Override
    public void deleteUser(String userId) {

        logger.info("method .deleteUser userId={}", userId);

        try {
            if (Objects.isNull(userId)) {
                throw new BusinessLogicException("UserId is null", ErrorCodes.ILLEGAL_ARGUMENT.toString());
            }
            User user = authRepository.findUserByUserId(userId);
            if (Objects.isNull(user)) {
                throw new BusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND.toString());
            } else {
                authRepository.deleteUserId(userId);
                logger.info("method .deleteUser completed, user={} received", userId);
            }
        } catch (Exception ex) {
            String message = "Failed to .deleteUser error={}" + ex.toString();
            logger.error(message, ex);
            throw new BusinessLogicException(message, ex, getErrorCode(ex, ErrorCodes.GET_USER_BY_USER_ID_ERROR));
        }

    }

    /**
     * Service method of creating user
     *
     * @param user - user,that needs to be created in DB
     * @return Passed user if success creating or throw exception BusinessLogicException.class
     * if USER_NOT_CONSISTENT or USER_ALREADY_EXISTS
     */
    @Override
    public User createUser(User user) {
        logger.info("Method .createUser a={}", user);

        String userId = Objects.nonNull(user.getUserId()) ? user.getUserId() : UUID.randomUUID().toString();
        user.setUserId(userId);

        User userFromTable = authRepository.findUserByUserId(userId);
        if (userFromTable != null) {
            this.throwBusinessLogicException("User already exists", ErrorCodes.USER_ALREADY_EXISTS);
        }

        authRepository.createUser(user);
        logger.info("Method .create completed a={} , user={}", user, user);
        return user;
    }

    /**
     * Service method of updating user
     *
     * @param user - user,that needs to be updated in DB
     * @return Passed user if success updating or throw exception BusinessLogicException.class
     * if USER_NOT_CONSISTENT or USER_NOT_EXISTS
     */
    @Override
    public User updateUser(User user) {
        logger.info("Method .createUser a={}", user);

        User userFromTable = authRepository.findUserByUserId(user.getUserId());
        if (userFromTable == null) {
            this.throwBusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND);
        }

        authRepository.updateUser(user);
        logger.info("Method .update completed  user={}", user);
        return user;
    }

    /**
     * Method gets all user from repository and return list of users
     *
     * @return List<User>
     */
    @Override
    public List<User> getAllUsers() {
        logger.info("method .getAllUsers invoked");
        return authRepository.getAllUsers();
    }

    /**
     * Method gets collection of roles in params and gets all users from repository and return user
     * list only have a role contained in roles collection
     *
     * @param roles
     * @return List<User>
     */
    @Override
    public List<User> getAllUsersByRole(Collection<String> roles) {
        logger.info("method .getAllUsersByRole roles={}", roles);

        try {
            if (Objects.isNull(roles)) {
                throw new BusinessLogicException("Roles are null", ErrorCodes.ILLEGAL_ARGUMENT.toString());
            }

            List<User> usersWithRoles = authRepository.getAllUsers().stream()
                    .filter(user -> userHasAnyRole(user, roles))
                    .collect(Collectors.toList());

            if (usersWithRoles.isEmpty()) {
                throw new BusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND.toString());
            }
            logger.info("Method .getAllUsersByRole completed, users were received={}", roles);
            return usersWithRoles;
        } catch (Exception ex) {
            String message = "Failed to .getAllUsersByRole error={}" + ex.toString();
            logger.error(message, ex);
            throw new BusinessLogicException(message, ex, getErrorCode(ex, ErrorCodes.GET_ALL_USERS_BY_ROLE));
        }
    }

    /**
     * Attach telegram account to user.
     *
     * @param userId - user id
     * @param chatId - chat id
     * @throws BusinessLogicException with code USER_NOT_FOUND
     */
    @Override
    public User attachTelegramChatAccount(String userId, String chatId) {
        logger.info("Method .attachTelegramChatAccount started userId={} chatId={}", userId, chatId);
        try {
            if (Objects.isNull(userId)) {
                throw new BusinessLogicException("UserId is null", ErrorCodes.ILLEGAL_ARGUMENT.toString());
            }
            User user = authRepository.findUserByUserId(userId);
            if (Objects.isNull(user)) {
                throw new BusinessLogicException("User is not found", ErrorCodes.USER_NOT_FOUND.toString());
            }
            user = authRepository.attachTelegramChatAccount(userId, chatId);
            logger.info("Method .attachTelegramChatAccount completed, chat id was installed={}", chatId);
            return user;
        } catch (Exception ex) {
            String message = "Failed to .attachTelegramChatAccount error={}" + ex.toString();
            logger.error(message, ex);
            throw new BusinessLogicException(message, ex, getErrorCode(ex, ErrorCodes.GET_USER_BY_USER_ID_ERROR));
        }
    }

    private String getErrorCode(Exception ex, ErrorCodes defaultCode) {
        if (ex.getClass().equals(BusinessLogicException.class))
            return ((BusinessLogicException) ex).getCode();
        else
            return defaultCode.name();
    }

    private void throwBusinessLogicException(String message, ErrorCodes code) {
        RuntimeException ex = new BusinessLogicException(message, code.toString());
        String logMessage = "Failed to .updateUser error={}" + ex;
        logger.error(logMessage, ex);
        throw ex;
    }

    private boolean userHasAnyRole(User user, Collection<String> roles) {
        return roles.stream().anyMatch(role -> user.getRoles().contains(role));
    }
}
