package clear.solution.userapp.service.impl;

import clear.solution.userapp.dto.UserRequestDto;
import clear.solution.userapp.dto.UserResponseDto;
import clear.solution.userapp.dto.UserSearchRequestDto;
import clear.solution.userapp.dto.UserUpdateRequestDto;
import clear.solution.userapp.exception.EntityNotFoundException;
import clear.solution.userapp.exception.RegistrationException;
import clear.solution.userapp.exception.ValidationException;
import clear.solution.userapp.mapper.UserMapper;
import clear.solution.userapp.model.User;
import clear.solution.userapp.repository.UserRepository;
import clear.solution.userapp.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String REGISTRATION_ERROR_MESSAGE = "This email is already used: ";
    private static final String USER_FIND_ERROR_MESSAGE = "Can't find a user with id ";
    private static final String DATE_SEARCH_ERROR_MESSAGE = "Input dates isn't correct";
    private static final String MINIMUM_AGE_ERROR_MESSAGE = "Input age is less than permissible!!!";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${adult.age}")
    private Integer adultAge;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException(REGISTRATION_ERROR_MESSAGE + userRequestDto.getEmail());
        }
        User user = userMapper.toEntity(userRequestDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(USER_FIND_ERROR_MESSAGE + id));
        return userMapper.toDto(userRepository.save(updateUserInfo(user,
                userUpdateRequestDto)));
    }

    @Override
    public UserResponseDto fullUpdateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(USER_FIND_ERROR_MESSAGE + id));
        User updatedUser = userMapper.toEntity(userRequestDto);
        updatedUser.setId(id);
        return userMapper.toDto(userRepository.save(updatedUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> getAllUsersByBirthDateRange(
            UserSearchRequestDto userSearchRequestDto) {
        if (userSearchRequestDto.getFromDate()
                .isAfter(userSearchRequestDto.getToDate())) {
            throw new ValidationException(DATE_SEARCH_ERROR_MESSAGE);
        }

        List<User> users = userRepository
                .getAllByBirthDateAfterAndBirthDateBefore(userSearchRequestDto.getFromDate(),
                userSearchRequestDto.getToDate());

        return userMapper.toDtoList(users);
    }

    private User updateUserInfo(User user, UserUpdateRequestDto userUpdateRequestDto) {
        Optional.ofNullable(userUpdateRequestDto.getEmail())
                .ifPresent(user::setEmail);

        Optional.ofNullable(userUpdateRequestDto.getAddress())
                .ifPresent(user::setAddress);

        Optional.ofNullable(userUpdateRequestDto.getBirthDate())
                .filter(birthDate -> LocalDate.now().isAfter(birthDate.plusYears(adultAge)))
                .ifPresent(user::setBirthDate);

        Optional.ofNullable(userUpdateRequestDto.getFirstName())
                .ifPresent(user::setFirstName);

        Optional.ofNullable(userUpdateRequestDto.getLastName())
                .ifPresent(user::setLastName);

        Optional.ofNullable(userUpdateRequestDto.getPhoneNumber())
                .ifPresent(user::setPhoneNumber);

        return user;
    }

}
