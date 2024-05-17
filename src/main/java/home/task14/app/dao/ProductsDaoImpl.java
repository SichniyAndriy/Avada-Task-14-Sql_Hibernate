package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.Product;
import home.task14.app.model.Vendor;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ProductsDaoImpl implements ProductsDao {
    Logger logger = LogManager.getLogger(ProductsDaoImpl.class);

    @Override
    public long add(Product product) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            Vendor vendor = Util.findOrCreateVendor(product.getVendor().getName(), session);
            product.setVendor(vendor);
            session.persist(product);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Added product by id %d", product.getId());
            return product.getId();
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of adding product by id %d", product.getId());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Optional<Product> getById(long id) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            Optional<Product> optional = Optional.ofNullable(session.get(Product.class, id));
            logger.printf(Level.INFO, "Got product by id %d", id);
            return optional;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of getting product by id %d", id);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getAll() {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            List<Product> products = session
                    .createQuery("select p from Product p", Product.class)
                    .getResultList();
            logger.info("Got product list");
            return products;
        } catch (HibernateException e) {
            logger.error("Error getting product list");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean update(Product product) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            Product updated = session.merge(Product.class.toString(), product);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Updated product by id %d", product.getId());
            return !updated.equals(product);
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error updating product by id %d", product.getId());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Product product) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            session.remove(product);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Deleted product by id %d", product.getId());
            return true;
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error deleting product by id %d", product.getId());
            e.printStackTrace();
        }
        return false;
    }
}
