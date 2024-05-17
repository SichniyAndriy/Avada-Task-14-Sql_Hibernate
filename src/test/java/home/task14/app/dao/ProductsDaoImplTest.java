package home.task14.app.dao;

import home.task14.app.model.Product;
import home.task14.app.model.Vendor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing ProductsDaoImpl class")
class ProductsDaoImplTest {
    private ProductsDao productsDao = new ProductsDaoImpl();
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.EMPTY();
        testProduct.setName("test_product");
        testProduct.setVendor(Vendor.of("test_vendor"));
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        productsDao.add(testProduct);
    }

    @AfterEach
    void tearDown() {
        productsDao.delete(testProduct);
        testProduct = null;
    }

    @Test
    void add() {
        Assertions.assertTrue(testProduct.getId() > 0);
    }

    @Test
    void getById() {
        Optional<Product> optional = productsDao.getById(testProduct.getId());
        Assertions.assertTrue(optional.isPresent());
        Product tmpProduct = optional.get();
        Assertions.assertTrue(testProduct.equals(tmpProduct));
    }

    @Test
    void getAll() {
        List<Product> productList = productsDao.getAll();
        Assertions.assertEquals(1, productList.size());
    }

    @Test
    void update() {
        testProduct.setPrice(BigDecimal.valueOf(199.99));
        productsDao.update(testProduct);
        Optional<Product> optional = productsDao.getById(testProduct.getId());
        Assertions.assertTrue(optional.isPresent());
        Product tmpProduct = optional.get();
        Assertions.assertEquals(BigDecimal.valueOf(199.99), tmpProduct.getPrice());
    }

    @Test
    void delete() {
        Product tmpProduct = Product.EMPTY();
        tmpProduct.setName("test_product_2");
        tmpProduct.setVendor(Vendor.of("test_vendor_2"));
        tmpProduct.setPrice(BigDecimal.valueOf(00.00));
        productsDao.add(tmpProduct);
        List<Product> productList = productsDao.getAll();
        Assertions.assertEquals(2, productList.size());
        productsDao.delete(tmpProduct);
        productList = productsDao.getAll();
        Assertions.assertEquals(1, productList.size());
    }
}
