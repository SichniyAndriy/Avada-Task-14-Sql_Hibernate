package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.Order;
import home.task14.app.model.User;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing OrdersDaoImpl class")
class OrdersDaoImplTest {
    private OrdersDao ordersDao = new OrdersDaoImpl();
    private UsersDao usersDao = new UsersDaoImpl();

    private Order testOrder;
    private User testUser;

    @BeforeAll
    void init() {
        testUser = Util.createUser();
        usersDao.add(testUser);
        testOrder = Order.EMPTY();
        testOrder.setUser(testUser);
        testOrder.setDescription("test order description");
        testOrder.setTotalPrice(BigDecimal.valueOf(9999.99));
        ordersDao.addOrder(testOrder);
    }

    @AfterAll
    void shutdown() {
        usersDao.delete(testUser);
    }

    @Test
    void addOrder() {
        List<Order> orderList = ordersDao.getAllOrder();
        Assertions.assertEquals(1, orderList.size());
    }

    @Test
    void getOrderByUser() {
        List<Order> orderByUser = ordersDao.getOrderByUser(testUser);
        Assertions.assertEquals(1, orderByUser.size());
    }

    @Test
    void getAllOrder() {
        List<Order> orderList = ordersDao.getAllOrder();
        Assertions.assertEquals(1, orderList.size());
    }
}
