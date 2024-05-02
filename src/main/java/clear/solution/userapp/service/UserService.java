package clear.solution.userapp.service;

import clear.solution.userapp.dto.UserRequestDto;
import clear.solution.userapp.dto.UserResponseDto;
import clear.solution.userapp.dto.UserSearchRequestDto;
import clear.solution.userapp.dto.UserUpdateRequestDto;
import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto);

    UserResponseDto fullUpdateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);

    List<UserResponseDto> getAllUsersByBirthDateRange(UserSearchRequestDto userSearchRequestDto);

}
