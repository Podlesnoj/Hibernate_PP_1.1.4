package jm.task.core.jdbc.dao;

import java.util.List;
import jm.task.core.jdbc.model.User;

public interface UserDao {

    void createUsersTable();

    void dropUsersTable();

    void saveUser(String name, String lastName, byte age);

    void removeUserById(long id);

    List<User> getAllUsers();

    void cleanUsersTable();
}
