package jm.task.core.jdbc.dao;

import java.util.List;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;


    public UserDaoHibernateImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();

    }
    @Override
    public void createUsersTable() {
        Transaction trans = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("CREATE TABLE IF NOT EXISTS User (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50), " +
                    "lastName VARCHAR(50), " +
                    "age TINYINT)");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (trans != null) {
                trans.rollback();
                throw e;
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction trans = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("DROP TABLE IF EXISTS User");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (trans != null) {
                trans.rollback();
                throw e;
            }
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction trans = null;
        try (Session session = sessionFactory.openSession()) {
            trans = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            trans.commit();
        } catch (Exception e) {
            if (trans != null)
                trans.rollback();
                throw e;

        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction trans = null;
        try (Session session = sessionFactory.openSession()) {
            trans = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                trans.commit();
            }
        } catch (Exception e) {
            if (trans != null) {
                trans.rollback();
                throw e;
            }
        }
    }


    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }



    @Override
    public void cleanUsersTable() {
        Transaction trans = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("TRUNCATE TABLE User");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (trans != null) {
                trans.rollback();
                throw e;
            }
        }
    }
}