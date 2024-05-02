package clear.solution.userapp.controller;

import clear.solution.userapp.dto.UserRequestDto;
import clear.solution.userapp.dto.UserResponseDto;
import clear.solution.userapp.dto.UserSearchRequestDto;
import clear.solution.userapp.dto.UserUpdateRequestDto;
import clear.solution.userapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Users management", description = "Endpoints to manage users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user",
            description = "Endpoint for registering a new user")
    public UserResponseDto registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.registerUser(userRequestDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user's information",
            description = "Endpoint for updating user's information")
    public UserResponseDto updateSomeUserInformation(@PathVariable Long id,
                                                     @Valid @RequestBody
                                                     UserUpdateRequestDto userUpdateRequestDto) {
        return userService.updateUser(id, userUpdateRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update all user's information",
            description = "Endpoint for updating all user's information")
    public UserResponseDto updateAllUserInformation(@PathVariable Long id,
                                                    @Valid @RequestBody
                                                    UserRequestDto userRequestDto) {
        return userService.fullUpdateUser(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user", description = "Endpoint for deleting a user")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/searchByDate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users by birth date range",
            description = "Endpoint for searching users by birth date range")
    public List<UserResponseDto> getAllUsersByBirthDateRange(
            @Valid @RequestBody UserSearchRequestDto userSearchRequestDto) {
        return userService.getAllUsersByBirthDateRange(userSearchRequestDto);
    }
}
