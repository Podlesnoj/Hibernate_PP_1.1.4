package jm.task.core.jdbc;


import java.util.logging.Logger;
import jm.task.core.jdbc.util.Util;
import jm.task.core.jdbc.service.UserServiceImpl;



public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Ivan", "Ivanov", (byte) 25);
        userService.saveUser("Petr", "Petrov", (byte) 30);
        userService.saveUser("Svetlana", "Sidorova", (byte) 28);


        logger.info("Все пользователи:");

        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.shutdown();
    }

}