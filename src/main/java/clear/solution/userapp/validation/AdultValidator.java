package clear.solution.userapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {
    @Value("${adult.age}")
    private Integer adultAge;

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext
            constraintValidatorContext) {
        if (birthDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(birthDate.plusYears(adultAge));
    }
}
