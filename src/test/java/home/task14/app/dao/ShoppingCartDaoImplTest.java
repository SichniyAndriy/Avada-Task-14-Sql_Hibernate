package home.task14.app.dao;

import home.task14.app.Util;
import home.task14.app.model.Product;
import home.task14.app.model.User;
import home.task14.app.model.Vendor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing ShoppingCartDaoImpl class")
class ShoppingCartDaoImplTest {
    private ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
    private UsersDao usersDao = new UsersDaoImpl();
    private ProductsDao productsDao = new ProductsDaoImpl();
    private User testUser;
    private Set<Product> testProducts = new HashSet<>();

    @BeforeAll
    void init() {
        testUser = Util.createUser();
        usersDao.add(testUser);
        for (int i = 0; i < 2; ++i) {
            Product product = Product.EMPTY();
            product.setName("test_prod_" + (i + 1));
            product.setVendor(Vendor.of("test_vendor_" + (i + 1)));
            product.setPrice( BigDecimal.valueOf((i + 1) * 100) );
            productsDao.add(product);
            testProducts.add(product);
        }
    }

    @AfterAll
    void shutdown() {
        usersDao.delete(testUser);
        for (var p: productsDao.getAll()){
            productsDao.delete(p);
        }
        testProducts.clear();
        testProducts = null;
        testUser = null;
    }

    @BeforeEach
    void setUp() {
        int quantity = 0;
        for (var product: testProducts) {
            testUser = shoppingCartDao.addProductToUser(testUser, product.getId(), ++quantity);
        }
    }

    @AfterEach
    void tearDown() {
        testUser.getShoppingCart().clear();
    }

    @Test
    void addProductToUser() {
        Product tmpProduct = Product.EMPTY();
        tmpProduct.setName("test_prod_3");
        tmpProduct.setVendor(Vendor.of("test_vendor_3"));
        tmpProduct.setPrice(BigDecimal.valueOf(999.99));
        productsDao.add(tmpProduct);
        testUser = shoppingCartDao.addProductToUser(testUser, tmpProduct.getId(), 3);
        Assertions.assertEquals(3, testUser.getShoppingCart().size());
    }

    @Test
    void deleteInCartByUserAndProduct() {
        Product tmpProduct = productsDao.getById(2).get();
        testUser = shoppingCartDao.deleteInCartByUserAndProduct(testUser, tmpProduct);
        assertEquals(1, testUser.getShoppingCart().size());
    }

    @Test
    void getAllUserProduct() {
        List<Product> allUserProduct = shoppingCartDao.getAllUserProduct(testUser);
        assertEquals(2, allUserProduct.size());
    }

    @Test
    void deleteAllUserProduct() {
        testUser = shoppingCartDao.deleteAllUserProduct(testUser);
        Assertions.assertEquals(0,  testUser.getShoppingCart().size());
    }
}
