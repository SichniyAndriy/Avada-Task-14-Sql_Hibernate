package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.Order;
import home.task14.app.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class OrdersDaoImpl implements OrdersDao {
    private final Logger logger = LogManager.getLogger(OrdersDaoImpl.class);

    @Override
    public Long addOrder(Order order) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            session.persist(order);
            session.getTransaction().commit();
            logger.printf(Level.INFO, "Added new order by id %d", order.getId());
            return order.getId();
        } catch (HibernateException e) {
            logger.printf(Level.ERROR, "Error of adding new order by id %d", order.getId());
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public List<Order> getOrderByUser(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(builder.equal(root.get("user"), user));
            List<Order> resultList = session.createQuery(query).getResultList();
            logger.info("Get order list by user");
            return resultList;
        } catch (HibernateException e) {
            logger.error("Error getting list by user");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<Order> getAllOrder() {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            List<Order> resultList = session.createQuery("from Order o", Order.class).getResultList();
            logger.info("Get order list");
            return resultList;
        } catch (HibernateException e) {
            logger.error("Error getting order list");
            e.printStackTrace();
        }
        return List.of();
    }

    public void deleteOrderByUser(User user) {
        try (
                Session session = Util.SESSION_FACTORY_INSTANCE.openSession()
        ) {
            session.getTransaction().begin();
            session.createMutationQuery("delete from Order o where user = ?1")
                    .setParameter(1, user)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
