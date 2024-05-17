package home.task14.app;

import home.task14.Main;
import home.task14.app.model.City;
import home.task14.app.model.Order;
import home.task14.app.model.Product;
import home.task14.app.model.User;
import home.task14.app.model.UserDetails;
import home.task14.app.model.Vendor;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

public class Util {
    private static SessionFactory _SESSION_FACTORY;
    public final static SessionFactory SESSION_FACTORY_INSTANCE;

    private final static int USERS_QUANTITY = 10;
    private final static int USER_DETAILS_QUANTITY = (int) (USERS_QUANTITY * .6);
    private final static int PRODUCTS_QUANTITY = (int) (USERS_QUANTITY * .6);

    private final static Faker FAKER = new Faker(Locale.getDefault());

    static {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().build();
        _SESSION_FACTORY = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(UserDetails.class)
                .addAnnotatedClass(Vendor.class)
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Order.class)
                .buildMetadata()
                .buildSessionFactory();
        SESSION_FACTORY_INSTANCE = _SESSION_FACTORY;
    }

    public static void initDB() {
        try (
                Session session = SESSION_FACTORY_INSTANCE.openSession()
        ) {
            List<User> users = new ArrayList<>();

            session.getTransaction().begin();
            for (int i = 0; i < USERS_QUANTITY; ++i) {
                User user = createUser();
                session.persist(user);
                users.add(user);
            }
            session.getTransaction().commit();

            List<UserDetails> userDetailsList = new ArrayList<>();
            session.getTransaction().begin();
            for (int i = 0; i < USER_DETAILS_QUANTITY; ++i) {
                User user = users.get(FAKER.random().nextInt(users.size()));
                UserDetails userDetails = createUserDetails(user, session);
                session.persist(userDetails);
                userDetailsList.add(userDetails);
                users.remove(user);
            }
            session.getTransaction().commit();

            session.getTransaction().begin();
            for (int i = 0; i < PRODUCTS_QUANTITY; ++i) {
                Product product = createProduct(session);
                if (checkProduct(product, session)) {
                    session.persist(product);
                }
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            LogManager.getLogger(Main.class).error("Error");
        }
    }

    public static User createUser() {
        User user = User.EMPTY();
        user.setFirstName(FAKER.name().firstName());
        user.setLastName(FAKER.name().lastName());
        user.setEmail(FAKER.internet().emailAddress());
        user.setPhone(FAKER.phoneNumber().phoneNumber());
        return user;
    }

    public static UserDetails createUserDetails(User user, Session session) {
        UserDetails userDetails =  UserDetails.EMPTY();
        userDetails.setUser(user);
        String name = FAKER.address().cityName();
        userDetails.setCity(findOrCreateCity(name, session));
        userDetails.setPostalCode(FAKER.address().postcode());
        userDetails.setStreet(FAKER.address().streetName());
        userDetails.setHouse(FAKER.address().buildingNumber());
        userDetails.setIpn(FAKER.numerify("##########"));
        userDetails.setPassport(FAKER.bothify("??######", true));
        return userDetails;
    }

    public static Product createProduct(Session session) {
        Product product = Product.EMPTY();
        product.setVendor(findOrCreateVendor(FAKER.appliance().brand(), session));
        product.setName(FAKER.appliance().equipment());
        String priceS= FAKER.commerce().price(50, 2500);
        Double price = Double.valueOf(priceS.replace(',', '.'));
        product.setPrice(BigDecimal.valueOf(price).setScale(2));
        return product;
    }

    public static City findOrCreateCity(String cityName, Session session) {
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<City> criteriaQuery = builder.createQuery(City.class);
        Root<City> root = criteriaQuery.from(City.class);
        criteriaQuery.select(root).where(builder.equal(root.get("name"), cityName));
        City city = session.createQuery(criteriaQuery).uniqueResult();
        if (city == null) {
            city = City.of(cityName);
            session.persist(city);
        }
        return city;
    }

    public static Vendor findOrCreateVendor(String vendorName, Session session) {
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Vendor> criteriaQuery = builder.createQuery(Vendor.class);
        JpaRoot<Vendor> root = criteriaQuery.from(Vendor.class);
        criteriaQuery.select(root).where(builder.equal(root.get("name"), vendorName));
        Vendor vendor = session.createQuery(criteriaQuery).getSingleResultOrNull();
        if (vendor == null) {
            vendor = Vendor.of(vendorName);
            session.persist(vendor);
        }
        return vendor;
    }

    public static boolean checkProduct(Product product, Session session) {
        Product result = session
                .createQuery("select p from Product p where p.name = :n", Product.class)
                .setParameter("n", product.getName())
                .getSingleResultOrNull();
        return result == null;
    }
}
