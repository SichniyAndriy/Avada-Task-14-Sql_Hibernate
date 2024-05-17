package home.task14.app.service;

import home.task14.app.dao.OrdersDao;
import home.task14.app.dao.OrdersDaoImpl;
import home.task14.app.dao.ProductsDao;
import home.task14.app.dao.ProductsDaoImpl;
import home.task14.app.dao.ShoppingCartDao;
import home.task14.app.dao.ShoppingCartDaoImpl;
import home.task14.app.dao.UsersDao;
import home.task14.app.dao.UsersDaoImpl;
import home.task14.app.model.Order;
import home.task14.app.model.Product;
import home.task14.app.model.User;
import java.math.BigDecimal;
import java.util.Optional;

public class OrdersService {
    private final OrdersDao ordersDao = new OrdersDaoImpl();
    private final ProductsDao productsDao = new ProductsDaoImpl();
    private final UsersDao usersDao = new UsersDaoImpl();
    private final ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();

    public Long makeOrder(User user) {
        Order order = formOrder(user);
        shoppingCartDao.deleteAllUserProduct(user);
        return saveOrder(order);
    }

    private Order formOrder(User user) {
        StringBuilder description = new StringBuilder();
        usersDao.getById(user.getId());
        BigDecimal sum = BigDecimal.ZERO;
        for (var entry: user.getShoppingCart().entrySet()) {
            Optional<Product> optional = productsDao.getById(entry.getKey());
            if (optional.isPresent()) {
                Product product = optional.get();
                sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
                description.append(product.getName()).append(" - ").append(entry.getValue()).append("\n");
            }
        }
        Order order = Order.EMPTY();
        order.setUser(user);
        order.setDescription(description.toString());
        order.setTotalPrice(sum);
        return order;
    }

    private long saveOrder(Order order) {
        return ordersDao.addOrder(order);
    }
}
