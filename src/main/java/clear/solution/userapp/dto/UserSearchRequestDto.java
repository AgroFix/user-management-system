package clear.solution.userapp.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UserSearchRequestDto {
    private LocalDate fromDate;

    private LocalDate toDate;
}
