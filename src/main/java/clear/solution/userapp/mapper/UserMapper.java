package clear.solution.userapp.mapper;

import clear.solution.userapp.config.MapperConfig;
import clear.solution.userapp.dto.UserRequestDto;
import clear.solution.userapp.dto.UserResponseDto;
import clear.solution.userapp.model.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtoList(List<User> userList);

}
