package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.User;
import home.task14.app.model.UserDetails;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UsersDaoImpl implements UsersDao {
    private final Logger logger = LogManager.getLogger(UsersDaoImpl.class);

    @Override
    public long add(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Saved user by id %d", user.getId());
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of saving %s %s", user.getFirstName(), user.getLastName());
            e.printStackTrace();
        }
        return user.getId();
    }

    @Override
    public Optional<User> getById(long id) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            Optional<User> optionalUser = Optional.ofNullable(session.get(User.class, id));
            logger.printf(Level.INFO, "Get user by id %d", id);
            return optionalUser;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of getting user by id %d", id);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            List<User> users = session.createQuery("select u from User u", User.class).getResultList();
            logger.info("Got user list");
            return users;
        } catch (HibernateException e) {
            logger.error("Error getting user list");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean update(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            User updatedUser = session.merge(User.class.toString(), user);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Update user by id %d", user.getId());
            return updatedUser.equals(user);
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error updating user by id %d", user.getId());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            UserDetails userDetails = session.get(UserDetails.class, user.getId());
            if (userDetails != null) {
                session.remove(userDetails);
            }
            session.getTransaction().begin();
            session.remove(user);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Removed user by id %d", user.getId());
            return true;
        } catch(HibernateException e) {
            logger.printf(Level.ERROR, "Error of deleting user by id %d", user.getId());
            e.printStackTrace();
        }
        return false;
    }
}
