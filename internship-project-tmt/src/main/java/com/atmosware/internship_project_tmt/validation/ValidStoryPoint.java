package com.atmosware.internship_project_tmt.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@SuppressWarnings("unused")
@Documented
@Constraint(validatedBy = StoryPointValidator.class)
// kurallar StoryPointValidatior'da
@Target({ElementType.FIELD})
// değişkenler sadece field üzerine yazılabilir
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStoryPoint {
    String message() default "Story Point yalnızca 1, 2, 3, 5, 8, 13 değerlerinden biri olabilir.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}