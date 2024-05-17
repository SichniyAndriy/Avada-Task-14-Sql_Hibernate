package home.task14.app.dao;

import home.task14.app.model.City;
import home.task14.app.model.User;
import home.task14.app.model.UserDetails;
import java.util.List;
import java.util.Optional;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing UserDetailsDaoImp class")
class UserDetailsDaoImplTest {
    private UserDetailsDao userDetailsDao = new UserDetailsDaoImpl();
    private UsersDao usersDao = new UsersDaoImpl();

    private User testUser;
    private UserDetails testUserDetails;

    @BeforeAll
    void init() {
        testUser = User.EMPTY();
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setEmail("test@email.com");
        testUser.setPhone("0123456789");
        usersDao.add(testUser);
    }

    @AfterAll
    void shutdown() {
        usersDao.delete(testUser);
        testUser = null;
    }

    @BeforeEach
    void setUp() {
        testUserDetails = UserDetails.EMPTY();
        testUserDetails.setUser(testUser);
        testUserDetails.setCity(City.of("test_city"));
        testUserDetails.setPostalCode("123456");
        testUserDetails.setStreet("test_street");
        testUserDetails.setHouse("123");
        testUserDetails.setIpn("1234567890");
        testUserDetails.setPassport("OO123456");
        userDetailsDao.add(testUserDetails);
    }

    @AfterEach
    void tearDown() {
        userDetailsDao.delete(testUserDetails);
        testUserDetails = null;
    }

    @Test
    void add() {
        Assertions.assertTrue(testUserDetails.getId() > 0);
    }

    @Test
    void getById() {
        Optional<UserDetails> optional = userDetailsDao.getById(testUserDetails.getId());
        assertTrue(optional.isPresent());
        UserDetails tmpUserDetails = optional.get();
        assertEquals(testUserDetails.getId(), tmpUserDetails.getId());
    }

    @Test
    void getAll() {
        List<UserDetails> userDetailsList = userDetailsDao.getAll();
        assertEquals(1, userDetailsList.size());
    }

    @Test
    void update() {
        String changedStreet = "changed_test_street";
        testUserDetails.setStreet(changedStreet);
        userDetailsDao.update(testUserDetails);
        Optional<UserDetails> optional = userDetailsDao.getById(testUserDetails.getId());
        assertTrue(optional.isPresent());
        UserDetails tmpUserDetails = optional.get();
        assertEquals(changedStreet, tmpUserDetails.getStreet());
    }

    @Test
    void delete() {
        User tmpUser = User.EMPTY();
        tmpUser.setFirstName("2testFirstName");
        tmpUser.setLastName("2testLastName");
        tmpUser.setEmail("2test@email.com");
        tmpUser.setPhone("4_9_88");
        usersDao.add(tmpUser);
        UserDetails tmpUserDetails = UserDetails.EMPTY();
        tmpUserDetails.setUser(tmpUser);
        tmpUserDetails.setCity(City.of("test_city"));
        tmpUserDetails.setPostalCode("123456");
        tmpUserDetails.setStreet("2_test_street");
        tmpUserDetails.setHouse("321");
        tmpUserDetails.setIpn("0987654321");
        tmpUserDetails.setPassport("HH987321");
        userDetailsDao.add(tmpUserDetails);
        List<UserDetails> userDetailsList = userDetailsDao.getAll();
        assertEquals(2, userDetailsList.size());
        userDetailsDao.delete(tmpUserDetails);
        userDetailsList = userDetailsDao.getAll();
        assertEquals(1, userDetailsList.size());
        usersDao.delete(tmpUser);
    }
}
