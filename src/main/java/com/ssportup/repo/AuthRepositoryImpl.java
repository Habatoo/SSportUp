package com.ssportup.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import com.ssportup.model.User;
import com.ssportup.exception.BusinessLogicException;
import com.ssportup.exception.ErrorCodes;
import com.ssportup.service.AuthServiceImpl;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            "first_name",
            "last_name",
            "second_name",
            "birth_date",
            "login",
            "password",
            "telegram_chat_id",
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
        String query = "Select " + fieldList() + " from user where user_id = ?";
        User user = null;
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
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
     * @param login - user login
     * @return User or null
     */
    @Override
    public User findUserByLogin(String login) {
        String query = String.format("SELECT * FROM user WHERE login ='%s' COLLATE NOCASE", login);

        User.Builder builder = new User.Builder();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                builder.setUserId(resultSet.getString("user_id"));
                builder.setFirstName(resultSet.getString("first_name"));
                builder.setLastName(resultSet.getString("last_name"));
                builder.setSecondName(resultSet.getString("second_name"));
                builder.setBirthDate(LocalDate.parse(resultSet.getString("birth_date")));
                builder.setLogin(resultSet.getString("login"));
                builder.setPassword(resultSet.getString("password"));
                builder.setTelegramChatId(resultSet.getString("telegram_chat_id"));
                String[] arrayRoles = resultSet.getString("roles").split(",");
                Set<String> roles = new HashSet<>();
                for (String s : arrayRoles) {
                    roles.add(s);
                }
                builder.setRoles(roles);
                return builder.build();
            } else {
                throw new BusinessLogicException("User not found", ErrorCodes.USER_NOT_FOUND.toString());
            }
        } catch (SQLException ex) {
            throw new BusinessLogicException("Failed in AuthRepository.findUserByLogin method", ex, ErrorCodes.ILLEGAL_ARGUMENT.toString());
        }
    }

    /**
     * Returns registered user from database with specified chat id
     * or null if user does not exist
     *
     * @param chatId - user login
     * @return User or null
     */
    @Override
    public User findUserByTelegramChat(String chatId) {
        String query = String.format("SELECT * FROM user WHERE telegram_chat_id ='%s'", chatId);

        User.Builder builder = new User.Builder();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                builder.setUserId(resultSet.getString("user_id"));
                builder.setFirstName(resultSet.getString("first_name"));
                builder.setLastName(resultSet.getString("last_name"));
                builder.setSecondName(resultSet.getString("second_name"));
                builder.setBirthDate(LocalDate.parse(resultSet.getString("birth_date")));
                builder.setLogin(resultSet.getString("login"));
                builder.setPassword(resultSet.getString("password"));
                builder.setTelegramChatId(resultSet.getString("telegram_chat_id"));
                String[] arrayRoles = resultSet.getString("roles").split(",");
                Set<String> roles = new HashSet<>();
                for (String s : arrayRoles) {
                    roles.add(s);
                }
                builder.setRoles(roles);
                return builder.build();
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new BusinessLogicException("Failed find user .findUserByTelegramChat", ex, ErrorCodes.ILLEGAL_ARGUMENT.toString());
        }
    }

    /**
     * Method delete user by id that gets in params
     *
     * @param userId
     */
    @Override
    public void deleteUserId(String userId) {
        String query = "DELETE FROM user  where user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Dao method of creating a new user in DB
     *
     * @param user - what user needs to be created
     * @return Passed user if success creating or throw exception BusinessLogicException.class if PreparedStatement.executeUpdate is failed
     */
    @Override
    public User createUser(User user) {
        logger.info("Method .createUser a={}", user);
        final String query = "INSERT INTO user VALUES (? ,? ,? ,? ,? ,? ,? ,? ,?);";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getSecondName());
            statement.setString(5, getBirthDateForQuery(user));
            statement.setString(6, user.getLogin());
            statement.setString(7, user.getPassword());
            statement.setString(8, user.getTelegramChatId());
            statement.setString(9, getRolesForQuery(user));
            statement.executeUpdate();
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
     * @return Passed user if success updating or throw exception BusinessLogicException.class if PreparedStatement.executeUpdate is failed
     */
    @Override
    public User updateUser(User user) {
        logger.info("Method .updateUser a={}", user);
        final String query = "UPDATE user \n" +
                "SET first_name = ?,\n" +
                "last_name = ?,\n" +
                "second_name = ?,\n" +
                "birth_date = ?,\n" +
                "login = ?,\n" +
                "password = ?,\n" +
                "telegram_chat_id = ?,\n" +
                "roles = ?\n" +
                "WHERE user_id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getSecondName());
            statement.setString(4, getBirthDateForQuery(user));
            statement.setString(5, user.getLogin());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getTelegramChatId());
            statement.setString(8, this.getRolesForQuery(user));
            statement.setString(9, user.getUserId());
            statement.executeUpdate();
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
        String query = "Select " + fieldList() + " from user";
        List<User> users = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User attachTelegramChatAccount(String userId, String chatId) {
        String query = "UPDATE \"user\"" + "SET telegram_chat_id = " + "'" + chatId + "'" + "WHERE user_id = " + "'" + userId + "'";
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

    private String getBirthDateForQuery(User user) {
        LocalDate localDate = user.getBirthDate();

        if (localDate == null) {
            return null;
        }

        return localDate.toString();
    }

    private String fieldList() {
        return String.join(", ", fields);
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User.Builder userBuilder = new User.Builder();
        User user = userBuilder
                .setUserId(resultSet.getString("user_id"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setSecondName(resultSet.getString("second_name"))
                .setBirthDate(getBirthDayFromResultSet(resultSet))
                .setLogin(resultSet.getString("login"))
                .setPassword(resultSet.getString("password"))
                .setTelegramChatId(resultSet.getString("telegram_chat_id"))
                .setRoles(getRolesFromResultSet(resultSet))
                .build();
        return user;
    }

    private LocalDate getBirthDayFromResultSet(ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("birth_date");

        if (value == null) {
            return null;
        }

        try {
            LocalDate localDate = LocalDate.parse(value);
            return localDate;
        } catch (DateTimeParseException exception) {
            return null;
        }
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
