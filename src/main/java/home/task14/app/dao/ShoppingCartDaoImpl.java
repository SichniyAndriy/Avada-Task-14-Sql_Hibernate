package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.Product;
import home.task14.app.model.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ShoppingCartDaoImpl implements ShoppingCartDao{
    private final Logger logger = LogManager.getLogger(ShoppingCartDaoImpl.class);

    @Override
    public User addProductToUser(User user, Long productId, int quantity) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            User mergedUser = session.merge(user);
            mergedUser.getShoppingCart().put(productId, quantity);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Added product id %d in shopping cart", productId);
            return mergedUser;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error adding product id %d in shopping cart", productId);
            e.printStackTrace();
        }
        return User.EMPTY();
    }

    @Override
    public User deleteInCartByUserAndProduct(User user, Product product) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            user = session.merge(user);
            user.getShoppingCart().remove(product.getId());
            session.getTransaction().commit();
            logger.printf(
                    Level.INFO,
                    "Deleted cart by user id %d and product id %d",
                    user.getId(), product.getId()
            );
            return user;
        } catch (HibernateException e) {
            logger.printf(
                    Level.ERROR,
                    "Error deleting cart by user id %d and product id %d",
                    user.getId(), product.getId()
            );
        }
        return User.EMPTY();
    }

    @Override
    public List<Product> getAllUserProduct(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            List<Product> products = new ArrayList<>();
            session.getTransaction().begin();
            session.merge(user);

            for (var key: user.getShoppingCart().keySet()) {
                Product product = (Product) session
                        .createQuery("from Product where id = :id")
                        .setParameter("id", key)
                        .getSingleResult();
                products.add(product);
            }
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Getting product list by user %d", user.getId());
            return products;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error getting product list by user %d", user.getId());
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public User deleteAllUserProduct(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
           session.getTransaction().begin();
           user = session.merge(user);
           user.getShoppingCart().clear();
           session.getTransaction().commit();
           return user;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error clearing user shopping cart by id %d", user.getId());
            e.printStackTrace();
        }
        return User.EMPTY();
    }
}
