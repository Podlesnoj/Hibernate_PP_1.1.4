package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());
    private final Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection();
        logger.info("Инициализирован UserDaoJDBCImpl с соединением к БД");
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50)," +
                "lastName VARCHAR(50)," +
                "age TINYINT)";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users успешно создана или уже существует");
        } catch (SQLException e) {
            logger.severe("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users успешно удалена");
        } catch (SQLException e) {
            logger.severe("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            logger.info("Пользователь " + name + " " + lastName + " успешно сохранен в БД");
        } catch (SQLException e) {
            logger.severe("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Пользователь с ID " + id + " успешно удален");
            } else {
                logger.warning("Пользователь с ID " + id + " не найден");
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, lastname, age FROM users"; // исправил lastName на lastname
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                User user = new User(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("lastName"), resultSet.getByte("age"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get the list of users: " + e);
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users успешно очищена");
        } catch (SQLException e) {
            logger.severe("Ошибка при очистке таблицы: " + e.getMessage());
        }
    }
}