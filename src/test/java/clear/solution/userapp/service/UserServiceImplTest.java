package clear.solution.userapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import clear.solution.userapp.dto.UserRequestDto;
import clear.solution.userapp.dto.UserResponseDto;
import clear.solution.userapp.dto.UserSearchRequestDto;
import clear.solution.userapp.dto.UserUpdateRequestDto;
import clear.solution.userapp.mapper.UserMapper;
import clear.solution.userapp.model.User;
import clear.solution.userapp.repository.UserRepository;
import clear.solution.userapp.service.impl.UserServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private static final Long ALEX_ID = 1L;
    private static final String ALEX_FIRST_NAME = "Alex";
    private static final String ALEX_LAST_NAME = "Brown";
    private static final String ALEX_EMAIL = "alex@gmail.com";
    private static final String ALEX_PHONE_NUMBER = "+380111222333";
    private static final String ALEX_ADDRESS = "321 Maple St";
    private static final LocalDate ALEX_BIRTH_DATE = LocalDate.of(1988, 3, 10);
    private static final LocalDate FROM = LocalDate.now().minusYears(45);
    private static final LocalDate TO = LocalDate.now().minusYears(20);
    private static final String SOPHIA_PHONE_NUMBER = "+380444555666";
    private static final String SOPHIA_EMAIL = "sophia@gmail.com";
    private static final String SOPHIA_FIRST_NAME = "Sophia";
    private static final String SOPHIA_LAST_NAME = "Johnson";
    private static final LocalDate SOPHIA_BIRTH_DATE = LocalDate.of(1995, 7, 20);
    private static final String SOPHIA_ADDRESS = "654 Pine St";
    private static final String WILLIAM_EMAIL = "william@gmail.com";
    private static final String WILLIAM_FIRST_NAME = "William";
    private static final LocalDate WILLIAM_BIRTH_DATE = LocalDate.of(1979, 11, 25);
    private static final String ADULT = "adultAge";
    private static UserRequestDto alexRequestDto;
    private static User alex;
    private static UserRequestDto sophiaRequestDto;
    private static User sophia;
    private static UserResponseDto sophiaResponseDto;
    private static UserResponseDto alexResponseDto;
    private static UserUpdateRequestDto alexUpdateRequestDto;
    private static UserSearchRequestDto userSearchRequestDto;

    @Value("${adult.age}")
    private int minAge;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeAll
    static void beforeAll() {
        alex = new User().setFirstName(ALEX_FIRST_NAME)
                .setLastName(ALEX_LAST_NAME)
                .setAddress(ALEX_ADDRESS)
                .setEmail(ALEX_EMAIL)
                .setBirthDate(ALEX_BIRTH_DATE)
                .setPhoneNumber(ALEX_PHONE_NUMBER)
                .setId(ALEX_ID);

        alexRequestDto = new UserRequestDto().setFirstName(ALEX_FIRST_NAME)
                .setLastName(ALEX_LAST_NAME)
                .setAddress(ALEX_ADDRESS)
                .setEmail(ALEX_EMAIL)
                .setBirthDate(ALEX_BIRTH_DATE)
                .setPhoneNumber(ALEX_PHONE_NUMBER);

        sophiaRequestDto = new UserRequestDto().setFirstName(SOPHIA_FIRST_NAME)
                .setLastName(SOPHIA_LAST_NAME)
                .setAddress(SOPHIA_ADDRESS)
                .setEmail(SOPHIA_EMAIL)
                .setBirthDate(SOPHIA_BIRTH_DATE)
                .setPhoneNumber(SOPHIA_PHONE_NUMBER);

        sophia = new User().setFirstName(SOPHIA_FIRST_NAME)
                .setLastName(SOPHIA_LAST_NAME)
                .setAddress(SOPHIA_ADDRESS)
                .setEmail(SOPHIA_EMAIL)
                .setBirthDate(SOPHIA_BIRTH_DATE)
                .setPhoneNumber(SOPHIA_PHONE_NUMBER)
                .setId(ALEX_ID);

        alexUpdateRequestDto = new UserUpdateRequestDto().setFirstName(WILLIAM_FIRST_NAME)
                .setBirthDate(WILLIAM_BIRTH_DATE)
                .setEmail(WILLIAM_EMAIL);

        userSearchRequestDto = new UserSearchRequestDto().setFromDate(FROM).setToDate(TO);
    }

    @BeforeEach
    void setUp() {
        alexResponseDto = new UserResponseDto().setFirstName(ALEX_FIRST_NAME)
                .setLastName(ALEX_LAST_NAME)
                .setAddress(ALEX_ADDRESS)
                .setEmail(ALEX_EMAIL)
                .setId(ALEX_ID)
                .setBirthDate(ALEX_BIRTH_DATE)
                .setPhoneNumber(ALEX_PHONE_NUMBER);
        sophiaResponseDto = new UserResponseDto().setFirstName(SOPHIA_FIRST_NAME)
                .setLastName(SOPHIA_LAST_NAME)
                .setAddress(SOPHIA_ADDRESS)
                .setEmail(SOPHIA_EMAIL)
                .setBirthDate(SOPHIA_BIRTH_DATE)
                .setPhoneNumber(SOPHIA_PHONE_NUMBER)
                .setId(ALEX_ID);

        ReflectionTestUtils.setField(userServiceImpl, ADULT, minAge);
    }

    @Test
    @DisplayName("""
            Verify registerUser() method
            """)
    void registerUser() {
        // when
        when(userRepository.findByEmail(ALEX_EMAIL)).thenReturn(Optional.empty());
        when(userMapper.toEntity(alexRequestDto)).thenReturn(alex);
        when(userRepository.save(alex)).thenReturn(alex);
        when(userMapper.toDto(alex)).thenReturn(alexResponseDto);

        // then
        UserResponseDto actual = userServiceImpl.registerUser(alexRequestDto);
        UserResponseDto expected = alexResponseDto;
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify updateUser() method
            """)
    void updateUser() {
        // given
        User william = alex.setFirstName(WILLIAM_FIRST_NAME).setBirthDate(WILLIAM_BIRTH_DATE)
                .setEmail(WILLIAM_EMAIL);
        UserResponseDto williamResponseDto = alexResponseDto.setFirstName(WILLIAM_FIRST_NAME)
                .setBirthDate(WILLIAM_BIRTH_DATE).setEmail(WILLIAM_EMAIL);

        // when
        when(userRepository.findById(ALEX_ID)).thenReturn(Optional.of(alex));
        when(userRepository.save(william)).thenReturn(william);
        when(userMapper.toDto(william)).thenReturn(williamResponseDto);

        // then
        UserResponseDto actual = userServiceImpl
                .updateUser(ALEX_ID, alexUpdateRequestDto);
        UserResponseDto expected = williamResponseDto;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify fullUpdateUser() method
            """)
    void fullUpdateUser() {
        // when
        when(userRepository.findById(ALEX_ID)).thenReturn(Optional.of(alex));
        when(userMapper.toEntity(sophiaRequestDto)).thenReturn(sophia);
        when(userRepository.save(sophia)).thenReturn(sophia);
        when(userMapper.toDto(sophia)).thenReturn(sophiaResponseDto);

        // then
        UserResponseDto actual = userServiceImpl
                .fullUpdateUser(ALEX_ID, sophiaRequestDto);
        UserResponseDto expected = sophiaResponseDto;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getAllUsersByBirthDateRange() method
            """)
    void getAllUsersByBirthDateRange() {
        //given
        List<User> users = Arrays.asList(alex, sophia);
        List<UserResponseDto> expectedResponse = Arrays.asList(alexResponseDto, sophiaResponseDto);

        // when
        when(userRepository.getAllByBirthDateAfterAndBirthDateBefore(FROM, TO)).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(expectedResponse);
        List<UserResponseDto> actualResponse = userServiceImpl
                .getAllUsersByBirthDateRange(userSearchRequestDto);

        // then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }
}
