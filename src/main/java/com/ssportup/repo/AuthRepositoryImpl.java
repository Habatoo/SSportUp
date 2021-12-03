package com.ssportup.repo;

import com.ssportup.exception.BusinessLogicException;
import com.ssportup.exception.ErrorCodes;
import com.ssportup.model.User;
import com.ssportup.service.AuthServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс конфигурации репозитория по работе с аутентификацией.
 *
 * @author habatoo
 */
public class AuthRepositoryImpl implements AuthRepository {

    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);
    private final String[] fields = {
            "user_id",
            "user_name",
            "user_password",
            "user_email",
            "telegram_chat_id",
            "user_creation_date",
            "last_visited_date",
            "roles"
    };
    private final Connection connection;
    @Value("${auth.default_role}")
    private String defaultRole;

    public AuthRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns registered user from database with specified userId
     * or null if user does not exist
     *
     * @param userId - user id
     * @return User or null
     */
    @Override
    public User findUserByUserId(String userId) {
        String query = "Select " + fieldList() + " from users where user_id = ?";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = createUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /**
     * Returns registered user from database with specified login
     * or null if user does not exist
     *
     * @param userName - user login
     * @return User or null
     */
    @Override
    public User findUserByUserName(String userName) {
        String query = String.format("SELECT * FROM users WHERE user_name ='%s' COLLATE NOCASE", userName);
        try {
            return runQuery(query);
        } catch (SQLException ex) {
            throw new BusinessLogicException("Failed in AuthRepository.findUserByLogin method", ex,
                    ErrorCodes.ILLEGAL_ARGUMENT.toString());
        }
    }

    /**
     * Returns registered user from database with specified chat id
     * or null if user does not exist
     *
     * @param telegramChatId - user login
     * @return User or null
     */
    @Override
    public User findUserByTelegramChatId(String telegramChatId) {
        String query = String.format("SELECT * FROM users WHERE telegram_chat_id ='%s'", telegramChatId);
        try {
            return runQuery(query);
        } catch (SQLException ex) {
            throw new BusinessLogicException("Failed find user .findUserByTelegramChat", ex,
                    ErrorCodes.ILLEGAL_ARGUMENT.toString());
        }
    }

    /**
     * Метод выполнения запросов в ResultSet
     *
     * @param query запрос на выполнение
     * @return объект User
     * @throws SQLException
     */
    private User runQuery(String query) throws SQLException {
        User.Builder builder = new User.Builder();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            builder.setUserId(resultSet.getString("user_id"));
            builder.setUserName(resultSet.getString("user_name"));
            builder.setUserPassword(resultSet.getString("user_password"));
            builder.setUserEmail(resultSet.getString("user_email"));
            builder.setTelegramChatId(resultSet.getString("telegram_chat_id"));
            builder.setUserCreationDate(LocalDateTime.parse(resultSet.getString("user_creation_date")));
            builder.setLastVisitedDate(LocalDateTime.parse(resultSet.getString("last_visited_date")));
            String[] arrayRoles = resultSet.getString("roles").split(",");
            Set<String> roles = new HashSet<>();
            for (String s : arrayRoles) {
                roles.add(s);
            }
            builder.setRoles(roles);
            return builder.build();
        } else {
            throw new BusinessLogicException("User not found",
                    ErrorCodes.USER_NOT_FOUND.toString());
        }
    }

    /**
     * Method delete user by id that gets in params
     *
     * @param userId
     */
    @Override
    public void deleteUserId(String userId) {
        String query = "DELETE FROM users where user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод обновляет данные через PreparedStatement
     *
     * @param user
     * @param query
     * @throws SQLException
     */
    private void setUserStatement(User user, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        // statement.setString(1, user.getUserId());
        statement.setString(2, user.getUserName());
        statement.setString(3, user.getUserPassword());
        statement.setString(4, user.getUserEmail());
        statement.setString(5, user.getTelegramChatId());
        statement.setString(6, user.getUserCreationDate().toString());
        statement.setString(7, user.getLastVisitedDate().toString());
        statement.setString(8, getRolesForQuery(user));
        statement.executeUpdate();
    }

    /**
     * Dao method of creating a new user in DB
     *
     * @param user - what user needs to be created
     * @return Passed user if success creating or throw exception BusinessLogicException.class
     * if PreparedStatement.executeUpdate is failed
     */
    @Override
    public User createUser(User user) {
        logger.info("Method .createUser a={}", user);
        final String query = "INSERT INTO users VALUES (? ,? ,? ,? ,? ,? ,? ,?);";

        try {
            setUserStatement(user, query);
        } catch (SQLException ex) {
            String message = "Failed to .createUser error={}" + ex;
            logger.error(message, ex);
            throw new BusinessLogicException(ex.getMessage(), ex, ErrorCodes.ILLEGAL_STATE.toString());
        }

        logger.info("Method .createUser completed user={}", user);
        return user;
    }

    /**
     * DAO method of updating user data
     *
     * @param user - what user needs to be updated
     * @return Passed user if success updating or throw exception BusinessLogicException.class
     * if PreparedStatement.executeUpdate is failed
     */
    @Override
    public User updateUser(User user) {
        logger.info("Method .updateUser a={}", user);
        final String query = "UPDATE users \n" +
                "SET user_name = ?,\n" +
                "user_password = ?,\n" +
                "user_email = ?,\n" +
                "telegram_chat_id = ?,\n" +
                "user_creation_date = ?,\n" +
                "last_visited_date = ?,\n" +
                "roles = ?\n" +
                "WHERE user_id = ?;";

        try {
            setUserStatement(user, query);
        } catch (SQLException ex) {
            String message = "Failed to .updateUser error={}" + ex;
            logger.error(message, ex);
            throw new BusinessLogicException(ex.getMessage(), ex, ErrorCodes.ILLEGAL_STATE.toString());
        }
        logger.info("Method .updateUser completed user={}", user);
        return user;
    }

    /**
     * Возвращает список всех пользователей из БД
     *
     * @return
     */
    @Override
    public List<User> getAllUsers() {
        String query = "Select " + fieldList() + " from users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User attachTelegramChatAccount(String userId, String telegramChatId) {
        String query = "UPDATE \"users\"" + "SET telegram_chat_id = "
                + "'" + telegramChatId + "'" + "WHERE user_id = " + "'" + userId + "'";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findUserByUserId(userId);
    }

    @PreDestroy
    public void onClose() {
        try {
            connection.close();
        } catch (Exception ignored) {
        }
    }

    private String getRolesForQuery(User user) {
        Set<String> roles = user.getRoles();

        if (roles == null || roles.isEmpty()) {
            return defaultRole;
        }

        return user.getRoles().stream()
                .collect(Collectors.joining(","));
    }

    private String fieldList() {
        return String.join(", ", fields);
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User.Builder userBuilder = new User.Builder();
        User user = userBuilder
                .setUserId(resultSet.getString("user_id"))
                .setUserName(resultSet.getString("user_name"))
                .setUserPassword(resultSet.getString("user_password"))
                .setUserEmail(resultSet.getString("user_email"))
                .setTelegramChatId(resultSet.getString("telegram_chat_id"))
                .setUserCreationDate(LocalDateTime.parse(resultSet.getString("user_creation_date")))
                .setLastVisitedDate(LocalDateTime.parse(resultSet.getString("last_visited_date")))
                .setRoles(getRolesFromResultSet(resultSet))
                .build();
        return user;
    }

    private Set<String> getRolesFromResultSet(ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("roles");

        if (value == null) {
            return null;
        }

        return Arrays.stream(value.split(","))
                .collect(Collectors.toSet());
    }
}
