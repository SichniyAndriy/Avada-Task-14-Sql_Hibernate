package home.task14;

import home.task14.app.Util;
import home.task14.app.dao.ProductsDao;
import home.task14.app.dao.ProductsDaoImpl;
import home.task14.app.dao.ShoppingCartDao;
import home.task14.app.dao.ShoppingCartDaoImpl;
import home.task14.app.dao.UsersDao;
import home.task14.app.dao.UsersDaoImpl;
import home.task14.app.model.Product;
import home.task14.app.model.User;
import home.task14.app.service.OrdersService;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import net.datafaker.Faker;

public class Main {
    private static Faker faker = new Faker();

    public static void main(String[] args) {
        Util.initDB();

        UsersDao usersDao = new UsersDaoImpl();
        ProductsDao productDao = new ProductsDaoImpl();
        ShoppingCartDao shoppingCartDao = new ShoppingCartDaoImpl();
        List<Product> products = productDao.getAll();
        List<User> users = usersDao.getAll();
        Queue<User> userQueue = new ArrayDeque<>();

        int lenI = faker.random().nextInt(50, 100);
        for (int i = 0; i < lenI; ++i) {
            User user = users.get(faker.random().nextInt(users.size()));
            users.remove(user);
            int lenJ = faker.random().nextInt(1, 4);
            for (int j = 0; j < lenJ; ++j) {
                Product product = products.get(faker.random().nextInt(products.size()));
                user = shoppingCartDao.addProductToUser(user, product.getId(), faker.random().nextInt(1, 4));
            }
            userQueue.offer(user);
        }

        OrdersService ordersService = new OrdersService();
        while (!userQueue.isEmpty()) {
            ordersService.makeOrder(userQueue.poll());
        }
    }
}
