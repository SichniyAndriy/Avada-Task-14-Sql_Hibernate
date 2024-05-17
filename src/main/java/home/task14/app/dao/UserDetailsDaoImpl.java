package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.City;
import home.task14.app.model.User;
import home.task14.app.model.UserDetails;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UserDetailsDaoImpl implements UserDetailsDao {
    private final Logger logger = LogManager.getLogger(UserDetails.class);

    @Override
    public long add(UserDetails userDetails) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            User mergedUser = session.merge(userDetails.getUser());
            City mergedCity = Util.findOrCreateCity(userDetails.getCity().getName(), session);
            userDetails.setUser(mergedUser);
            userDetails.setCity(mergedCity);
            session.persist(userDetails);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Added details for user by id %d", userDetails.getUser().getId());
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of adding details for user by id %d", userDetails.getUser().getId());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Optional<UserDetails> getById(long id) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            Optional<UserDetails> optionalUserDetails = Optional.ofNullable(session.get(UserDetails.class, id));
            logger.printf(Level.INFO, "Got user details by id %d", id);
            return optionalUserDetails;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error getting user details by id %d", id);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<UserDetails> getAll() {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            List<UserDetails> details = session
                    .createQuery("select ud from UserDetails ud", UserDetails.class)
                    .getResultList();
            logger.info("Got user details list");
            return details;
        } catch (HibernateException e) {
            logger.error("Error getting user details list");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean update(UserDetails userDetails) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            UserDetails updated = session.merge(UserDetails.class.toString(), userDetails);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Update user details by id %d", userDetails.getId());
            return !updated.equals(userDetails);
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of updating user details by id %d", userDetails.getId());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(UserDetails userDetails) {
        try(
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            session.remove(userDetails);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Deleted user details by id %d", userDetails.getId());
            return true;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error deleting user details by id %d", userDetails.getId());
            e.printStackTrace();
        }
        return false;
    }
}
