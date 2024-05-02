package clear.solution.userapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import clear.solution.userapp.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    private static final Long FIRST_USER_ID = 1L;
    private static final String USER_EMAIL = "alex@gmail.com";
    private static final String USER_FIRST_NAME = "Alex";
    private static final String USER_LAST_NAME = "Brown";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.of(1988, 3, 10);
    private static final String USER_PHONE_NUMBER = "+380111222333";
    private static final String USER_ADDRESS = "321 Maple St";
    private static final Integer FROM = 45;
    private static final Integer TO = 20;
    private static final String BASE_PATH = "classpath:database/";
    private static final String ADD_ONE_USER = "add-one-user-into-users-table.sql";
    private static final String CREATE_TABLE = "create-table-users.sql";
    private static final String REMOVE_USERS = "delete-users-from-users-table.sql";
    private static User alex;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        alex = new User().setId(FIRST_USER_ID)
                .setEmail(USER_EMAIL)
                .setFirstName(USER_FIRST_NAME)
                .setLastName(USER_LAST_NAME)
                .setBirthDate(USER_BIRTH_DATE)
                .setPhoneNumber(USER_PHONE_NUMBER)
                .setAddress(USER_ADDRESS);
    }

    @Test
    @DisplayName("""
            Verify findById() method
            """)
    @Sql(scripts = {BASE_PATH + CREATE_TABLE, BASE_PATH + ADD_ONE_USER},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BASE_PATH + REMOVE_USERS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ValidEmail_ReturnUser() {
        Optional<User> actual = userRepository.findById(FIRST_USER_ID);
        User expected = alex;
        assertEquals(expected, actual.get());
    }

    @Test
    @DisplayName("""
            Verify getAllByBirthDateAfterAndBirthDateBefore() method
            """)
    @Sql(scripts = {BASE_PATH + CREATE_TABLE, BASE_PATH + ADD_ONE_USER},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BASE_PATH + REMOVE_USERS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllByBirthDateAfterAndBirthDateBefore_ValidInputs_ReturnOneUser() {
        List<User> actual = userRepository.getAllByBirthDateAfterAndBirthDateBefore(LocalDate.now()
                .minusYears(FROM), LocalDate.now().minusYears(TO));
        List<User> expected = List.of(alex);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findByEmail() method
            """)
    @Sql(scripts = {BASE_PATH + CREATE_TABLE, BASE_PATH + ADD_ONE_USER},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BASE_PATH + REMOVE_USERS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByEmail_ValidEmail_ReturnUser() {
        Optional<User> actual = userRepository.findByEmail(USER_EMAIL);
        User expected = alex;
        assertEquals(expected, actual.get());
    }
}
