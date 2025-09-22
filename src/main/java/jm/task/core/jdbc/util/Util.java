package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


public final class Util {
    public static final String URL = "jdbc:mysql://localhost:3306/new_schema_project?useSSL=false&serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    private static Connection connection = null;

    private static SessionFactory sessionFactory;

    private Util() {
        throw new UnsupportedOperationException("This is a utilitarian class, instantiation is prohibited");
    }


    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting connection", e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Error closing connection", e);
            }
        }
    }


    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, URL);
                settings.put(Environment.USER, USERNAME);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "create-drop");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (HibernateException e) {
                throw e;
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            try {
                sessionFactory.close();
                sessionFactory = null;
            } catch (HibernateException e) {
                throw e;
            }
        }
    }
}



