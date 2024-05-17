package home.task14.app.dao;

import home.task14.app.model.Product;
import home.task14.app.model.User;
import java.util.List;

public interface ShoppingCartDao {
    User addProductToUser(User user, Long productId, int quantity);

    User deleteInCartByUserAndProduct(User user, Product product);

    List<Product> getAllUserProduct(User user);

    User deleteAllUserProduct(User user);
}
