package clear.solution.userapp.dto;

import clear.solution.userapp.validation.Adult;
import clear.solution.userapp.validation.NotFuture;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UserRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @NotFuture
    @Adult
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
