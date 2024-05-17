package home.task14.app.dao;

import home.task14.app.model.Order;
import home.task14.app.model.User;
import java.util.List;

public interface OrdersDao {
    Long addOrder(Order order);

    List<Order> getOrderByUser(User user);

    List<Order> getAllOrder();
}
