package home.task14.app.dao;

import home.task14.app.model.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Testing UsersDaoImpl class")
class UsersDaoImplTest {
    private final UsersDao usersDao = new UsersDaoImpl();
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.EMPTY();
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setEmail("test@email.com");
        testUser.setPhone("0123456789");
        usersDao.add(testUser);
    }

    @AfterEach
    void tearDown() {
        usersDao.delete(testUser);
        testUser = null;
    }

    @Test
    void add() {
        assertTrue(testUser.getId() > 0);
    }

    @Test
    void getById() {
        Long testId = testUser.getId();
        Optional<User> optional = usersDao.getById(testId);
        assertTrue(optional.isPresent());
        User user = optional.get();
        assertEquals(testId, user.getId());
    }

    @Test
    void getAll() {
        List<User> userList = usersDao.getAll();
        assertTrue(userList.size() > 0);
    }

    @Test
    void update() {
        String tmp_FName = "changed_f_name";
        String tmp_LName = "changed_l_name";
        testUser.setFirstName(tmp_FName);
        testUser.setLastName(tmp_LName);
        usersDao.update(testUser);
        Optional<User> optional = usersDao.getById(testUser.getId());
        assertTrue(optional.isPresent());
        User user = optional.get();
        assertEquals(tmp_FName, user.getFirstName());
        assertEquals(tmp_LName, user.getLastName());
    }

    @Test
    void delete() {
        User tmpUser = User.EMPTY();
        tmpUser.setFirstName("testFirstName");
        tmpUser.setLastName("testLastName");
        tmpUser.setEmail("test@email.com");
        tmpUser.setPhone("0123456789");
        usersDao.add(tmpUser);
        Long id = tmpUser.getId();
        usersDao.delete(tmpUser);
        Optional<User> optional = usersDao.getById(id);
        Assertions.assertFalse(optional.isPresent());
    }
}
