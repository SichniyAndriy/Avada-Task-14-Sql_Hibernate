package home.task14.app.service;

import home.task14.app.Util;
import home.task14.app.dao.OrdersDaoImpl;
import home.task14.app.dao.ProductsDao;
import home.task14.app.dao.ProductsDaoImpl;
import home.task14.app.dao.UsersDao;
import home.task14.app.dao.UsersDaoImpl;
import home.task14.app.model.Order;
import home.task14.app.model.Product;
import home.task14.app.model.User;
import home.task14.app.model.Vendor;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersServiceTest {
    private OrdersService ordersService = new OrdersService();

    private UsersDao usersDao = new UsersDaoImpl();
    private ProductsDao productsDao = new ProductsDaoImpl();
    private OrdersDaoImpl ordersDao = new OrdersDaoImpl();

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testUser = Util.createUser();
        usersDao.add(testUser);
        testProduct = Product.EMPTY();
        testProduct.setName("test_product");
        testProduct.setVendor(Vendor.of("test_vendor"));
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        productsDao.add(testProduct);
        testUser.getShoppingCart().put(testProduct.getId(), 1);
    }

    @AfterEach
    void tearDown() {
        ordersDao.deleteOrderByUser(testUser);
        productsDao.delete(testProduct);
        usersDao.delete(testUser);
        testUser = null;
        testProduct = null;
    }

    @Test
    void makeOrder() {
        ordersService.makeOrder(testUser);
        List<Order> orderByUser = ordersDao.getOrderByUser(testUser);
        Assertions.assertEquals(1, orderByUser.size());
        Assertions.assertEquals(1, testUser.getShoppingCart().size());

    }
}
