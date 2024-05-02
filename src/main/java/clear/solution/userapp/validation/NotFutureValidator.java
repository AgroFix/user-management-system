package clear.solution.userapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotFutureValidator implements ConstraintValidator<NotFuture, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        return date.isBefore(currentDate);
    }
}
