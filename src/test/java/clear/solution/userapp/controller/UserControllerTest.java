package clear.solution.userapp.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import clear.solution.userapp.dto.UserRequestDto;
import clear.solution.userapp.dto.UserResponseDto;
import clear.solution.userapp.dto.UserSearchRequestDto;
import clear.solution.userapp.dto.UserUpdateRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    private static final long ALEX_ID = 1L;
    private static final String ALEX_EMAIL = "alex@gmail.com";
    private static final String ALEX_FIRST_NAME = "Alex";
    private static final String ALEX_LAST_NAME = "Brown";
    private static final LocalDate ALEX_BIRTH_DATE = LocalDate.of(1988, 3, 10);

    private static final String WILLIAM_NEW_EMAIL = "martinez@gmail.com";
    private static final String WILLIAM_FIRST_NAME = "William";
    private static final String WILLIAM_LAST_NAME = "Martinez";
    private static final LocalDate WILLIAM_BIRTH_DATE = LocalDate.of(1979, 11, 25);
    private static final String WILLIAM_ADDRESS = "987 Cedar St";
    private static final String WILLIAM_PHONE_NUMBER = "+380777888999";
    private static final String USERS = "/api/users";
    private static final String CLASSPATH_DATABASE_USERS = "classpath:database/";
    private static final String DELETE_USERS_FROM_USERS_TABLE_SQL = CLASSPATH_DATABASE_USERS
            + "delete-users-from-users-table.sql";
    private static final String ADD_USERS_TO_USERS_TABLE_SQL = CLASSPATH_DATABASE_USERS
            + "add-users-into-users-table.sql";
    private static final String USERS_ID_3 = USERS + "/3";
    private static final String USERS_ID_1 = USERS + "/1";
    private static final String ID = "id";
    private static final int FROM = 30;
    private static final int TO = 18;
    private static final String CREATE_TABLE = "create-table-users.sql";
    private static UserRequestDto alexUserRequestDto;
    private static UserResponseDto alexUserResponseDto;
    private static UserUpdateRequestDto williamUserUpdateRequestDto;
    private static UserResponseDto williamUserResponseDto;
    private static UserSearchRequestDto userSearchRequestDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();

        alexUserRequestDto = new UserRequestDto()
                .setEmail(ALEX_EMAIL)
                .setFirstName(ALEX_FIRST_NAME)
                .setLastName(ALEX_LAST_NAME)
                .setBirthDate(ALEX_BIRTH_DATE);

        alexUserResponseDto = new UserResponseDto()
                .setId(ALEX_ID)
                .setEmail(alexUserRequestDto.getEmail())
                .setFirstName(alexUserRequestDto.getFirstName())
                .setLastName(alexUserRequestDto.getLastName())
                .setBirthDate(alexUserRequestDto.getBirthDate());

        williamUserUpdateRequestDto = new UserUpdateRequestDto()
                .setEmail(WILLIAM_NEW_EMAIL);

        williamUserResponseDto = new UserResponseDto()
                .setEmail(williamUserUpdateRequestDto.getEmail())
                .setFirstName(WILLIAM_FIRST_NAME)
                .setLastName(WILLIAM_LAST_NAME)
                .setBirthDate(WILLIAM_BIRTH_DATE)
                .setAddress(WILLIAM_ADDRESS)
                .setPhoneNumber(WILLIAM_PHONE_NUMBER);

        userSearchRequestDto = new UserSearchRequestDto().setFromDate(LocalDate.now()
                .minusYears(FROM)).setToDate(LocalDate.now().minusYears(TO));
    }

    @Test
    @DisplayName("""
            Verify register() method
            """)
    @Sql(
            scripts = CLASSPATH_DATABASE_USERS + CREATE_TABLE,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {CLASSPATH_DATABASE_USERS + CREATE_TABLE, DELETE_USERS_FROM_USERS_TABLE_SQL},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void register_ValidUser_ShouldReturnUserResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        post(USERS)
                                .content(objectMapper.writeValueAsString(alexUserRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto expected = alexUserResponseDto;

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class
        );

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, ID));
    }

    @Test
    @DisplayName("""
            Verify updateSomeUserInformation() method
            """)
    @Sql(
            scripts = {CLASSPATH_DATABASE_USERS + CREATE_TABLE, ADD_USERS_TO_USERS_TABLE_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_USERS_FROM_USERS_TABLE_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void updateSomeUserInformation_ValidData_ShouldReturnUserResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        patch(USERS_ID_3)
                                .content(objectMapper.writeValueAsString(
                                        williamUserUpdateRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto expected = williamUserResponseDto;

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class
        );

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, ID));
    }

    @Test
    @DisplayName("""
            Verify updateAllUserInformation() method
            """)
    @Sql(
            scripts = {CLASSPATH_DATABASE_USERS + CREATE_TABLE, ADD_USERS_TO_USERS_TABLE_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_USERS_FROM_USERS_TABLE_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void updateAllUserInformation_ValidData_ShouldReturnUserResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        put(USERS_ID_1)
                                .content(objectMapper.writeValueAsString(alexUserRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto expected = alexUserResponseDto;

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class
        );

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, ID));
    }

    @Test
    @DisplayName("""
            Verify getAllUsersByBirthDateRange() method
            """)
    @Sql(
            scripts = {CLASSPATH_DATABASE_USERS + CREATE_TABLE, ADD_USERS_TO_USERS_TABLE_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_USERS_FROM_USERS_TABLE_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAllUsersByBirthDateRange_ValidInputs_ReturnUserResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(
                get(USERS + "/searchByDate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSearchRequestDto)))
                .andReturn();
        List<UserResponseDto> actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                new TypeReference<List<UserResponseDto>>(){});
        List<UserResponseDto> expected = List.of(alexUserResponseDto);
        assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected, actual, ID);
    }
}
